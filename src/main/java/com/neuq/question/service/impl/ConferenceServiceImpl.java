package com.neuq.question.service.impl;


import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.error.ECResourceNotFoundException;
import com.neuq.question.service.ConferenceService;
import com.neuq.question.service.events.conference.ConferenceCreateEvent;
import com.neuq.question.service.events.conference.ConferenceCreateEventDispatcher;
import com.neuq.question.web.rest.management.conference.ConferenceController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuhaoi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;

    private final ConferenceCreateEventDispatcher dispatcher;

    @Override
    public ConferenceDO findAndVerifyConference(String conferenceId) {
        ConferenceDO conferenceDO = conferenceRepository.queryByConferenceId(conferenceId);

        if (conferenceDO == null) {
            throw new ECResourceNotFoundException("conference not found with id: " + conferenceId);
        }

        return conferenceDO;
    }

    @Override
    public ConferenceDO createConference(String creator, ConferenceController.ConferenceDTO conferenceDTO) {

        ConferenceDO conference = new ConferenceDO();

        conference.setEnable(true);
        conference.setImage(conferenceDTO.getImage());
        conference.setName(conferenceDTO.getName());
        conference.setCreator(creator);
        conference.setStartTime(conferenceDTO.getStartTime());
        // 将结束时间改为当天最晚时间
        long endTime = DateUtils.addDays(conferenceDTO.getEndTime(), 1).getTime() - 1;
        conference.setEndTime(new Date(endTime));
        conference.setTopic(conferenceDTO.getTopic());
        conference.setHelpSignInRoles(conferenceDTO.getHelpSignInRoles());

        conferenceRepository.save(conference);

        dispatcher.dispatch(new ConferenceCreateEvent(conference));

        return conference;
    }

    @Override
    public List<ConferenceDO> sortConferenceList(List<ConferenceDO> list) {

        // 进行中的大会排在前面，按开始时间正序排列
        List<ConferenceDO> sortList = list.stream()
                .filter(conferenceDO -> conferenceDO.getEndTime().after(new Date()) &&
                        conferenceDO.getStartTime().before(new Date()))
                .sorted(Comparator.comparing(ConferenceDO::getStartTime))
                .collect(Collectors.toList());

        // 未开始的大会排在中间，按开始时间倒序排列
        sortList.addAll(list.stream()
                .filter(conferenceDO -> conferenceDO.getStartTime().after(new Date()))
                .sorted(Comparator.comparing(ConferenceDO::getStartTime).reversed())
                .collect(Collectors.toList()));

        // 完成的大会排在进行中大会之后，按结束时间倒序排列
        sortList.addAll(list.stream()
                .filter(conferenceDO -> conferenceDO.getEndTime().before(new Date()))
                .sorted(Comparator.comparing(ConferenceDO::getEndTime).reversed())
                .collect(Collectors.toList()));

        return sortList;
    }


}
