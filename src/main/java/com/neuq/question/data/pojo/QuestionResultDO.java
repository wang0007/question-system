package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.QuestionBasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author wangshyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "question.result")
@FieldNameConstants
public class QuestionResultDO extends QuestionBasicDO {

    public static final String QUESTION_RESULT_MEMBER_ID = "user.memberId";

    private InAPIUser user;

    /**
     * 用户选择
     */
    private List<String> choose;

    /**
     * 正确答案
     */
    private List<String> rightAnswer;

    /**
     * 用户得分
     */
    private Integer score;

    /**
     * 答题耗时
     */
    private Long costTime;

    /**
     * 题目ID
     */
    private String questionId;

    /**
     * 考试场次
     */
    private String sequence;

}
