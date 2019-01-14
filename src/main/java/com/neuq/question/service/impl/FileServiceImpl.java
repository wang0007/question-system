package com.neuq.question.service.impl;


import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author wangshyi
 * @since 2018/11/11 15:01
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ConferenceRepository conferenceRepository;

    private final ConferenceActivityRepository activityRepository;

    @Override
    public StringBuilder buildFileName(String conferenceId, String activityId, String fileName) {

        ConferenceActivityDO activityDO = activityRepository.query(conferenceId, activityId);
        ConferenceDO conferenceDO = conferenceRepository.queryByConferenceId(conferenceId);

        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(conferenceDO.getName())
                .append("-")
                .append(activityDO.getName())
                .append(fileName);

        return fileNameBuilder;
    }

    @Override
    public StringBuilder buildFileName(String conferenceId, String fileName) {

        ConferenceDO conferenceDO = conferenceRepository.queryByConferenceId(conferenceId);

        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(conferenceDO.getName())
                .append("-")
                .append(fileName);

        return fileNameBuilder;
    }
}
