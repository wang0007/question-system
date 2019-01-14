package com.neuq.question.service.events.conference.impl;

import com.neuq.question.configuration.ApplicationProperties;
import com.neuq.question.data.dao.*;
import com.neuq.question.data.pojo.*;

import com.neuq.question.service.events.conference.ConferenceCreateEvent;
import com.neuq.question.service.events.conference.ConferenceCreateEventListener;
import com.neuq.question.support.LocaleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author wangshyi
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializeConferenceCreateEventListener implements ConferenceCreateEventListener {

    private final ConferenceAppRepository appRepository;

    private final ConferenceGuideRepository repository;

    private final SignUpFormRepository formRepository;

    private final SignUpSettingRepository signUpSettingRepository;


    private final ApplicationProperties properties;

    @Override
    public void onCreate(ConferenceCreateEvent event) {

        ConferenceDO conference = event.getConference();

        initConferenceApp(conference);
        initConferenceGuide(conference);
        initConferenceSignUpSetting(conference);
        initConferenceSignUpForm(conference);

    }

    private void initConferenceApp(ConferenceDO conference) {

        ConferenceAppDO conferenceAppDO = appRepository.queryByConferenceId(ConferenceAppDO.DEFAULT_APP_ID);

        conferenceAppDO.getApps().stream()
                .filter(app -> StringUtils.isNotBlank(app.getUrl()))
                .forEach(app -> {
                    app.setUrl(app.getUrl()
                            .replace(ConferenceAppDO.CONFERENCE_ID_PLACE_HOLDER, conference.getConferenceId()));
                    app.setName(app.getI18nName().get(LocaleUtil.getLocale()));
                });

        conferenceAppDO.setConferenceId(conference.getConferenceId());
        conferenceAppDO.setId(null);
        appRepository.save(conferenceAppDO);
    }

    private void initConferenceGuide(ConferenceDO conference) {

        List<ConferenceGuideDO> data = repository.list(LocaleUtil.getDefaultIdWithLocale(), 0, 3);
        data.forEach(conferenceGuideDO -> {
            conferenceGuideDO.setConferenceId(conference.getConferenceId());
            conferenceGuideDO.setId(null);
        });

        repository.saveAll(data);
    }

    private void initConferenceSignUpSetting(ConferenceDO conference) {

        ConferenceSignUpSettingDO settingDO = new ConferenceSignUpSettingDO();
        settingDO.setNeedInvitationCode(false);
        // 设置默认报名截止时间为大会开始时间
        settingDO.setClosingTime(conference.getStartTime().getTime());

        // 设置默认报名开始时间为大会开始时间前三天
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(conference.getStartTime());
        int dayAddNum = -3;
        calendar.add(Calendar.DAY_OF_MONTH, dayAddNum);
        settingDO.setStartTime(calendar.getTimeInMillis());
        settingDO.setId(null);
        settingDO.setConferenceId(conference.getConferenceId());
        signUpSettingRepository.save(settingDO);
    }

    private void initConferenceSignUpForm(ConferenceDO conference) {

        ConferenceSignUpFormDO formDO = formRepository.queryById(ConferenceAppDO.DEFAULT_APP_ID);
        // 若大会报名表为空，添加默认字段到报名表中
        formDO.setConferenceId(conference.getConferenceId());

        // 若报名设置不需要邀请码，则将邀请码字段过滤
        List<ConferenceSignUpFormDO.FormField> tempFields = formDO.getFields().stream()
                .filter(formField -> !ConferenceSignUpRecordDO.FIXED_INVITE_CODE_ID.equals(formField.getFormFieldId()))
                .collect(Collectors.toList());
        formDO.setFields(tempFields);
        formDO.setFormTitle(conference.getName());
        formDO.setId(null);

        formRepository.save(formDO);
    }

}