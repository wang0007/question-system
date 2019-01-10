package com.neuq.question.service.events.signin;


import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.ActivitySignInRecordDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.service.ConferenceActivityService;
import com.neuq.question.service.events.signin.pojo.SignInEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liuhaoi
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SignInEventFactory {

    private final ConferenceRepository repository;

    private final ConferenceActivityService activityService;

    private final ActivitySignInSettingRepository signInSettingRepository;


    public SignInEvent build(ActivitySignInRecordDO record, InAPIUser user, String activityId) {

        return new SignInEvent(repository, activityService, signInSettingRepository, user, activityId, record);

    }


}
