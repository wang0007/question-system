package com.neuq.question.service;

import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.web.rest.management.conference.ConferenceActivityController;

/**
 * @author liuhaoi
 */
public interface ConferenceActivityService {

    /**
     * 查找活动并且校验是否存在
     *
     * @param activityId 活动ID
     * @return 活动
     */
    ConferenceActivityDO findAndVerifyActivity(String activityId);

    /**
     * 查找大会信息，并校验是否存在
     *
     * @param activityId 活动ID
     * @return 大会信息
     */
    ConferenceDO findAndVerifyConference(String activityId);


    /**
     * 创建大会
     *
     * @param conferenceId 大会ID
     * @param activity     活动信息
     * @return 活动对象
     */
    ConferenceActivityDO createActivity(String conferenceId,
                                        ConferenceActivityController.ConferenceActivityDTO activity);


}
