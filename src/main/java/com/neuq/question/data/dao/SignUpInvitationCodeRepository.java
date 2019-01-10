package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ConferenceSignUpInvitationCodeDO;
import com.neuq.question.web.rest.management.conference.signup.SignUpInvitationCodeController;

import java.util.List;

/**
 * @author yegk7
 * @since 2018/7/13 18:20
 */
public interface SignUpInvitationCodeRepository {

    /**
     * 生成邀请码
     *
     * @param codeDO 邀请码信息
     */
    void create(ConferenceSignUpInvitationCodeDO codeDO);

    /**
     * 更新邀请码信息
     *
     * @param conferenceId     大会ID
     * @param invitationCodeId 邀请码id
     * @param codeDTO          邀请码信息
     * @return 成功条数
     */
    long update(String conferenceId, String invitationCodeId, SignUpInvitationCodeController.ConferenceSignUpInvitationCodeDTO codeDTO);

    /**
     * 邀请码数量
     *
     * @param conferenceId 大会id
     * @return 数量
     */
    long count(String conferenceId);

    /**
     * 邀请码列表
     *
     * @param conferenceId 大会id
     * @param start        开始数
     * @param size         size
     * @return 邀请码list
     */
    List<ConferenceSignUpInvitationCodeDO> list(String conferenceId, int start, int size);

    /**
     * 修改邀请码状态
     *
     * @param conferenceId     大会ID
     * @param invitationCodeId 邀请码id
     * @param enable           邀请码状态
     * @return 成功条数
     */
    long changeEnable(String conferenceId, String invitationCodeId, Boolean enable);

    /**
     * 根据id查询信息
     *
     * @param invitationCodeId 邀请码id
     * @return 邀请码信息
     */
    ConferenceSignUpInvitationCodeDO queryById(String invitationCodeId);

    /**
     * 查询大会下邀请码列表
     *
     * @param conferenceId 大会ID
     * @return 邀请码列表
     */
    List<String> invitationCodeList(String conferenceId);

}
