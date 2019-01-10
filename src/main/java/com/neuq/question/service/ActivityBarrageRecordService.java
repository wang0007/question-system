package com.neuq.question.service;


import com.neuq.question.data.pojo.ActivityBarrageRecordDO;
import com.neuq.question.data.pojo.InAPIUser;

import java.util.List;

/**
 * @author sunhuih
 * @date 2018/8/29  15:46
 */

public interface ActivityBarrageRecordService {
    /**
     * 拉取活动弹幕
     *
     * @param conferenceId 大会id
     * @param partitions   弹幕分区
     * @param ts           时间戳
     * @return 活动弹幕
     */

    List<ActivityBarrageRecordDO> queryBarrage(String conferenceId, List<Integer> partitions, Long ts);

    /**
     * 存储活动弹幕
     *
     * @param conferenceId 大会ID
     * @param content      弹幕内容
     * @param user 用户信息
     */
    void saveBarrage(String conferenceId, String content, InAPIUser user);
}
