package com.neuq.question.service;

import com.neuq.question.data.pojo.ConferenceSignUpInvitationCodeDO;
import com.neuq.question.web.rest.management.conference.signup.SignUpInvitationCodeController;

/**
 * @author yegk7
 */
public interface SignUpInvitationCodeService {

    /**
     * 将codeDTO转换为DO，主要处理邀请码
     *
     * @param codeDTO      codeDTO
     * @param conferenceId 大会ID
     * @return 处理完的DO对象
     */
    ConferenceSignUpInvitationCodeDO translateToInvitationCodeDO(SignUpInvitationCodeController.ConferenceSignUpInvitationCodeDTO codeDTO,
                                                                 String conferenceId);

    /**
     * 更新邀请码
     *
     * @param conferenceId     大会id
     * @param invitationCodeId 邀请码id
     * @param codeDTO          邀请码详情
     */
    void updateInvitationCodeDO(String conferenceId, String invitationCodeId, SignUpInvitationCodeController.ConferenceSignUpInvitationCodeDTO codeDTO);
}
