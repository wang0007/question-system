package com.neuq.question.service.impl;

import com.neuq.question.data.dao.SignUpInvitationCodeRepository;
import com.neuq.question.data.pojo.ConferenceSignUpInvitationCodeDO;
import com.neuq.question.error.ChineseErrorException;
import com.neuq.question.error.InviteCodeDuplicateErrorException;
import com.neuq.question.error.InviteCodeLengthErrorException;
import com.neuq.question.service.SignUpInvitationCodeService;
import com.neuq.question.support.ChineseUtil;
import com.neuq.question.web.rest.management.conference.signup.SignUpInvitationCodeController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangshyi
 */
@Service
@RequiredArgsConstructor
public class SignUpInvitationCodeServiceImpl implements SignUpInvitationCodeService {


    private final SignUpInvitationCodeRepository codeRepository;

    @Override
    public ConferenceSignUpInvitationCodeDO translateToInvitationCodeDO(SignUpInvitationCodeController.ConferenceSignUpInvitationCodeDTO codeDTO, String conferenceId) {

        ConferenceSignUpInvitationCodeDO codeDO = new ConferenceSignUpInvitationCodeDO();
        codeDO.setChannel(codeDTO.getChannel());
        codeDO.setConferenceId(conferenceId);
        codeDO.setEnable(true);
        if (codeDTO.getCode() != null) {
            String code = codeDTO.getCode();
            codeDO.setCode(verifyCode(code, conferenceId));
            codeDO.setCustomizable(true);
        } else {
            // 生成6位随机数作为邀请码
            codeDO.setCode(String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));
            codeDO.setCustomizable(false);
        }

        return codeDO;
    }

    @Override
    public void updateInvitationCodeDO(String conferenceId, String invitationCodeId, SignUpInvitationCodeController.ConferenceSignUpInvitationCodeDTO codeDTO) {

        verifyCode(codeDTO.getCode(), conferenceId);
        codeRepository.update(conferenceId, invitationCodeId, codeDTO);

    }

    private String verifyCode(String code, String conferenceId) {

        List<String> codeList = codeRepository.invitationCodeList(conferenceId);

        int maxLength = 10;
        int minLength = 6;
        if (code.length() > maxLength || code.length() < minLength) {
            throw new InviteCodeLengthErrorException("invite code length out of range");
        }

        if (codeList.contains(code)) {
            throw new InviteCodeDuplicateErrorException("invite code is already used");
        }

        if (ChineseUtil.isContainsChinese(code)) {
            throw new ChineseErrorException("invite code can't contains chinese");
        }
        return code;
    }


}
