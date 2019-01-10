package com.neuq.question.web.rest.management.conference.signup;


import com.neuq.question.data.dao.SignUpInvitationCodeRepository;
import com.neuq.question.data.dao.SignUpRecordRepository;
import com.neuq.question.data.pojo.ConferenceSignUpInvitationCodeDO;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.service.SignUpInvitationCodeService;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 大会报名邀请码设置
 *
 * @author liuhaoi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/signup/invitation")
@RequiredArgsConstructor
@Api(value = "大会报名邀请码设置", description = "大会报名邀请码设置")
public class SignUpInvitationCodeController {

    private final SignUpInvitationCodeRepository codeRepository;

    private final SignUpRecordRepository recordRepository;

    private final SignUpInvitationCodeService codeService;

    /**
     * 获取报名邀请码列表
     *
     * @param conferenceId 大会id
     * @param start        开始页
     * @param size         大小
     * @return 邀请码列表
     */
    @ApiOperation(value = "获取报名邀请码列表", notes = "获取报名邀请码列表")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<SignUpInvitationCodeVO> list(@PathVariable String conferenceId,
                                                   @RequestParam(defaultValue = "0") int start,
                                                   @RequestParam(defaultValue = "20") int size) {

        List<ConferenceSignUpInvitationCodeDO> codeDOList = codeRepository.list(conferenceId, start, size);

        List<ConferenceSignUpRecordDO> recordDOList = recordRepository.allList(conferenceId);
        List<SignUpInvitationCodeVO> codeVOList = codeDOList.stream().
                map(codeDO -> translateToVO(codeDO, recordDOList))
                .collect(Collectors.toList());

        long count = codeRepository.count(conferenceId);
        return new PageResult<>(codeVOList, count);

    }


    /**
     * 生成邀请码信息
     *
     * @param conferenceId 大会id
     * @param codeDTO      邀请码信息
     * @return 邀请码详情
     */
    @ApiOperation(value = "生成邀请码信息", notes = "生成邀请码信息")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceSignUpInvitationCodeDO generate(@PathVariable String conferenceId,
                                                     @RequestBody ConferenceSignUpInvitationCodeDTO codeDTO) {

        ConferenceSignUpInvitationCodeDO codeDO = codeService.translateToInvitationCodeDO(codeDTO, conferenceId);

        codeRepository.create(codeDO);
        return codeDO;
    }

    /**
     * 修改验证码信息
     *
     * @param conferenceId     大会id
     * @param invitationCodeId 邀请码id
     * @param codeDTO          邀请码信息
     * @return 邀请码list
     */
    @ApiOperation(value = "修改验证码信息", notes = "修改验证码信息")
    @PutMapping(value = "/{invitationCodeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceSignUpInvitationCodeDO update(@PathVariable String conferenceId,
                                                   @PathVariable String invitationCodeId,
                                                   @RequestBody ConferenceSignUpInvitationCodeDTO codeDTO) {

        codeService.updateInvitationCodeDO(conferenceId, invitationCodeId, codeDTO);

        return codeRepository.queryById(invitationCodeId);
    }

    /**
     * 修改邀请码状态
     *
     * @param conferenceId     大会id
     * @param invitationCodeId 邀请码id
     * @param enable           邀请码状态
     */
    @ApiOperation(value = "修改邀请码状态", notes = "修改邀请码状态")
    @PutMapping(value = "/enable/{invitationCodeId}")
    public void enableChange(@PathVariable String conferenceId,
                             @PathVariable String invitationCodeId,
                             @RequestParam("enable") Boolean enable) {

        codeRepository.changeEnable(conferenceId, invitationCodeId, enable);

    }

    /**
     * 统计报名邀请码对应的已报名人数和通过审核人数
     *
     * @param codeDO       邀请码信息
     * @param recordDOList 报名记录list
     * @return vo
     */
    private SignUpInvitationCodeVO translateToVO(ConferenceSignUpInvitationCodeDO codeDO, List<ConferenceSignUpRecordDO> recordDOList) {

        SignUpInvitationCodeVO codeVO = new SignUpInvitationCodeVO();
        codeVO.setChannel(codeDO.getChannel());
        codeVO.setCode(codeDO.getCode());
        codeVO.setId(codeDO.getId());
        codeVO.setConferenceId(codeDO.getConferenceId());
        codeVO.setEnable(codeDO.getEnable());
        codeVO.setCustomizable(codeDO.getCustomizable() == null ? false : codeDO.getCustomizable());

        if (recordDOList == null) {
            codeVO.setSignUpNum(0);
            codeVO.setCheckPassNum(0);
            return codeVO;
        }

        // 获取使用对应邀请码报名的人数
        long signUpNum = recordRepository.countByInvitationCode(codeDO.getCode(), null);

        // 获取使用对应邀请码报名并且通过审核的人数
        long checkPassNum = recordRepository.countByInvitationCode(codeDO.getCode(), AuditStatus.PASS);
        codeVO.setSignUpNum((int) signUpNum);
        codeVO.setCheckPassNum((int) checkPassNum);

        return codeVO;
    }

    @Data
    public static class ConferenceSignUpInvitationCodeDTO {

        private String channel;

        private String code;

    }

    @Data
    public static class SignUpInvitationCodeVO {

        private String id;

        private String conferenceId;

        /**
         * 已报名人数
         */
        private Integer signUpNum;

        /**
         * 审核通过人数
         */
        private Integer checkPassNum;

        private String code;

        /**
         * 邀请码渠道
         */
        private String channel;

        /**
         * 是否启用
         */
        private Boolean enable;

        /**
         * 是否为自定义
         */
        private Boolean customizable;
    }
}
