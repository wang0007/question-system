package com.neuq.question.service.events.activity.impl;


import com.neuq.question.data.dao.ActivityAgendaRepository;
import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.pojo.*;
import com.neuq.question.service.events.activity.ActivityCreateEvent;
import com.neuq.question.service.events.activity.ActivityCreateEventListener;
import com.neuq.question.support.LocaleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liuhaoi
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializeActivityCreateEventListener implements ActivityCreateEventListener {

    private final ActivityAgendaRepository agendaRepository;

    private final ActivitySignInSettingRepository signInSettingRepository;


    @Override
    public void onCreate(ActivityCreateEvent event) {

        ConferenceActivityDO activity = event.getActivity();
        initAgenda(activity);
        initSignInSetting(activity);
    }


    private void initAgenda(ConferenceActivityDO activity) {

        ActivityAgendaDO activityAgendaDO = agendaRepository.queryById(LocaleUtil.getDefaultIdWithLocale());

        if (activityAgendaDO != null) {
            activityAgendaDO.setConferenceId(activity.getConferenceId());
            activityAgendaDO.setActivityId(activity.getId());
            activityAgendaDO.setId(null);
            agendaRepository.save(activityAgendaDO);
        }
    }


    private void initSignInSetting(ConferenceActivityDO activity) {

        ActivitySignInSettingDO settingDO = signInSettingRepository.queryByActivityId(LocaleUtil.getDefaultIdWithLocale());

        settingDO.setActivityId(activity.getId());
        settingDO.setConferenceId(activity.getConferenceId());
        settingDO.setId(null);

        signInSettingRepository.save(settingDO);
    }

}
