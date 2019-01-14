package com.neuq.question.data.pojo.common;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author wangshyi
 */
@Data
public class BasicDO {

    public static final String DEFAULT_ID = "__default::";

    public static final String FIELD_ID = "_id";
    public static final String FIELD_CTIME = "ctime";
    public static final String FIELD_UTIME = "utime";
    public static final String FIELD_CONFERENCE_ID = "conferenceId";

    @Id
    protected String id;

    protected String conferenceId;

    protected Long ctime;

    protected Long utime;

}
