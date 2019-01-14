package com.neuq.question.web.rest.management.feed;

import com.google.common.collect.Lists;
import com.neuq.question.data.dao.ActivityNewsFeedRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ActivityNewsFeedRecordDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.ActivityNewsFeedService;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.web.rest.pojo.IncrementalResult;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 动态接口
 *
 * @author wangshyi
 */
@Api(description = "发言上墙相关接口")
@Slf4j
@RestController
@RequestMapping("rest/v1/activity/{activityId}/feed/record")
@RequiredArgsConstructor
public class NewsFeedRecordController {

    private final ActivityNewsFeedService newsFeedService;

    private final ActivityNewsFeedRepository newsFeedRepository;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    private final static String BATCH_UPDATE_STATUS = "batch update activity feed status,status=%s,feedIds=%s";

    private final static String UPDATE_STATUS = "update activity feed status,status=%s,feedId=%s";

    /**
     * 列出大会活动的动态
     *
     * @param activityId 活动ID
     * @param keyword    关键字,null表示不过滤
     * @param startTime  开始时间,null表示不过滤
     * @param endTime    结束时间,null表示不过滤
     * @param status     审核状态,null表示不过滤
     * @return 分页结果
     */
    @ApiOperation(value = "获取发言数据")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<ActivityNewsFeedRecordDO> list(@PathVariable("activityId") String activityId,
                                                     @RequestParam(required = false) String keyword,
                                                     @RequestParam(required = false) Long startTime,
                                                     @RequestParam(required = false) Long endTime,
                                                     @RequestParam(required = false) AuditStatus status,
                                                     @RequestParam(defaultValue = "0") int start,
                                                     @RequestParam(defaultValue = "20") int size) {

        newsFeedService.synchronize(activityId);

        Date startDate = startTime == null ? null : new Date(startTime);
        Date endDate = endTime == null ? null : new Date(endTime);

        List<ActivityNewsFeedRecordDO> records = newsFeedService
                .query(activityId, keyword, startDate, endDate, status, start, size);

        long count = newsFeedService.count(activityId, keyword, startDate, endDate, status);

        return new PageResult<>(records, count);
    }

    /**
     * 批量审核活动动态
     *
     * @param activityId 活动ID
     * @param feedIds    动态ID
     * @param status     审核状态
     */
    @ApiOperation(value = "批量审核活动动态")
    @PutMapping("/batch/status")
    public void multiUpdateStatus(@PathVariable("activityId") String activityId,
                                  @RequestParam("memberId") String memberId,
                                  @RequestParam("status") AuditStatus status, @RequestBody List<String> feedIds) {

        long updated = newsFeedRepository.updateStatus(activityId, feedIds, status);

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        log.info("update {} news feed with status {} changed for activity {}", status, updated, activityId);

        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, null,
                activityId, System.currentTimeMillis(), String.format(BATCH_UPDATE_STATUS, status, feedIds.toString()));
    }

    /**
     * 审核单条动态
     *
     * @param activityId 活动ID
     * @param feedId     动态ID
     * @param status     审核状态
     */
    @ApiOperation(value = "审核单条动态")
    @PatchMapping("/{feedId}/status")
    public void updateStatus(@PathVariable("activityId") String activityId,
                             @RequestParam("memberId") String memberId,
                             @PathVariable("feedId") String feedId,
                             @RequestParam("status") AuditStatus status) {

        long updated = newsFeedRepository.updateStatus(activityId, Lists.newArrayList(feedId), status);

        log.info("update {} news feed with status {} changed for activity {}", status, updated, activityId);

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, null,
                activityId, System.currentTimeMillis(), String.format(UPDATE_STATUS, status, feedId));
    }

    /**
     * 增量获取审核通过的动态列表, 第一次请求倒序排列,其他正序排列
     *
     * @param activityId 活动ID
     * @param ts         上次请求的时间戳,第一次请求为-1
     * @param size       第一次请求的时候 最多请求多少条
     * @return 动态列表
     */
    @ApiOperation(value = "增量拉取发言数据, 投屏用", notes = "ts首次传-1")
    @GetMapping(value = "/incremental/{ts}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public IncrementalResult<ActivityNewsFeedRecordDO> incremental(@PathVariable("activityId") String activityId,
                                                                   @PathVariable("ts") long ts,
                                                                   @RequestParam(defaultValue = "100") int size) {

        Long currentTs = System.currentTimeMillis();
        List<ActivityNewsFeedRecordDO> records = newsFeedService.incrementalPull(activityId, ts, size);

        return new IncrementalResult<>(currentTs, records);
    }


}
