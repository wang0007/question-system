package com.neuq.question.service.events.signin.pojo;


import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.*;
import com.neuq.question.service.ConferenceActivityService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuhaoi
 */
@Data
public class SignInEvent {

    private final ConferenceRepository repository;

    private final ConferenceActivityService activityService;

    private final ActivitySignInSettingRepository signInSettingRepository;

    private InAPIUser user;

    private final String activityId;

    private Boolean alreadySignIn;

    /**
     * 签到记录
     */
    private final ActivitySignInRecordDO record;

    /**
     * 大会
     */
    private ConferenceDO conference;

    /**
     * 活动
     */
    private ConferenceActivityDO activity;

    /**
     * 签到设置
     */
    private ActivitySignInSettingDO setting;

    public SignInEvent(ConferenceRepository repository,
                       ConferenceActivityService activityService,
                       ActivitySignInSettingRepository signInSettingRepository,
                       InAPIUser user, String activityId,
                       ActivitySignInRecordDO record) {
        this.repository = repository;
        this.activityService = activityService;
        this.signInSettingRepository = signInSettingRepository;
        this.user = user;
        this.activityId = activityId;
        this.record = record;
    }


    public InAPIUser getUser() {

        return user;
    }

    public ConferenceDO getConference() {

        if (conference == null) {
            conference = activityService.findAndVerifyConference(activityId);
        }

        return conference;
    }

    public ConferenceActivityDO getActivity() {

        if (activity == null) {
            activity = activityService.findAndVerifyActivity(activityId);
        }

        return activity;
    }

    public ActivitySignInSettingDO getSetting() {

        if (setting == null) {
            setting = signInSettingRepository.queryByActivityId(activityId);
        }

        return setting;
    }

    public boolean alreadySignIn() {
        return alreadySignIn != null && alreadySignIn;
    }
}
