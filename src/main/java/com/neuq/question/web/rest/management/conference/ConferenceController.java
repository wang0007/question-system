package com.neuq.question.web.rest.management.conference;


import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.dao.SignUpFormRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.ConferenceService;
import com.neuq.question.service.InAPIUserService;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * 大会接口
 *
 * @author wangshyi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/")
@RequiredArgsConstructor
@Api(value = "大会接口", description = "大会接口")
public class ConferenceController {


    private final ConferenceRepository conferenceRepository;

    private final ConferenceService conferenceService;

    private final UserRepsitory userRepsitory;

    private final InAPIUserService inAPIUserService;

    private final OperationLogService operationLogService;

    private final SignUpFormRepository formRepository;

    /**
     * 获取大会列表,
     *
     * @param endTimeAfter 条件:结束时间在指定时间之后
     * @return 大会列表
     */
    @ApiOperation(value = "获取大会列表")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<ConferenceDO> list(@RequestParam(required = false) Date endTimeAfter,
                                         @RequestParam(defaultValue = "0") int start,
                                         @RequestParam(defaultValue = "20") int end) {

        long count = conferenceRepository.count(endTimeAfter);

        if (start > count) {
            return new PageResult<>(Collections.emptyList(), count);
        }

        List<ConferenceDO> data = conferenceRepository.list(endTimeAfter, start, end);
        data = conferenceService.sortConferenceList(data);

        return new PageResult<>(data, count);
    }


    /**
     * 获取大会详情
     *
     * @param conferenceId 大会ID
     * @return 大会详情
     */
    @ApiOperation(value = "获取大会详情", notes = "获取大会详情")
    @GetMapping(value = "{conferenceId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceVo getById(@PathVariable("conferenceId") String conferenceId) {
        return getConferenceById(conferenceId);
    }

    /**
     * 获取某人负责的大会
     *
     * @param isConferenceAdmin 是否为大会管理员
     * @return 大会详情
     */
    @ApiOperation(value = "获取某人负责的大会", notes = "获取某人负责的大会")
    @GetMapping(value = "/personalConference", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConferenceDO> getByMemberId(@RequestParam("memberId") String memberId,
                                            @RequestParam boolean isConferenceAdmin) {
        InAPIUser user = userRepsitory.queryById(memberId);

        if (isConferenceAdmin) {
            return conferenceRepository.queryByMemberId(user.getMemberId());
        }
        return null;
    }

    /**
     * 创建大会
     *
     * @param conference 大会
     * @return 创建的大会
     */
    @ApiOperation(value = "创建大会", notes = "创建大会")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceDO create(@RequestParam("memberId") String memberId,
                               @RequestBody ConferenceDTO conference) {


        return conferenceService.createConference(memberId, conference);
    }

    /**
     * 更新大会
     *
     * @param conferenceId 大会ID
     * @param conference   大会
     * @return 更新后的大会
     */
    @ApiOperation(value = "更新大会", notes = "更新大会")
    @PutMapping(value = "{conferenceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceVo update(@PathVariable String conferenceId,
                               @RequestParam("memberId") String memberId,
                               @RequestBody ConferenceDTO conference) {

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        long endTime = DateUtils.addDays(conference.getEndTime(), 1).getTime() - 1;
        conference.setEndTime(new Date(endTime));
        conferenceRepository.update(conferenceId, conference);

        // 更新大会名称同时更新报名表名称
        formRepository.updateFormTitle(conferenceId, conference.getName());
        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, System.currentTimeMillis(), "update conference" + conference);

        return getConferenceById(conferenceId);
    }


    /**
     * 删除大会, 此处进行禁用操作,并不进行真正的删除
     *
     * @param conferenceId 大会ID
     */
    @ApiOperation(value = "删除大会, 此处进行禁用操作,并不进行真正的删除", notes = "删除大会, 此处进行禁用操作,并不进行真正的删除")
    @DeleteMapping("{conferenceId}")
    public void disable(@PathVariable String conferenceId,
                        @RequestParam("memberId") String memberId) {

        conferenceRepository.disable(conferenceId);

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        operationLogService.createOperationLog(inAPIUser, OperationType.DELETE, conferenceId,
                null, System.currentTimeMillis(), "delete conference");
    }


    /**
     * 根据大会id获取大会详情
     *
     * @param conferenceId 大会ID
     * @return 大会vo
     */
    private ConferenceVo getConferenceById(String conferenceId) {
        ConferenceDO conferenceDO = conferenceRepository.queryByConferenceId(conferenceId);
        ConferenceVo conferenceVo = new ConferenceVo();
        if (conferenceDO != null) {

            if (conferenceDO.getHelpSignInRoles() != null) {
                List<String> helpSignInList = new ArrayList<>(conferenceDO.getHelpSignInRoles());
                Map<String, InAPIUser> helpSignInMap = inAPIUserService.getUsersByMemberIds(helpSignInList);
                conferenceVo.setHelpSignInMembers(new ArrayList<>(helpSignInMap.values()));
            }
            conferenceVo.setImage(conferenceDO.getImage());
            conferenceVo.setThumbImage(conferenceDO.getThumbImage());
            conferenceVo.setName(conferenceDO.getName());
            conferenceVo.setStartTime(conferenceDO.getStartTime());
            conferenceVo.setEndTime(conferenceDO.getEndTime());
            conferenceVo.setTopic(conferenceDO.getTopic());
            conferenceVo.setConferenceId(conferenceDO.getConferenceId());
        }

        return conferenceVo;
    }

    @Data
    @Validated
    public static class ConferenceDTO {

        @Size(max = 50, min = 1)
        @Valid
        private String name;

        @Valid
        @NotNull
        private Date startTime;

        @Valid
        @NotNull
        private Date endTime;

        private String topic;

        private String image;

        private String thumbImage;


        /**
         * 帮他签到角色
         */
        private Set<String> helpSignInRoles;

    }

    @Data
    public static class ConferenceVo {

        private String conferenceId;

        private String name;

        private String qzId;

        private Date startTime;

        private Date endTime;

        private String topic;

        private String image;

        private String thumbImage;

        private Set<String> adminMemberIds;

        private List<InAPIUser> helpSignInMembers;

    }


}
