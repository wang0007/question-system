package com.neuq.question.service.events.activity;

import com.neuq.question.data.pojo.ConferenceActivityDO;
import lombok.Data;

/**
 * @author wangshyi
 */
@Data
public class ActivityCreateEvent {

    private final ConferenceActivityDO activity;

}
