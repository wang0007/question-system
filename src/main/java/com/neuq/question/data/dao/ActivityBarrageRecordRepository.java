package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.ActivityBarrageRecordDO;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/11/29  15:57
 */
public interface ActivityBarrageRecordRepository {

    /**
     * 清空大会弹幕
     *
     * @param conferenceId 大会ID
     */
    void deleteBarrage(String conferenceId);

    /**
     * 存储大会弹幕
     *
     * @param barrageDo 弹幕信息
     */
    void saveBarrage(ActivityBarrageRecordDO barrageDo);

    /**
     * 拉取大会弹幕
     *
     * @param conferenceId 大会id
     * @param partitions   弹幕分区号
     * @param ts           时间戳
     * @return 弹幕
     */
    List<ActivityBarrageRecordDO> queryBarrage(String conferenceId, List<Integer> partitions, Long ts);
}
