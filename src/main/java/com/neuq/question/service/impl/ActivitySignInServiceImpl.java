package com.neuq.question.service.impl;


import com.neuq.question.data.dao.*;
import com.neuq.question.data.pojo.*;
import com.neuq.question.domain.enums.SignInScope;
import com.neuq.question.domain.enums.SignInType;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.error.ECUnauthorizedException;
import com.neuq.question.service.ActivitySignInService;
import com.neuq.question.service.ConferenceActivityService;
import com.neuq.question.service.events.signin.SignInEventDispatcher;
import com.neuq.question.service.events.signin.SignInEventFactory;
import com.neuq.question.service.events.signin.pojo.SignInEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangshyi
 * @create 2018/11/17 19:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivitySignInServiceImpl implements ActivitySignInService {

    private final ActivitySignInRecordRepository signInRecordRepository;

    private final ConferenceActivityRepository activityRepository;

    private final ConferenceActivityService activityService;

    private final AutoIncrementIdRepository increaseRepository;

    private final SignInEventDispatcher signInEventDispatcher;

    private final SignUpRecordRepository signUpRecordRepository;

    private final SignInEventFactory signInEventFactory;

    private final ConferenceGuestRepository guestRepository;

    private final ActivitySignInSettingRepository signInSettingRepository;

    @Override
    public SignInEvent activitySignIn(InAPIUser user, String activityId, SignInType type, String staffMemberId) {

        ConferenceActivityDO activityDO = activityRepository.query(activityId);
        if (activityDO == null) {
            throw new ECIllegalArgumentException("Activity Not exists when sign in");
        }

        checkUserPermission(user, activityDO);

        boolean alreadySignIn = true;
        ActivitySignInRecordDO signInRecordDO = signInRecordRepository
                .querySignInRecord(activityId, user.getMemberId());
        if (signInRecordDO == null) {
            signInRecordDO = signInDTOTranslateToDO(user, activityId, activityDO.getConferenceId(), type, staffMemberId);
            signInRecordDO.setTimestamp(System.currentTimeMillis());
            signInRecordRepository.insert(signInRecordDO);
            alreadySignIn = false;
        }

        SignInEvent event = signInEventFactory.build(signInRecordDO, user, activityId);
        event.setAlreadySignIn(alreadySignIn);

        signInEventDispatcher.dispatch(event);

        return event;
    }

    private void checkUserPermission(InAPIUser user, ConferenceActivityDO activityDO) {

        ActivitySignInSettingDO settingDO = signInSettingRepository.queryByActivityId(activityDO.getId());

        SignInScope scope = settingDO.getScope();

        boolean isGuest;
        boolean isSignUp;

        switch (scope) {
             case GUEST:
                isGuest = guestRepository.isGuest(activityDO.getConferenceId(), user.getMobile());
                if (isGuest) {
                    return;
                }
                throw new ECUnauthorizedException("user has no permission to sign in, should be guest");
            case SIGNUP:
                // 判断是否为已报名人员或者默认参会人员
                isSignUp = signUpRecordRepository
                        .isUserSignedUpPassed(activityDO.getConferenceId(), user.getMobile());
                if (isSignUp) {
                    return;
                }
                throw new ECUnauthorizedException("user has no permission to sign in, should sign up");
            case SIGNUP_AND_GUEST:
            default:
                isGuest = guestRepository.isGuest(activityDO.getConferenceId(), user.getMobile());
                isSignUp = signUpRecordRepository.isUserSignedUpPassed(activityDO.getConferenceId(), user.getMobile());

                if (isSignUp || isGuest) {
                    return;
                }
                throw new ECUnauthorizedException("user has no permission to sign in, should be guest or sign up");
        }

    }

    /**
     * 将签到DTO转换为DO
     *
     * @return 签到DO
     */
    private ActivitySignInRecordDO signInDTOTranslateToDO(InAPIUser user, String activityId, String conferenceId,
                                                          SignInType type, String staffMemberId) {

        ActivitySignInRecordDO signInRecordDO = new ActivitySignInRecordDO();
        signInRecordDO.setType(type);
        signInRecordDO.setUser(user);
        signInRecordDO.setActivityId(activityId);
        if (staffMemberId != null) {
            signInRecordDO.setStaffMemberId(staffMemberId);
        }

        signInRecordDO.setConferenceId(conferenceId);

        String category = buildAutoIncrementIdCategory(activityId);
        int sequence = increaseRepository.nextID(category).intValue();
        signInRecordDO.setSequence(sequence);

        return signInRecordDO;
    }

    private static final String ID_TEMPLATE = "SIGN_IN::%s";

    private String buildAutoIncrementIdCategory(String activityId) {

        return String.format(ID_TEMPLATE, activityId);

    }

}
