package com.neuq.question.service.events.conference;


import com.neuq.question.data.pojo.ConferenceDO;
import lombok.Data;

/**
 * @author liuhaoi
 */
@Data
public class ConferenceCreateEvent {

    private final ConferenceDO conference;

}
