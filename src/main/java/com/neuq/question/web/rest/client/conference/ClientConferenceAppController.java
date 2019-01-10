package com.neuq.question.web.rest.client.conference;


import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceAppDO;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.service.ConferenceAppService;
import com.neuq.question.service.ConferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liuhaoi
 */
@Api(value = "移动端-大会应用列表接口", description = "移动端-大会应用列表接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/v1/client/conference/{conferenceId}/app")
public class ClientConferenceAppController {

    private final ConferenceService conferenceService;

    private final ConferenceAppService appService;

    private final UserRepsitory userRepsitory;

    private static final String HELP_SIGN_APP_ID = "_signin_proxy";

    @ApiOperation("获取大会应用列表")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceAppDO queryByConference(@RequestParam("memberId") String memberId,
                                             @PathVariable String conferenceId) {

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        ConferenceDO conference = conferenceService.findAndVerifyConference(conferenceId);

        ConferenceAppDO conferenceAppDO = appService.queryConferenceAppWithDefault(conferenceId);


        List<ConferenceAppDO.App> apps = conferenceAppDO.getApps();

        if (apps == null) {
            conferenceAppDO.setApps(Collections.emptyList());
            return conferenceAppDO;
        }

        Set<String> helpSignMembers = conference.getHelpSignInRoles();
        List<ConferenceAppDO.App> collect = apps.stream().filter(app -> {
            Boolean enable = app.getEnable();
            if (helpSignMembers != null && app.getAppId().equals(HELP_SIGN_APP_ID) &&
                    !helpSignMembers.contains(inAPIUser.getMemberId())) {
                enable = false;
            }
            return enable != null && enable;
        }).collect(Collectors.toList());

        conferenceAppDO.setApps(collect);

        return conferenceAppDO;

    }

}
