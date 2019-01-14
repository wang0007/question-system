package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 活动通用属性
 *
 * @author wangshyi
 */
@Document(collection = "activity")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
public class ConferenceActivityDO extends BasicDO {

    private String qzId;

    private String name;

    /**
     * 是否为主会场活动
     */
    private Boolean principal;

    private Date activityDate;

    private Date startTime;

    private Date endTime;

}
