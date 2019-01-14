package com.neuq.question.domain.feed;


import com.neuq.question.data.dao.ActivityNewsFeedRepository;
import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.service.InAPINewsFeedRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wangshyi
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NewsFeedDataSynchronizerFactory {

    private final ConferenceRepository conferenceRepository;

    private final ActivityNewsFeedRepository newsFeedRepository;

    private final InAPINewsFeedRestService newsFeedRestService;

    public NewsFeedDataSynchronizer build(String conferenceId, String activityId, Date startDay) {

        return new NewsFeedDataSynchronizer(newsFeedRepository, conferenceRepository, newsFeedRestService, startDay,
                conferenceId, activityId);
    }

}
