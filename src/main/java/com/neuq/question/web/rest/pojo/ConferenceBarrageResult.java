package com.neuq.question.web.rest.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangshyi
 * @date 2018/10/25 17:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConferenceBarrageResult<T> extends IncrementalResult<T> {

    private Integer barrageSpeed;

    /**
     * 多少毫秒允许进入一个弹幕
     */
    private Integer enterFrequency;

    /**
     * 是否清空弹幕
     */
    private Boolean clearBarrage;

}
