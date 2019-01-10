package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.QuestionBasicDO;
import com.neuq.question.domain.enums.QuestionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author yegk7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "question")
@FieldNameConstants
public class QuestionDO extends QuestionBasicDO implements Serializable {

    /**
     * 题库Id
     */
    private String questionBlankId;

    /**
     * 题目类型
     */
    private QuestionType questionType;

    /**
     * 题目名称
     */
    private String name;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 题目的各个选项内容，key为选项id，value为选项内容，通过key唯一确定value
     */
    private Map<String, String> question;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 答案，存答案对应的ID，存list便于多选查询
     */
    private List<String> answer;
}
