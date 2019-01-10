package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.web.rest.management.conference.ConferenceActivityController;

import java.util.List;

/**
 * @author liuhaoi
 */
public interface ConferenceActivityRepository {

    /**
     * 活动列表
     *
     * @param conferenceId 大会ID
     * @param start        skip
     * @param size         limit
     * @return 列表
     */
    List<ConferenceActivityDO> list(String conferenceId, int start, int size);

    /**
     * 活动个数
     *
     * @param conferenceId 大会ID
     * @return 活动个数
     */
    long count(String conferenceId);

    /**
     * 大会活动
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     * @return 大会活动
     */
    ConferenceActivityDO query(String conferenceId, String activityId);

    /**
     * 大会活动
     *
     * @param activityId 活动ID
     * @return 大会活动
     */
    ConferenceActivityDO query(String activityId);


    /**
     * 创建大会活动
     *
     * @param activityId 活动ID
     * @param activity   活动
     */
    void create(String activityId, ConferenceActivityDO activity);

    /**
     * 更新大会活动
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     * @param activity     活动
     * @return 更改的条数, 应该为1
     */
    long update(String conferenceId, String activityId, ConferenceActivityController.ConferenceActivityDTO activity);

    /**
     * 删除大会活动
     *
     * @param conferenceId 会议ID
     * @param activityId   活动ID
     * @return 大会活动
     */
    long delete(String conferenceId, String activityId);

    /**
     * 获取大会下的主活动
     *
     * @param conferenceId 大会ID
     * @return 主活动
     */
    ConferenceActivityDO queryPrincipalActivity(String conferenceId);

    /**
     * 更新大会主活动
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     * @param principal    是否为主活动
     * @return 更新条数
     */
    long updatePrincipal(String conferenceId, String activityId, boolean principal);

}
