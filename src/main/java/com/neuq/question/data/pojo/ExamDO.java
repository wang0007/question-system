package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.QuestionBasicDO;
import com.neuq.question.domain.enums.ExamType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author wangshyi
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "exam")
@FieldNameConstants
public class ExamDO extends QuestionBasicDO {

    private String name;

    /**
     * 考试规则
     */
    private String examRegulation;

    /**
     * 题库ID
     */
    private String questionBlankId;

    /**
     * 单选题目数量
     */
    private Integer singleQuestionCount;

    /**
     * 多选题目数量
     */
    private Integer multipleQuestionCount;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 考试入口地址
     */
    private String examUrl;

    /**
     * 考试模式
     */
    private ExamType examType;

    /**
     * 竞速模式下时间间隔
     */
    private Long interval;

    /**
     * 考试题目是否有序,true为有序，false为无序
     */
    private Boolean order;

}
