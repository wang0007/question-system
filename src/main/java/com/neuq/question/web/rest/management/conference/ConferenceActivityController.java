package com.neuq.question.web.rest.management.conference;


import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.error.PrincipalActivityExistException;
import com.neuq.question.service.ConferenceActivityService;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 大会活动接口
 *
 * @author wangshyi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/activity/")
@RequiredArgsConstructor
@Api(value = "大会活动接口", description = "大会活动接口")
public class ConferenceActivityController {

    private final ConferenceActivityRepository conferenceActivityRepository;

    private final ConferenceActivityService activityService;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    /**
     * 获取大会的活动列表,不分页
     *
     * @param conferenceId 大会ID
     * @return 活动列表
     */
    @ApiOperation(value = "获取大会的活动列表,不分页", notes = "获取大会的活动列表,不分页")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<ConferenceActivityDO> list(@PathVariable("conferenceId") String conferenceId,
                                                 @RequestParam(defaultValue = "0") int start,
                                                 @RequestParam(defaultValue = "10000") int size) {

        List<ConferenceActivityDO> data = conferenceActivityRepository.list(conferenceId, start, size);

        data = data.stream().sorted(Comparator.comparing(ConferenceActivityDO::getActivityDate)
                .thenComparing(ConferenceActivityDO::getStartTime).thenComparing(ConferenceActivityDO::getCtime))
                .collect(Collectors.toList());

        long count = conferenceActivityRepository.count(conferenceId);

        return new PageResult<>(data, count);
    }

    /**
     * 获取活动详情
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     * @return 活动详情
     */
    @ApiOperation(value = "获取活动详情", notes = "获取活动详情")
    @GetMapping(value = "{activityId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceActivityVO getById(@PathVariable("conferenceId") String conferenceId,
                                        @PathVariable("activityId") String activityId) {

        ConferenceActivityDO activityDO = conferenceActivityRepository.query(conferenceId, activityId);

        ConferenceActivityVO conferenceActivityVO = new ConferenceActivityVO();
        BeanUtils.copyProperties(activityDO, conferenceActivityVO);

        ConferenceActivityDO activity = conferenceActivityRepository.queryPrincipalActivity(conferenceId);
        conferenceActivityVO.setPrincipalExist(true);
        if (activity == null) {
            conferenceActivityVO.setPrincipalExist(false);
        }

        return conferenceActivityVO;
    }

    /**
     * 创建大会活动
     *
     * @param conferenceId 大会ID
     * @param activity     活动ID
     * @return 大会活动
     */
    @ApiOperation(value = "创建大会活动", notes = "创建大会活动")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceActivityDO create(@PathVariable("conferenceId") String conferenceId,
                                       @RequestBody ConferenceActivityDTO activity) {

        return activityService.createActivity(conferenceId, activity);
    }

    /**
     * 更新大会活动信息
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     * @param activity     大会活动
     * @return 大会活动
     */
    @ApiOperation(value = "更新大会活动信息", notes = "更新大会活动信息")
    @PutMapping(value = "{activityId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceActivityDO update(@PathVariable("conferenceId") String conferenceId,
                                       @PathVariable("activityId") String activityId,
                                       @RequestParam("memberId") String memberId,
                                       @RequestBody ConferenceActivityDTO activity) {

        activityService.findAndVerifyActivity(activityId);

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        conferenceActivityRepository.update(conferenceId, activityId, activity);

        ConferenceActivityDO conferenceActivityDO = conferenceActivityRepository.query(conferenceId, activityId);

        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                activityId, conferenceActivityDO.getUtime(), "update activity" + activity);

        return conferenceActivityDO;
    }

    /**
     * 更新大会主活动
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     * @param principal    是否为主活动
     */
    @ApiOperation(value = "更新大会主活动", notes = "更新大会主活动")
    @PutMapping(value = "{activityId}/{principal}")
    public void updatePrincipal(@PathVariable("conferenceId") String conferenceId,
                                @PathVariable("activityId") String activityId,
                                @RequestParam("memberId") String memberId,
                                @PathVariable("principal") boolean principal) {

        ConferenceActivityDO activityDO = conferenceActivityRepository.queryPrincipalActivity(conferenceId);
        if (activityDO != null && principal) {
            throw new PrincipalActivityExistException("principal activity has exist");
        }

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        conferenceActivityRepository.updatePrincipal(conferenceId, activityId, principal);

        ConferenceActivityDO conferenceActivityDO = conferenceActivityRepository.query(conferenceId, activityId);

        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                activityId, conferenceActivityDO.getUtime(), "update activity principal");
    }

    /**
     * 删除活动
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     */
    @ApiOperation(value = "删除活动", notes = "删除活动")
    @DeleteMapping("{activityId}")
    public void delete(@PathVariable("conferenceId") String conferenceId,
                       @RequestParam("memberId") String memberId,
                       @PathVariable("activityId") String activityId) {

        conferenceActivityRepository.delete(conferenceId, activityId);

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        operationLogService.createOperationLog(inAPIUser, OperationType.DELETE, conferenceId,
                activityId, System.currentTimeMillis(), "delete activity");

    }

    @Data
    @Validated
    public static class ConferenceActivityDTO {

        @Valid
        @Size(max = 50, min = 1)
        private String name;

        @Valid
        @NotNull
        private Date activityDate;

        @Valid
        @NotNull
        private Date startTime;

        @Valid
        @NotNull
        private Date endTime;

        private Boolean principal;

    }

    @Data
    public static class ConferenceActivityVO {
        protected String id;

        protected String conferenceId;

        private String qzId;

        private String name;

        /**
         * 是否为主会场活动
         */
        private Boolean principal;

        private Date activityDate;

        private Date startTime;

        private Date endTime;

        /**
         * 是否存在主会场
         */
        private Boolean principalExist;
    }
}
