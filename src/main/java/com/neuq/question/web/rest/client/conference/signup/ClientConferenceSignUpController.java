package com.neuq.question.web.rest.client.conference.signup;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import com.neuq.question.data.dao.SignUpFormRepository;
import com.neuq.question.data.dao.SignUpRecordRepository;
import com.neuq.question.data.dao.SignUpSettingRepository;
import com.neuq.question.data.pojo.ConferenceSignUpFormDO;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.data.pojo.ConferenceSignUpSettingDO;
import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.error.MobileDuplicateErrorException;
import com.neuq.question.error.TimeExpiredException;
import com.neuq.question.error.TimeNotArrivedException;
import com.neuq.question.service.SignUpRecordService;
import com.neuq.question.service.events.signup.SignUpEventDispatcher;
import com.neuq.question.service.events.signup.SignUpEventFactory;
import com.neuq.question.service.events.signup.pojo.SignUpEvent;
import com.neuq.question.support.LocaleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yegk7
 * @since 2018/8/14 14:06
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/v1/client/{conferenceId}/signup")
@Api(value = "移动端-大会报名", description = "移动端-大会报名")
public class ClientConferenceSignUpController {

    private final SignUpFormRepository repository;

    private final SignUpSettingRepository settingRepository;

    private final SignUpRecordService signUpRecordService;

    private final SignUpRecordRepository recordRepository;

    private final SignUpEventFactory signUpEventFactory;

    private final SignUpEventDispatcher signUpEventDispatcher;

    /**
     * 获取大会报名表
     *
     * @param conferenceId 大会ID
     * @return 大会报名表信息
     */
    @GetMapping(value = "")
    public ConferenceSignUpFormDO getForm(@PathVariable("conferenceId") String conferenceId) {

        ConferenceSignUpFormDO formDO = repository.queryById(conferenceId);
        List<ConferenceSignUpFormDO.FormField> formFields = formDO.getFields()
                .stream()
                .peek(formField -> {
                    if (formField.getI18nLabel() != null && formField.getI18nLabel().get(LocaleUtil.getLocale()) != null) {
                        formField.setLabel(formField.getI18nLabel().get(LocaleUtil.getLocale()));
                    }
                })
                .collect(Collectors.toList());
        formDO.setFields(formFields);

        return formDO;
    }

    /**
     * 添加报名记录
     *
     * @param conferenceId 大会id
     * @param recordDTO    大会详情
     * @return 报名记录
     */
    @ApiOperation(value = "大会报名", notes = "大会报名")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceSignUpRecordDO create(@PathVariable("conferenceId") String conferenceId,
                                           @RequestBody ConferenceSignUpRecordDTO recordDTO) {

        checkSignUpTime(conferenceId);

        ConferenceSignUpRecordDO recordDO = new ConferenceSignUpRecordDO();
        recordDO.setConferenceId(conferenceId);
        recordDO.setStatus(AuditStatus.PENDING);

        checkSignUpRecord(recordDTO.getFields(), conferenceId);
        recordDO.setFields(recordDTO.getFields());

        signUpRecordService.insertSignUpRecord(recordDO);

        SignUpEvent event = signUpEventFactory.build(conferenceId, recordDO);
        signUpEventDispatcher.dispatch(event);

        return recordDO;
    }

    /**
     * 检查报名时间是否在设定时间内
     *
     * @param conferenceId 大会Id
     */
    private void checkSignUpTime(String conferenceId) {
        ConferenceSignUpSettingDO settingDO = settingRepository.queryById(conferenceId);

        long ts = System.currentTimeMillis();
        if (ts < settingDO.getStartTime()) {
            throw new TimeNotArrivedException("sign up time not arrive");
        }

        if (ts > settingDO.getClosingTime()) {
            throw new TimeExpiredException("sign up time expired");
        }
    }

    private void checkSignUpRecord(List<ConferenceSignUpRecordDO.FormFieldValue> fields, String conferenceId) {

        for (ConferenceSignUpRecordDO.FormFieldValue fieldValue : fields) {

            if (ConferenceSignUpRecordDO.FIXED_NAME_ID.equals(fieldValue.getFieldId())) {
                // 判断姓名是否合法
                if (fieldValue.getValue() == null || fieldValue.getValue().length() < 1 || fieldValue.getValue().length() > 50) {
                    throw new ECIllegalArgumentException("username is illegal");
                }
            }

            if (ConferenceSignUpRecordDO.FIXED_MOBILE_ID.equals(fieldValue.getFieldId())) {
                // 判断手机号是否重复报名
                String mobile = fieldValue.getValue();
                List<String> recordDOList = recordRepository.allMobileList(conferenceId);
                if (recordDOList.contains(mobile)) {
                    throw new MobileDuplicateErrorException("mobile is already registered");
                }

                // 判断手机号是否合法
                try {
                    Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().parse(mobile, "CN");
                    if (!PhoneNumberUtil.getInstance().isPossibleNumber(phoneNumber)) {
                        throw new ECIllegalArgumentException("mobile is illegals");

                    }

                } catch (NumberParseException e) {
                    String msg = "invalid phone number format " + mobile;
                    throw new ECIllegalArgumentException(msg, e);
                }

            }

        }
    }

    /**
     * 大会报名记录
     */
    @Data
    public static class ConferenceSignUpRecordDTO {

        private List<ConferenceSignUpRecordDO.FormFieldValue> fields;
    }

}
