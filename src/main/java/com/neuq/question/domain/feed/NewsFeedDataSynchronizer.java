package com.neuq.question.domain.feed;


import com.neuq.question.data.dao.ActivityNewsFeedRepository;
import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.ActivityNewsFeedRecordDO;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.data.pojo.InAPINewFeedItem;
import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.service.InAPINewsFeedRestService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 */

@Slf4j
public class NewsFeedDataSynchronizer {

    private final ActivityNewsFeedRepository repository;

    private final ConferenceRepository conferenceRepository;

    private final InAPINewsFeedRestService restService;

    private final Date startDay;

    private final String latestFeedId;

    private final String conferenceId;

    private final String activityId;

    private List<ActivityNewsFeedRecordDO> records;

    private ConferenceDO conference;

    public NewsFeedDataSynchronizer(ActivityNewsFeedRepository repository,
                                    ConferenceRepository conferenceRepository,
                                    InAPINewsFeedRestService restService, Date startDay,
                                    String conferenceId, String activityId) {
        this.repository = repository;
        this.conferenceRepository = conferenceRepository;
        this.restService = restService;
        this.startDay = startDay;

        ActivityNewsFeedRecordDO latestFeed = repository.queryLatestItem(activityId);
        if (latestFeed != null) {
            this.latestFeedId = latestFeed.getFeed().getId();
        } else {
            this.latestFeedId = "0";
        }

        this.conferenceId = conferenceId;
        this.activityId = activityId;
    }

    public void pull() {

        if (records != null) {
            return;
        }

        conference = conferenceRepository.queryByConferenceId(conferenceId);

        List<InAPINewFeedItem> feedItems = restService
                .queryNewsFeed(latestFeedId, startDay, 10000);

        records = feedItems.stream().map(item -> {

            ActivityNewsFeedRecordDO record = new ActivityNewsFeedRecordDO();
            record.setConferenceId(conferenceId);
            record.setActivityId(activityId);
            record.setAuditStatus(AuditStatus.PENDING);
            record.setFeed(item);
            return record;
        }).collect(Collectors.toList());

    }


    public void save() {
        repository.saveAll(records);
    }


}
