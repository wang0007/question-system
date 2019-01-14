package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.common.ConferenceBackgroundDO;
import com.neuq.question.domain.enums.BackgroundType;

/**
 * @author wangshyi
 * @since 2018/11/30 19:21
 */
public interface ConferenceBackgroundRepository {

    /**
     * 新增背景图
     *
     * @param conferenceBackgroundDO 背景图信息
     */
    void insert(ConferenceBackgroundDO conferenceBackgroundDO);

    /**
     * 通过大会ID和背景图类型查找背景图
     *
     * @param conferenceId   大会ID
     * @param backgroundType 背景图类型
     * @return 背景图信息
     */
    ConferenceBackgroundDO query(String conferenceId, BackgroundType backgroundType);

    /**
     * 插入一张背景图
     *
     * @param conferenceId   大会ID
     * @param backgroundBO   背景图信息
     * @param backgroundType 背景图类型
     * @return 插入条数
     */
    long insertBackgroundItem(String conferenceId, ConferenceBackgroundDO.BackgroundBO backgroundBO, BackgroundType backgroundType);

    /**
     * 删除指定的背景图
     *
     * @param conferenceId   大会ID
     * @param backgroundId   背景图ID
     * @param backgroundType 背景图类型
     * @return 删除条数
     */
    long delete(String conferenceId, String backgroundId, BackgroundType backgroundType);

    /**
     * 获取初始的九张背景图
     *
     * @param backgroundType 背景图类型
     * @return 背景图信息
     */
    ConferenceBackgroundDO getDefaultBackground(BackgroundType backgroundType);

}
