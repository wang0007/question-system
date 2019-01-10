package com.neuq.question.web.rest.client.conference.agenda;



import com.neuq.question.data.dao.ActivityAgendaRepository;
import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.pojo.ActivityAgendaDO;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.domain.enums.LifeCycleStatus;
import com.neuq.question.support.bean.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuhaoi
 */
@Api(value = "移动端-议程信息接口", description = "移动端-议程信息接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/v1/client/{activityId}/agenda")
public class ClientAgendaController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");


    private final ActivityAgendaRepository agendaRepository;

    private final ConferenceActivityRepository activityRepository;

    @ApiOperation(value = "获取活动议程信息")
    @GetMapping("")
    public ActivityAgendaDTO getAgendas(@PathVariable String activityId) {

        ActivityAgendaDO activityAgendaDO = agendaRepository.queryById(activityId);


        ActivityAgendaDTO agenda = new ActivityAgendaDTO();
        agenda.setActivityId(activityId);
        agenda.setWithAmPmTitle(activityAgendaDO.getWithAmPmTitle());

        List<ActivityAgendaDO.Agenda> agendas = activityAgendaDO.getAgendas();

        if (agendas != null) {

            ConferenceActivityDO activity = activityRepository.query(activityId);
            Date activityDate = activity.getActivityDate();

            List<AgendaDTO> agendaDtos = agendas.stream().map(item -> {
                AgendaDTO agendaDTO = new AgendaDTO();
                BeanUtils.copyPropertiesIgnoresNull(item, agendaDTO);
                LifeCycleStatus lifeCycleStatus = calculateStatus(activityDate, agendaDTO.startTime, agendaDTO.endTime);
                agendaDTO.setStatus(lifeCycleStatus);
                agendaDTO.setActivityDate(activityDate);
                return agendaDTO;
            }).collect(Collectors.toList());
            agenda.setAgendas(agendaDtos);

            List<AgendaDTO> collect = agendaDtos.stream()
                    .sorted(Comparator.comparing(AgendaDTO::getStartTime).thenComparing(AgendaDTO::getEndTime))
                    .collect(Collectors.toList());
            agenda.setAgendas(collect);
        }

        return agenda;
    }

    private LifeCycleStatus calculateStatus(Date date, String startHourMinutes, String endHourMinutes) {

        String dateStr = new DateTime(date).toString(DATE_FORMATTER);
        String startDateTimeStr = dateStr + " " + startHourMinutes + ":00";
        DateTime startDateTime = DATE_TIME_FORMATTER.parseDateTime(startDateTimeStr);
        long startMillis = startDateTime.getMillis();

        String endDateTimeStr = dateStr + " " + endHourMinutes + ":00";
        DateTime endDateTime = DATE_TIME_FORMATTER.parseDateTime(endDateTimeStr);
        long endMillis = endDateTime.getMillis();

        long ts = System.currentTimeMillis();
        if (ts < startMillis) {
            return LifeCycleStatus.FUTURE;
        }

        if (ts > endMillis) {
            return LifeCycleStatus.PASSED;
        }

        return LifeCycleStatus.ON_GOING;


    }


    @Data
    public static class ActivityAgendaDTO {

        private String activityId;

        private Boolean withAmPmTitle;

        private List<AgendaDTO> agendas;


    }

    @Data
    public static class AgendaDTO {

        private Date activityDate;

        private String agendaId;

        private String title;

        private String subTitle;

        private String startTime;

        private String endTime;

        private LifeCycleStatus status;
    }


}
