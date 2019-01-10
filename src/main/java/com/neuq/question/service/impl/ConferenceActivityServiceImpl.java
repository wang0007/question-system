package com.neuq.question.service.impl;


import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.error.ECResourceNotFoundException;
import com.neuq.question.error.PrincipalActivityExistException;
import com.neuq.question.service.ConferenceActivityService;
import com.neuq.question.service.ConferenceService;
import com.neuq.question.service.events.activity.ActivityCreateEvent;
import com.neuq.question.service.events.activity.ActivityCreateEventDispatcher;
import com.neuq.question.web.rest.management.conference.ConferenceActivityController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author liuhaoi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConferenceActivityServiceImpl implements ConferenceActivityService {

    private final ConferenceActivityRepository activityRepository;

    private final ConferenceService conferenceService;

    private final ActivityCreateEventDispatcher dispatcher;

    @Override
    public ConferenceActivityDO findAndVerifyActivity(String activityId) {

        if (StringUtils.isBlank(activityId)) {
            throw new ECResourceNotFoundException("invalid activity id " + activityId + " not exists");
        }


        ConferenceActivityDO activity = activityRepository.query(activityId);

        if (activity == null) {
            throw new ECResourceNotFoundException("activity " + activityId + " not exists");
        }

        return activity;
    }

    @Override
    public ConferenceDO findAndVerifyConference(String activityId) {

        ConferenceActivityDO activity = findAndVerifyActivity(activityId);

        return conferenceService.findAndVerifyConference(activity.getConferenceId());
    }

    @Override
    public ConferenceActivityDO createActivity(String conferenceId,
                                               ConferenceActivityController.ConferenceActivityDTO activityDTO) {

        ConferenceDO conference = conferenceService.findAndVerifyConference(conferenceId);

        ConferenceActivityDO activityDO = activityRepository.queryPrincipalActivity(conferenceId);
        if (activityDO != null && activityDTO.getPrincipal() != null && activityDTO.getPrincipal()) {
            throw new PrincipalActivityExistException("principal activity has exist");
        }

        ConferenceActivityDO activity = new ConferenceActivityDO();

        activity.setPrincipal(activityDTO.getPrincipal() == null ? false : activityDTO.getPrincipal());
        activity.setActivityDate(activityDTO.getActivityDate());
        activity.setEndTime(activityDTO.getEndTime());
        activity.setName(activityDTO.getName());
        activity.setStartTime(activityDTO.getStartTime());
        activity.setConferenceId(conferenceId);

        activityRepository.create(conferenceId, activity);

        dispatcher.dispatch(new ActivityCreateEvent(activity));

        return activity;
    }
}
