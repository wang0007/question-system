package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.QuestionBasicDO;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author wangshyi
 * @date 2019/1/12  16:06
 */
@Document(collection = "rediskv")
@Data
@FieldNameConstants
public class RedisDo extends QuestionBasicDO {

    private String key;

    private String value;

}
