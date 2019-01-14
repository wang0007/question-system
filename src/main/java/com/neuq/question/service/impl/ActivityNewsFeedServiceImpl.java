package com.neuq.question.service.impl;


import com.neuq.question.data.dao.ActivityNewsFeedRepository;
import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.pojo.ActivityNewsFeedRecordDO;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.domain.feed.NewsFeedDataSynchronizer;
import com.neuq.question.domain.feed.NewsFeedDataSynchronizerFactory;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.service.ActivityNewsFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author liuhaoi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityNewsFeedServiceImpl implements ActivityNewsFeedService {

    private final NewsFeedDataSynchronizerFactory factory;

    private final ConferenceActivityRepository activityRepository;

    private final ActivityNewsFeedRepository newsFeedRepository;

    @Override
    public List<ActivityNewsFeedRecordDO> query(String activityId, String keyword, Date startTime, Date endTime,
                                                AuditStatus status, int start, int size) {

        return newsFeedRepository.query(activityId, keyword, startTime, endTime, status, start, size);
    }

    @Override
    public long count(String activityId, String keyword, Date startTime, Date endTime, AuditStatus status) {

        return newsFeedRepository.count(activityId, keyword, startTime, endTime, status);
    }

    @Override
    public void synchronize(String activityId) {
        ConferenceActivityDO activityDO = activityRepository.query(activityId);

        if (activityDO == null) {
            throw new ECIllegalArgumentException("activity not exists with id " + activityId);
        }

        NewsFeedDataSynchronizer synchronizer = factory.build(activityDO.getConferenceId(), activityId,
                new Date(System.currentTimeMillis() - 24L * 60 * 60 * 1000));

        synchronizer.pull();
        synchronizer.save();

    }

    @Override
    public List<ActivityNewsFeedRecordDO> incrementalPull(String activityId, long timestamp, int size) {

        long max = Math.max(System.currentTimeMillis() - 24L * 60 * 60 * 1000, timestamp);

        size = Math.min(size, 1000);

        return newsFeedRepository.incrementalPull(activityId, max, size);

    }
}
