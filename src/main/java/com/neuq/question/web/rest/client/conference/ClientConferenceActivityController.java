package com.neuq.question.web.rest.client.conference;


import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.pojo.ActivitySignInSettingDO;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.web.rest.pojo.ListResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 */
@Api(value = "移动端-大会活动信息接口", description = "移动端-大会活动信息接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/v1/client/conference/{conferenceId}/activity")
public class ClientConferenceActivityController {

    private final ActivitySignInSettingRepository signInSettingRepository;

    private final ConferenceActivityRepository activityRepository;

    @ApiOperation("需要签到的活动列表")
    @GetMapping("/signin/required")
    public ListResult<ConferenceActivityDO> listNeedSignInActivity(@PathVariable String conferenceId) {

        List<ActivitySignInSettingDO> setting = signInSettingRepository.queryByConferenceId(conferenceId, true);

        List<ConferenceActivityDO> collect = setting.stream()
                .map(item -> activityRepository.query(item.getActivityId()))
                .collect(Collectors.toList());

        return new ListResult<>(collect);
    }

}
