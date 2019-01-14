package com.neuq.question.web.rest.management.conference;


import com.neuq.question.data.dao.ConferenceAppRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceAppDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.ConferenceAppService;
import com.neuq.question.service.ConferenceService;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.web.rest.pojo.UpdateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 大会活动接口
 *
 * @author wangshyi
 */
@Api(value = "大会应用管理接口", description = "大会应用管理接口")
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/app")
@RequiredArgsConstructor
public class ConferenceAppController {

    private final ConferenceService conferenceService;

    private final ConferenceAppService appService;

    private final ConferenceAppRepository appRepository;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    @ApiOperation("获取大会应用列表")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceAppDO queryByConference(@PathVariable String conferenceId) {

        conferenceService.findAndVerifyConference(conferenceId);

        return appService.queryConferenceAppWithDefault(conferenceId);
    }

    @ApiOperation("更改大会应用状态")
    @PatchMapping("/{appId}/status")
    public UpdateResult updateAppStatus(@PathVariable String conferenceId,
                                        @PathVariable String appId,
                                        @RequestParam("memberId") String memberId,
                                        @RequestParam boolean enable) {

        conferenceService.findAndVerifyConference(conferenceId);

        long i = appRepository.updateStatus(conferenceId, appId, enable);

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, System.currentTimeMillis(), "update conference app status,appId=" + appId);

        return UpdateResult.builder().n(i).build();

    }


}
