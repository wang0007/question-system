package com.neuq.question.service.events.activity;

/**
 * 大会活动创建事件
 *
 * @author wangshyi
 */
public interface ActivityCreateEventListener {

    /**
     * 大会活动创建事件
     *
     * @param event 活动
     */
    void onCreate(ActivityCreateEvent event);

}
