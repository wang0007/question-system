package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ActivityNewsFeedSettingDO;
import com.neuq.question.web.rest.management.feed.NewsFeedSettingController;

/**
 * @author wangshyi
 * @date 2018/11/30 11:07
 */
public interface ActivityNewsFeedSettingRepository {

    /**
     * 更新发言投屏背景图设置
     *
     * @param activityId 活动ID
     * @param settingDTO 背景图设置
     * @return 成功条数
     */
    long updateProjection(String activityId, NewsFeedSettingController.NewsFeedProjectionSettingDTO settingDTO);

    /**
     * 获取发言投屏设置
     *
     * @param activityId 活动ID
     * @return 发言投屏设置
     */
    ActivityNewsFeedSettingDO queryByActivityId(String activityId);

    /**
     * 保存发言投屏设置
     *
     * @param settingDO 投屏设置
     */
    void save(ActivityNewsFeedSettingDO settingDO);

}
