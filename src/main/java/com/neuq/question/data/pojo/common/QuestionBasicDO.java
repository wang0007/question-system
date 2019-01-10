package com.neuq.question.data.pojo.common;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;

/**
 * @author yegk7
 */
@Data
@FieldNameConstants
public class QuestionBasicDO {

    @Id
    protected String id;

    protected Long ctime;

    protected Long utime;

}
