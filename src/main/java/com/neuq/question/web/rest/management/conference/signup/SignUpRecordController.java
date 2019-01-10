package com.neuq.question.web.rest.management.conference.signup;


import com.neuq.question.data.dao.SignUpRecordRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.service.SignUpRecordService;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报名记录相关接口
 *
 * @author liuhaoi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/signup/record")
@RequiredArgsConstructor
@Api(value = "报名记录相关接口", description = "报名记录相关接口")
public class SignUpRecordController {

    private final SignUpRecordRepository repository;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    private final SignUpRecordService recordService;


    /**
     * 获取大会的报名记录
     *
     * @param conferenceId   大会ID
     * @param auditStatuses  审核状态,为null表示不参与筛选
     * @param invitationCode 邀请码,为null表示不参与筛选
     * @param fieldName      字段名,为null表示不参与筛选
     * @param fieldValue     字段值,为null表示不参与筛选
     * @param start          分页起始条数
     * @param size           每页大小
     * @return 报名记录列表
     */
    @ApiOperation(value = "获取大会的报名记录", notes = "获取大会的报名记录")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<ConferenceSignUpRecordDO> list(@PathVariable("conferenceId") String conferenceId,
                                                     @RequestParam("auditStatuses[]") List<AuditStatus> auditStatuses,
                                                     @RequestParam(required = false) String invitationCode,
                                                     @RequestParam(required = false) String fieldName,
                                                     @RequestParam(required = false) String fieldValue,
                                                     @RequestParam(defaultValue = "0") Integer start,
                                                     @RequestParam(defaultValue = "20") Integer size) {

        List<ConferenceSignUpRecordDO> conferenceSignUpRecordDOList = repository
                .list(conferenceId, auditStatuses, invitationCode, fieldName, fieldValue, start, size);

        Long count = repository.count(conferenceId, auditStatuses, invitationCode, fieldName, fieldValue);
        return new PageResult<>(conferenceSignUpRecordDOList, count);
    }


    /**
     * 审核大会报名
     *
     * @param conferenceId 大会ID
     * @param statusDTO    手机号list 和审核状态
     * @return 更新后的报名记录
     */
    @ApiOperation(value = "审核大会报名", notes = "审核大会报名")
    @PatchMapping(value = "/audit/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ConferenceSignUpRecordDO> audit(@PathVariable("conferenceId") String conferenceId,
                                                @RequestParam("memberId") String memberId,
                                                @RequestBody SignUpAuditStatusDTO statusDTO) {

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        recordService.updateAuditRecord(conferenceId, statusDTO.getMobiles(), statusDTO.getStatus());

        List<ConferenceSignUpRecordDO> recordDOList = repository.queryByIds(conferenceId, statusDTO.getMobiles());

        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, recordDOList.get(0).getUtime(), "audit sign up status" + recordDOList.toString());

        return recordDOList;
    }

    /**
     * 审核报名状态
     */
    @Data
    public static class SignUpAuditStatusDTO {

        private AuditStatus status;

        /**
         * 手机号列表
         */
        private List<String> mobiles;
    }

}
