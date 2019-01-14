package com.neuq.question.service;


import com.neuq.question.data.pojo.ConferenceAppDO;

/**
 * @author wangshyi
 */
public interface ConferenceAppService {

    /**
     * 获取大会模板的APP, 如果没有则插入数据库并返回默认值
     *
     * @param conferenceId 大会ID
     * @return 大会应用数据
     */
    ConferenceAppDO queryConferenceAppWithDefault(String conferenceId);

}
