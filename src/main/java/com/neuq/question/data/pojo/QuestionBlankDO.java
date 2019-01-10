package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.QuestionBasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author yegk7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "question.blank")
@FieldNameConstants
public class QuestionBlankDO extends QuestionBasicDO {

    /**
     * 题库名称
     */
    private String name;

    /**
     * 空间id
     */
    private String qzId;

    /**
     * 题库描述
     */
    private String description;

    /**
     * 题库可用不可用属性
     */
    private Boolean enable;

}
