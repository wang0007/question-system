package com.neuq.question.web.rest.management.conference.signup;


import com.neuq.question.data.dao.SignUpSettingRepository;
import com.neuq.question.data.pojo.ConferenceSignUpSettingDO;
import com.neuq.question.service.SignUpFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 报名设置接口
 *
 * @author liuhaoi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/signup/setting")
@RequiredArgsConstructor
@Api(value = "报名设置接口", description = "报名设置接口")
public class SignUpSettingController {

    private final SignUpSettingRepository repository;

    private final SignUpFormService formService;


    /**
     * 获取大会报名设置
     *
     * @param conferenceId 大会ID
     * @return 报名设置
     */
    @ApiOperation(value = "获取大会报名设置", notes = "获取大会报名设置")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceSignUpSettingDO getById(@PathVariable("conferenceId") String conferenceId) {

        return repository.queryById(conferenceId);
    }

    /**
     * 更改大会报名设置
     *
     * @param conferenceId 大会ID
     * @param setting      报名设置
     * @return 更新后的报名设置
     */
    @ApiOperation(value = "更改大会报名设置", notes = "更改大会报名设置")
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceSignUpSettingDO update(@PathVariable("conferenceId") String conferenceId,
                                            @RequestBody SignUpSettingDTO setting) {

        ConferenceSignUpSettingDO oldSetting = repository.queryById(conferenceId);

        repository.update(conferenceId, setting);

        formService.processInvitationCodeField(conferenceId, setting, oldSetting);

        return repository.queryById(conferenceId);
    }


    @Data
    public static class SignUpSettingDTO {

        private Long closingTime;

        private Long startTime;

        private Boolean needInvitationCode;

    }

}
