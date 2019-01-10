package com.neuq.question.web.rest.client.conference;


import com.neuq.question.data.dao.ConferenceGuideRepository;
import com.neuq.question.data.pojo.ConferenceGuideDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuhaoi
 */
@Api(value = "移动端-大会信息接口", description = "移动端-大会信息接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/v1/client/conference/{conferenceId}/guide")
public class ClientConferenceGuideController {


    private final ConferenceGuideRepository guideRepository;

    @ApiOperation(value = "获取大会指南列表")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceGuides list(@PathVariable("conferenceId") String conferenceId) {

        List<ConferenceGuideDO> guideList = guideRepository.list(conferenceId, 0, 1000);

        ConferenceGuides guides = new ConferenceGuides();
        guides.setConferenceId(conferenceId);
        guides.setGuides(guideList);

        return guides;

    }

    @Data
    public static class ConferenceGuides {

        private String conferenceId;

        private List<ConferenceGuideDO> guides;

    }


}
