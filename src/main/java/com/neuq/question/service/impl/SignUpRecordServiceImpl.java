package com.neuq.question.service.impl;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import com.neuq.question.data.dao.SignUpInvitationCodeRepository;
import com.neuq.question.data.dao.SignUpRecordRepository;
import com.neuq.question.data.dao.SignUpSettingRepository;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.data.pojo.ConferenceSignUpInvitationCodeDO;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.data.pojo.ConferenceSignUpSettingDO;
import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.error.InviteCodeErrorException;
import com.neuq.question.error.VerifyCodeErrorException;
import com.neuq.question.service.ConferenceService;
import com.neuq.question.service.InAPISmsService;
import com.neuq.question.service.SignUpRecordService;
import com.neuq.question.service.VerifyCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 * @since 2018/11/16 20:02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpRecordServiceImpl implements SignUpRecordService {

    private static final String STRING_NULL = "_NULL_";

    private static final String INVITE_CODE_ERROR_MESSAGE = "An exception occurred when the user inserted the invitation code, " +
            "conference id = %s and invite code = %s .due to the invitation code error.";

    private final SignUpInvitationCodeRepository codeRepository;

    private final SignUpRecordRepository recordRepository;

    private final SignUpSettingRepository signUpSettingRepository;

    private final VerifyCodeService verifyCodeService;

    private final InAPISmsService inAPISmsService;

    private final MessageSource messageSource;

    private final ConferenceService conferenceService;

    @Override
    public void insertSignUpRecord(ConferenceSignUpRecordDO recordDO) {

        if (inviteCodeRequired(recordDO.getConferenceId())) {
            handleInviteCode(recordDO);
        }
        handleVerifyCode(recordDO);

        recordRepository.create(recordDO);
    }

    @Override
    public List<List<Object>> getSignUpRecordList(String conferenceId, List<AuditStatus> auditStatuses, String invitationCode, String fieldId, String fieldValue, Integer start, Integer size) {

        List<ConferenceSignUpRecordDO> signUpRecordDOList = recordRepository
                .list(conferenceId, auditStatuses, invitationCode, fieldId, fieldValue, 0, 50000);

        return signUpRecordDOList.stream()
                .map(this::translateToSignUpList)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAuditRecord(String conferenceId, List<String> mobiles, AuditStatus status) {

        recordRepository.updateAuditRecord(conferenceId, mobiles, status);

        if (status.equals(AuditStatus.PASS)) {

            ConferenceDO conference = conferenceService.findAndVerifyConference(conferenceId);

            for (String mobile : mobiles) {

                long nationNumber;
                try {
                    Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().parse(mobile, "CN");
                    nationNumber = phoneNumber.getNationalNumber();
                } catch (NumberParseException e) {
                    String msg = "invalid phone number format " + mobile;
                    throw new ECIllegalArgumentException(msg, e);
                }

                Map<String, String> data = new HashMap<>(5);
                data.put("mobile", String.valueOf(nationNumber));
                data.put("mettingName", conference.getName());

                String language = LocaleContextHolder.getLocale().toString();
                inAPISmsService.sendSms(mobile, data);
            }
        }

    }


    /**
     * 将signUpRecordDo 转换为指定字段的list
     *
     * @param recordDO 报名记录
     * @return 记录list
     */
    private List<Object> translateToSignUpList(ConferenceSignUpRecordDO recordDO) {

        List<Object> list = new ArrayList<>();

        list.add(0, queryFieldValue(ConferenceSignUpRecordDO.FIXED_NAME_ID, recordDO));
        list.add(1, queryFieldValue(ConferenceSignUpRecordDO.FIXED_MOBILE_ID, recordDO));
        list.add(2, queryFieldValue(ConferenceSignUpRecordDO.FIXED_INVITE_CODE_ID, recordDO));
        list.add(3, new Date(recordDO.getUtime()));
        list.add(4, setRecordType(recordDO.getStatus()));

        return list;
    }


    /**
     * 查询字段值
     *
     * @param fixedId  字段id
     * @param recordDO 报名记录
     * @return 字段值
     */
    private String queryFieldValue(String fixedId, ConferenceSignUpRecordDO recordDO) {

        List<ConferenceSignUpRecordDO.FormFieldValue> formFieldValues = recordDO.getFields();

        Optional<ConferenceSignUpRecordDO.FormFieldValue> optional = formFieldValues.stream()
                .filter(formFieldValue -> fixedId.equals(formFieldValue.getFieldId()))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get().getValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 设置审核状态显示值
     *
     * @param status 审核状态
     * @return 显示值
     */
    private String setRecordType(AuditStatus status) {
        switch (status) {
            case PASS:
                return messageSource.getMessage("pass", new Object[]{}, LocaleContextHolder.getLocale());
            case PENDING:
                return messageSource.getMessage("pending", new Object[]{}, LocaleContextHolder.getLocale());
            case REJECT:
                return messageSource.getMessage("reject", new Object[]{}, LocaleContextHolder.getLocale());
            default:
                return messageSource.getMessage("pending", new Object[]{}, LocaleContextHolder.getLocale());
        }
    }

    private void handleVerifyCode(ConferenceSignUpRecordDO recordDO) {

        List<ConferenceSignUpRecordDO.FormFieldValue> fieldList = recordDO.getFields();

        Optional<ConferenceSignUpRecordDO.FormFieldValue> optionalFormFieldMobile = fieldList
                .stream()
                .filter(fieldValue -> fieldValue.getFieldId().equals(ConferenceSignUpRecordDO.FIXED_MOBILE_ID))
                .findFirst();
        if (!optionalFormFieldMobile.isPresent()) {
            throw new ECIllegalArgumentException("mobile is need");
        }
        String mobile = optionalFormFieldMobile.get().getValue();

        Optional<ConferenceSignUpRecordDO.FormFieldValue> optionalFormFieldCode = fieldList
                .stream()
                .filter(fieldValue -> fieldValue.getFieldId().equals(ConferenceSignUpRecordDO.FIXED_VERIFY_CODE_ID))
                .findFirst();
        if (!optionalFormFieldCode.isPresent()) {
            throw new ECIllegalArgumentException("verify code is need");
        }
        String verifyCode = optionalFormFieldCode.get().getValue();

        String key = buildSignUpKey(recordDO.getConferenceId(), mobile);

        if (!verifyCodeService.verifyCodeMatch(key, verifyCode)) {
            throw new VerifyCodeErrorException("verify code error!");
        }
    }


    /**
     * 判断是否必须要邀请码
     *
     * @param conferenceId 大会ID
     * @return 是否要邀请码
     */
    private boolean inviteCodeRequired(String conferenceId) {
        ConferenceSignUpSettingDO settingDO = signUpSettingRepository.queryById(conferenceId);
        if (settingDO == null) {
            throw new ECIllegalArgumentException("can't find conference " + conferenceId);
        }
        return settingDO.getNeedInvitationCode();
    }

    /**
     * 处理邀请码，若不存在则抛出异常
     *
     * @param recordDO 签到记录
     */
    private void handleInviteCode(ConferenceSignUpRecordDO recordDO) {

        // 判断传入值是否包含邀请码
        Optional<ConferenceSignUpRecordDO.FormFieldValue> value = recordDO.getFields().stream()
                .filter(formFieldValue -> ConferenceSignUpRecordDO.FIXED_INVITE_CODE_ID
                        .equals(formFieldValue.getFieldId()))
                .findFirst();
        if (!value.isPresent()) {
            throw new InviteCodeErrorException("invitation code is required, but the invitation code does not exist.");
        }

        // 判断传入的邀请码是否在数据库中
        List<ConferenceSignUpInvitationCodeDO> codeDOList = codeRepository.list(recordDO.getConferenceId(), 0, 1000);
        boolean codeExist = codeDOList.stream()
                .anyMatch(codeDO -> codeDO.getCode().equals(value.get().getValue()) && codeDO.getEnable());
        if (!codeExist) {
            throw new InviteCodeErrorException(String.format(INVITE_CODE_ERROR_MESSAGE, recordDO.getConferenceId(), value.get().getValue()));
        }
    }

    private String buildSignUpKey(String conferenceId, String mobile) {

        return String.format("sign_up_verify_code:%s:%s", conferenceId == null ? STRING_NULL : conferenceId,
                mobile == null ? STRING_NULL : mobile);
    }

}
