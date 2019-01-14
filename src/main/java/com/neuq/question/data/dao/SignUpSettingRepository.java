package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ConferenceSignUpSettingDO;
import com.neuq.question.web.rest.management.conference.signup.SignUpSettingController;

/**
 * @author wangshyi
 */
public interface SignUpSettingRepository {

    /**
     * 根据ID查询报名设置
     *
     * @param conferenceId 大会ID
     * @return 大会报名设置
     */
    ConferenceSignUpSettingDO queryById(String conferenceId);

    /**
     * 保存到数据库
     *
     * @param settingDO 设置信息
     */
    void save(ConferenceSignUpSettingDO settingDO);

    /**
     * 更新大会报名设置
     *
     * @param conferenceId 大会ID
     * @param setting      大会报名设置
     * @return 更改的条数, 应该为1
     */
    long update(String conferenceId, SignUpSettingController.SignUpSettingDTO setting);

}
