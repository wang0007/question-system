package com.neuq.question.web.rest.management.barrage;
import com.neuq.question.data.dao.ActivityBarrageRecordRepository;
import com.neuq.question.data.dao.ActivityBarrageSettingRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ActivityBarrageRecordDO;
import com.neuq.question.data.pojo.ActivityBarrageSettingDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.ActivityBarrageRecordService;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.web.rest.pojo.ConferenceBarrageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author sunhuih
 * @date 2018/8/29  15:42
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/barrage/record")
@RequiredArgsConstructor
@Api(value = "活动弹幕接口", description = "活动弹幕接口")
public class ActivityBarrageRecordController {

    private final ActivityBarrageRecordService barrageService;

    private final ActivityBarrageRecordRepository barrageRepository;

    private final ActivityBarrageSettingRepository barrageSettingRepository;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    /**
     * 增量获取弹幕
     *
     * @param conferenceId 大会ID
     * @param ts           上次请求的时间戳，第一次为-1
     * @param partition    弹幕分区
     * @return 实时弹幕
     */

    @ApiOperation(value = "增量拉取活动弹幕，投屏使用", notes = "ts首次传入-1")
    @GetMapping(value = "/incremental/{ts}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceBarrageResult<ActivityBarrageRecordDO> pullBarrage(@PathVariable("conferenceId") String conferenceId,
                                                                        @PathVariable("ts") Long ts,
                                                                        @RequestParam("partition[]") Integer[] partition) {

        List<Integer> partitions = Arrays.asList(partition);
        ActivityBarrageSettingDO activityBarrageSettingDO = barrageSettingRepository.queryById(conferenceId);
        List<ActivityBarrageRecordDO> barrages = barrageService.queryBarrage(conferenceId, partitions, ts);

        ConferenceBarrageResult<ActivityBarrageRecordDO> conferenceBarrageResult = new ConferenceBarrageResult<>();
        conferenceBarrageResult.setBarrageSpeed(activityBarrageSettingDO.getBarrageSpeed() == null ? 10 : activityBarrageSettingDO.getBarrageSpeed());
        conferenceBarrageResult.setEnterFrequency(activityBarrageSettingDO.getEnterFrequency() == null ? 500 : activityBarrageSettingDO.getEnterFrequency());
        conferenceBarrageResult.setClearBarrage(activityBarrageSettingDO.getClearBarrage() == null ? false : activityBarrageSettingDO.getClearBarrage());
        conferenceBarrageResult.setNextTs(System.currentTimeMillis());
        conferenceBarrageResult.setData(barrages);
        return conferenceBarrageResult;
    }

    /**
     * 清空弹幕
     *
     * @param conferenceId 活动id
     */
    @DeleteMapping(value = "")
    @ApiOperation(value = "清空弹幕", notes = "清空弹幕")
    public void emptyActivityBarrage(@PathVariable("conferenceId") String conferenceId,
                                     @RequestParam("memberId") String memberId) {

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        barrageRepository.deleteBarrage(conferenceId);

        operationLogService.createOperationLog(inAPIUser, OperationType.DELETE, conferenceId,
                null, System.currentTimeMillis(), "clear barrage");
    }

}
