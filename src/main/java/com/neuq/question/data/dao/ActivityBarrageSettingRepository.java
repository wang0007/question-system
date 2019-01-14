package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.ActivityBarrageSettingDO;
import com.neuq.question.web.rest.management.barrage.ActivityBarrageSettingController;

/**
 * @author wangshyi
 * @since 2018/11/5 16:37
 */
public interface ActivityBarrageSettingRepository {

    /**
     * 查询弹幕设置
     *
     * @param conferenceId 活动ID
     * @return 弹幕设置
     */
    ActivityBarrageSettingDO queryById(String conferenceId);

    /**
     * 更新弹幕设置
     *
     * @param activityBarrageSettingDO 弹幕设置
     * @param conferenceId             大会ID
     * @return 更新成功条数
     */
    long update(ActivityBarrageSettingDO activityBarrageSettingDO, String conferenceId);

    /**
     * 保存到数据库
     *
     * @param setting 设置
     */
    void save(ActivityBarrageSettingDO setting);

    /**
     * 更新弹幕速度相关设置
     *
     * @param barrageSettingDTO 弹幕速度设置
     * @param conferenceId      大会Id
     * @return 更新成功条数
     */
    long updateSpeed(ActivityBarrageSettingController.BarrageSettingDTO barrageSettingDTO, String conferenceId);
}
