package com.neuq.question.service;

import com.neuq.question.data.pojo.ConferenceSignUpSettingDO;
import com.neuq.question.web.rest.management.conference.signup.SignUpSettingController;

/**
 * @author wangshyi
 * @since 2018/11/10 18:35
 */
public interface SignUpFormService {

    /**
     * 设置大会报名表
     *
     * @param conferenceId 大会ID
     * @param setting      设置信息
     * @param oldSetting   原始设置
     */
    void processInvitationCodeField(String conferenceId, SignUpSettingController.SignUpSettingDTO setting,
                                    ConferenceSignUpSettingDO oldSetting);

}
