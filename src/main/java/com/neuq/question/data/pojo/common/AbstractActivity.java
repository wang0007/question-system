package com.neuq.question.data.pojo.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author liuhaoi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class AbstractActivity extends BasicDO {

    public static final String FIELD_ACTIVITY_ID = "activityId";

    @Indexed
    protected String activityId;

}
