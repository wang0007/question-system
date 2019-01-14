package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.ConferenceAppDO;

/**
 * @author wangshyi
 */
public interface ConferenceAppRepository {

    ConferenceAppDO queryByConferenceId(String conferenceId);

    void save(ConferenceAppDO app);

    long updateStatus(String conferenceId, String appId, boolean status);

}
