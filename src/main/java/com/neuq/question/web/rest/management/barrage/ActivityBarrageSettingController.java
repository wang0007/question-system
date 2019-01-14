package com.neuq.question.web.rest.management.barrage;


import com.neuq.question.data.dao.ActivityBarrageSettingRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ActivityBarrageSettingDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.OperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author wangshyi
 * @since 2018/11/5 16:46
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/barrage/setting")
@RequiredArgsConstructor
@Api(value = "大会弹幕设置接口", description = "大会弹幕设置接口")
public class ActivityBarrageSettingController {

    private final ActivityBarrageSettingRepository settingRepository;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    /**
     * 获取大会弹幕设置
     *
     * @param conferenceId 大会ID
     * @return 邀请码设置
     */
    @ApiOperation(value = "获取大会弹幕设置", notes = "获取大会弹幕设置")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ActivityBarrageSettingDO getSetting(@PathVariable("conferenceId") String conferenceId) {

        return settingRepository.queryById(conferenceId);
    }

    /**
     * 更新大会弹幕设置
     *
     * @param conferenceId 大会ID
     * @param settingDO    大会弹幕信息
     * @return 弹幕设置
     */
    @ApiOperation(value = "更新大会弹幕设置", notes = "更新大会弹幕设置")
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ActivityBarrageSettingDO update(@PathVariable("conferenceId") String conferenceId,
                                           @RequestParam("memberId") String memberId,
                                           @RequestBody ActivityBarrageSettingDO settingDO) {

        settingRepository.update(settingDO, conferenceId);
        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, settingDO.getUtime(), "update barrage setting" + settingDO.toString());

        return settingDO;
    }

    /**
     * 更新大会弹幕速度相关设置（本接口不对外提供）
     *
     * @param conferenceId      大会ID
     * @param barrageSettingDTO 弹幕设置
     * @return 弹幕设置
     */
    @PutMapping(value = "/speed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ActivityBarrageSettingDO update(@PathVariable("conferenceId") String conferenceId,
                                           @RequestParam("memberId") String memberId,
                                           @RequestBody BarrageSettingDTO barrageSettingDTO) {

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        settingRepository.updateSpeed(barrageSettingDTO, conferenceId);

        ActivityBarrageSettingDO activityBarrageSettingDO = settingRepository.queryById(conferenceId);

        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, System.currentTimeMillis(), "update barrage speed setting" + barrageSettingDTO.toString());

        return activityBarrageSettingDO;
    }

    @Data
    @RequiredArgsConstructor
    public static class BarrageSettingDTO {

        /**
         * 是否清空弹幕
         */
        private Boolean clearBarrage;

        /**
         * 多少毫秒允许进入一个弹幕
         */
        private Integer enterFrequency;

        private Integer barrageSpeed;
    }
}
