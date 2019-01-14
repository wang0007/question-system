package com.neuq.question.web.rest.client.conference;


import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.error.ECResourceNotFoundException;
import com.neuq.question.service.ConferenceService;
import com.neuq.question.web.rest.pojo.ListResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 */
@Api(value = "移动端-大会信息接口", description = "移动端-大会信息接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/v1/client/conference")
public class ClientConferenceController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    private final ConferenceRepository repository;

    private final ConferenceService conferenceService;

    private final ConferenceActivityRepository activityRepository;

    private final UserRepsitory userRepsitory;


    @ApiOperation(value = "获取大会信息")
    @GetMapping("/{conferenceId}")
    public ConferenceDO getConferenceInfo(@PathVariable String conferenceId) {

        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.SECOND);
        calendar.get(Calendar.MINUTE);
        calendar.get(Calendar.HOUR_OF_DAY);

        return repository.queryByConferenceId(conferenceId);
    }

    @ApiOperation(value = "获取所有大会列表")
    @GetMapping("/active")
    public ListResult<ConferenceDO> loadActiveConference(@RequestParam("memberId") String memberId) {

        InAPIUser user = userRepsitory.queryById(memberId);

        List<ConferenceDO> list = repository.queryAllConference();

        List<ConferenceDO> sortList = conferenceService.sortConferenceList(list);

        return new ListResult<>(sortList);

    }

    @ApiOperation(value = "获大会的活动日期列表")
    @GetMapping("/{conferenceId}/days")
    public ConferenceDays getConferenceDays(@PathVariable String conferenceId) {

        ConferenceDO conferenceDO = repository.queryByConferenceId(conferenceId);

        if (conferenceDO == null) {
            throw new ECResourceNotFoundException("Conference not exists");
        }

        List<ConferenceActivityDO> activityList = activityRepository.list(conferenceId, 0, 1000);

        Map<String, List<ConferenceActivityDO>> dateListMap = new HashMap<>();
        for (ConferenceActivityDO activity : activityList) {
            String dateStr = new DateTime(activity.getActivityDate()).toString(DATE_FORMATTER);
            dateListMap.computeIfAbsent(dateStr, k -> new ArrayList<>()).add(activity);
        }

        ConferenceDays result = new ConferenceDays();
        result.setName(conferenceDO.getName());
        result.setStartDate(conferenceDO.getStartTime().getTime());
        result.setEndDate(conferenceDO.getEndTime().getTime());

        ArrayList<ConferenceDay> days = new ArrayList<>();
        result.setDays(days);

        dateListMap.forEach((k, v) -> days.add(new ConferenceDay(k, v.size())));
        return result;
    }

    @ApiOperation(value = "获大会的指定日期的活动列表")
    @GetMapping("/{conferenceId}/days/{day}/activities")
    public ConferenceActivities getActivitiesByDay(@PathVariable String conferenceId, @PathVariable String day) {

        ConferenceDO conferenceDO = repository.queryByConferenceId(conferenceId);

        if (conferenceDO == null) {
            throw new ECResourceNotFoundException("Conference not exists");
        }

        List<ConferenceActivityDO> activityList = activityRepository.list(conferenceId, 0, 1000);

        Map<String, List<ConferenceActivityDO>> dateListMap = new HashMap<>();
        for (ConferenceActivityDO activity : activityList) {
            String dateStr = new DateTime(activity.getActivityDate()).toString(DATE_FORMATTER);
            dateListMap.computeIfAbsent(dateStr, k -> new ArrayList<>()).add(activity);
        }

        ConferenceActivities result = new ConferenceActivities();
        result.setName(conferenceDO.getName());
        result.setStartDate(conferenceDO.getStartTime().getTime());
        result.setEndDate(conferenceDO.getEndTime().getTime());

        List<ConferenceActivityDO> activities = dateListMap.get(day);

        activities = (activities == null ? Collections.emptyList() : activities);

        activities = activities.stream().sorted(Comparator.comparing(ConferenceActivityDO::getActivityDate)
                .thenComparing(ConferenceActivityDO::getStartTime).thenComparing(ConferenceActivityDO::getCtime))
                .collect(Collectors.toList());

        result.setActivities(activities);

        return result;
    }

    @ApiOperation(value = "获取大会的主活动", notes = "获取大会的主活动,若没有主活动，获取第一个创建活动")
    @GetMapping("/{conferenceId}/primary/activity")
    public ConferenceActivityDO getPrimaryActivityId(@PathVariable("conferenceId") String conferenceId) {

        long count = activityRepository.count(conferenceId);

        if (count == 0) {
            return new ConferenceActivityDO();
        }

        ConferenceActivityDO activityDO = activityRepository.queryPrincipalActivity(conferenceId);

        // 若存在主活动，则展示主活动未签到列表，否则展示第一个创建的活动未签到列表
        if (activityDO == null) {
            List<ConferenceActivityDO> list = activityRepository.list(conferenceId, 0, Math.toIntExact(count));
            activityDO = list.get(0);
        }

        return activityDO;
    }

    @Data
    public static class ConferenceActivities {

        private String name;

        private Long startDate;

        private Long endDate;

        private List<ConferenceActivityDO> activities;
    }

    @Data
    public static class ConferenceDays {

        private String name;

        private Long startDate;

        private Long endDate;

        private List<ConferenceDay> days;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConferenceDay {

        private String day;

        private Integer activities;

    }


}
