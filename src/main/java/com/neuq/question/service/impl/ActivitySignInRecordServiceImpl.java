package com.neuq.question.service.impl;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import com.neuq.question.data.dao.ActivitySignInRecordRepository;
import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.dao.ConferenceGuestRepository;
import com.neuq.question.data.dao.SignUpRecordRepository;
import com.neuq.question.data.pojo.ActivitySignInRecordDO;
import com.neuq.question.data.pojo.ActivitySignInSettingDO;
import com.neuq.question.data.pojo.ConferenceGuestDO;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.domain.enums.SignInScope;
import com.neuq.question.domain.enums.SignInType;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.service.ActivitySignInRecordService;
import com.neuq.question.web.rest.pojo.NotSignInUserResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yegk7
 * @since 2018/7/19 20:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivitySignInRecordServiceImpl implements ActivitySignInRecordService {

    private final ActivitySignInRecordRepository activitySignInRecordRepository;

    private final SignUpRecordRepository signUpRecordRepository;

    private final ConferenceGuestRepository conferenceGuestRepository;

    private final ActivitySignInSettingRepository settingRepository;

    private final MessageSource messageSource;


    @Override
    public List<List<Object>> getSignInDataList(String activityId, String conferenceId) {
        List<ActivitySignInRecordDO> activitySignInRecordDOList = activitySignInRecordRepository.getAllRecordList(activityId);

        return activitySignInRecordDOList.stream()
                .map(signInRecordDO -> translateToSignInList(signInRecordDO, conferenceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<List<Object>> getNotSignInDataList(String activityId, String conferenceId) {

        List<NotSignInUserResult> notSignInUserList = getNotSignInList(activityId, conferenceId);

        return notSignInUserList.stream()
                .map(this::translateToNotSignInList)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotSignInUserResult> getNotSignInUserList(String activityId, String conferenceId) {

        return getNotSignInList(activityId, conferenceId);
    }

    /**
     * 将signInRecordDo 转换为指定字段的list
     *
     * @param signInRecordDO 签到记录
     * @param conferenceId   大会ID
     * @return 记录list
     */
    private List<Object> translateToSignInList(ActivitySignInRecordDO signInRecordDO, String conferenceId) {
        List<Object> list = new ArrayList<>();

        list.add(0, signInRecordDO.getUser().getName());
        list.add(1, signInRecordDO.getUser().getMobile());
        list.add(2, signInRecordDO.getUser().getEmail() == null ?
                signInRecordDO.getUser().getEmail() : signInRecordDO.getUser().getEmail());
        list.add(3, signUpType(signInRecordDO.getUser().getMobile(), conferenceId));
        list.add(4, new Date(signInRecordDO.getTimestamp()));
        list.add(5, signInRecordDO.getSequence());
        String alreadySignIn = messageSource
                .getMessage("already_sign_in", new Object[]{}, LocaleContextHolder.getLocale());
        list.add(6, alreadySignIn);
        list.add(7, setType(signInRecordDO.getType()));

        return list;
    }

    private List<Object> translateToNotSignInList(NotSignInUserResult notSignInUserResult) {
        List<Object> list = new ArrayList<>();
        list.add(0, notSignInUserResult.getUsername());
        list.add(1, notSignInUserResult.getMobile());
        list.add(2, notSignInUserResult.getEmail() == null ? "" : notSignInUserResult.getEmail());
        list.add(3, notSignInUserResult.getCompany() == null ? "" : notSignInUserResult.getCompany());
        list.add(4, notSignInUserResult.getDepartment() == null ? "" : notSignInUserResult.getDepartment());
        list.add(5, "");
        list.add(6, "");
        list.add(7, "");
        String notSignIn = messageSource
                .getMessage("not_sign_in", new Object[]{}, LocaleContextHolder.getLocale());
        list.add(8, notSignIn);
        list.add(9, "");

        return list;
    }

    /**
     * 设置签到方式,转换为中文导出到excel
     *
     * @param type 签到类型
     * @return 签到类型
     */
    private String setType(SignInType type) {
        if (type.equals(SignInType.SCAN_QR_CODE)) {
            return messageSource
                    .getMessage("scan_qr_code", new Object[]{}, LocaleContextHolder.getLocale());
        } else if (type.equals(SignInType.FACE)) {
            return messageSource
                    .getMessage("face_sign_in", new Object[]{}, LocaleContextHolder.getLocale());
        } else if (type.equals(SignInType.QR_CODE_CERTIFY)) {
            return messageSource
                    .getMessage("qr_code_certify", new Object[]{}, LocaleContextHolder.getLocale());
        } else if (type.equals(SignInType.HELP_SIGN_IN)) {
            return messageSource
                    .getMessage("help_sign_in", new Object[]{}, LocaleContextHolder.getLocale());
        } else {
            return messageSource
                    .getMessage("other_sign_in", new Object[]{}, LocaleContextHolder.getLocale());
        }
    }

    /**
     * 获取未签到用户列表
     *
     * @param activityId   活动ID
     * @param conferenceId 大会ID
     * @return 未签到用户列表
     */
    private List<NotSignInUserResult> getNotSignInList(String activityId, String conferenceId) {

        ActivitySignInSettingDO signInSettingDO = settingRepository.queryByActivityId(activityId);
        SignInScope scope = signInSettingDO.getScope();

        // 获取已签到人员的手机号列表，用手机号进行筛选
        List<String> signInUserMobileList = activitySignInRecordRepository.getAllRecordMobileList(activityId);

        if (SignInScope.SIGNUP.equals(scope)) {
            // 获取已报名人员
            List<NotSignInUserResult> signUpResult = getSignUpResult(conferenceId);
            return filterSignInResult(signUpResult, signInUserMobileList);

        }

        if (SignInScope.GUEST.equals(scope)) {
            // 获取导入的参会人员
            List<NotSignInUserResult> guestResult = getGuestResult(conferenceId);
            return filterSignInResult(guestResult, signInUserMobileList);
        }

        List<NotSignInUserResult> allUserResult = new ArrayList<>();
        allUserResult.addAll(getGuestResult(conferenceId));
        allUserResult.addAll(getSignUpResult(conferenceId));

        return filterSignInResult(allUserResult, signInUserMobileList);
    }

    /**
     * 过滤掉已签到人员
     *
     * @param resultList           报名或者导入人员
     * @param signInUserMobileList 已签到人员
     * @return 未签到人员列表
     */
    private List<NotSignInUserResult> filterSignInResult(List<NotSignInUserResult> resultList, List<String> signInUserMobileList) {

        return resultList.stream()
                .filter(result -> !signInUserMobileList.contains(result.getMobile()))
                .sorted(Comparator.comparing(NotSignInUserResult::getMobile))
                .collect(Collectors.toList());
    }

    /**
     * 获取参会人员列表
     *
     * @param conferenceId 大会ID
     * @return 参会人员列表
     */
    private List<NotSignInUserResult> getGuestResult(String conferenceId) {

        // 获取导入的参会人员
        List<ConferenceGuestDO> conferenceGuestDOList = conferenceGuestRepository.list(conferenceId, 0, Integer.MAX_VALUE);

        return conferenceGuestDOList.stream()
                .map(guestDO -> {
                    NotSignInUserResult notSignInUserResult = new NotSignInUserResult();
                    notSignInUserResult.setMobile(guestDO.getMobile());
                    notSignInUserResult.setUsername(guestDO.getName());
                    notSignInUserResult.setCompany(guestDO.getCompany());
                    notSignInUserResult.setDepartment(guestDO.getDepartment());
                    notSignInUserResult.setEmail(guestDO.getEmail());

                    return notSignInUserResult;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取已报名人员
     *
     * @param conferenceId 大会ID
     * @return 已报名人员
     */
    private List<NotSignInUserResult> getSignUpResult(String conferenceId) {

        // 获取已报名人员
        List<ConferenceSignUpRecordDO> signUpRecordDOList = signUpRecordRepository.passedList(conferenceId);

        return signUpRecordDOList.stream()
                .map(recordDO -> {
                    NotSignInUserResult notSignInUserResult = new NotSignInUserResult();

                    Optional<ConferenceSignUpRecordDO.FormFieldValue> nameOptional = recordDO.getFields().stream()
                            .filter(formFieldValue -> ConferenceSignUpRecordDO.FIXED_NAME_ID.equals(formFieldValue.getFieldId()))
                            .findFirst();

                    String userName = null;
                    if (nameOptional.isPresent()) {
                        userName = nameOptional.get().getValue();
                    }

                    Optional<ConferenceSignUpRecordDO.FormFieldValue> mobileOptional = recordDO.getFields().stream()
                            .filter(formFieldValue -> ConferenceSignUpRecordDO.FIXED_MOBILE_ID.equals(formFieldValue.getFieldId()))
                            .findFirst();

                    String mobile = null;
                    if (mobileOptional.isPresent()) {
                        mobile = mobileOptional.get().getValue();
                    }

                    // 由于从inApiUser拿到的用户手机号不包含国家前缀，做兼容处理
                    try {
                        Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().parse(mobile, "CN");
                        mobile = String.valueOf(phoneNumber.getNationalNumber());
                    } catch (NumberParseException e) {
                        String msg = "invalid phone number format " + mobile;
                        throw new ECIllegalArgumentException(msg, e);
                    }
                    notSignInUserResult.setUsername(userName);
                    notSignInUserResult.setMobile(mobile);

                    return notSignInUserResult;
                })
                .collect(Collectors.toList());

    }

    /**
     * 用户是否报名
     *
     * @param mobile       用户手机号
     * @param conferenceId 大会ID
     * @return 是否报名
     */
    private String signUpType(String mobile, String conferenceId) {

        List<NotSignInUserResult> signUpResult = getSignUpResult(conferenceId);

        if (signUpResult.isEmpty()) {
            return messageSource
                    .getMessage("not_sign_up", new Object[]{}, LocaleContextHolder.getLocale());
        }
        Optional<NotSignInUserResult> optional = signUpResult.stream()
                .filter(notSignInUserResult -> !notSignInUserResult.getMobile().equals(mobile))
                .findAny();
        if (!optional.isPresent()) {
            return messageSource
                    .getMessage("not_sign_up", new Object[]{}, LocaleContextHolder.getLocale());
        }
        return messageSource
                .getMessage("already_sign_up", new Object[]{}, LocaleContextHolder.getLocale());
    }

}
