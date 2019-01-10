package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.QuestionBasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author yegk7
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "exam.result")
@FieldNameConstants
public class ExamResultDO extends QuestionBasicDO {

    public static final String EXAM_RESULT_MEMBER_ID = "user.memberId";

    private InAPIUser user;

    /**
     * 考试id
     */
    private String examId;

    /**
     * 考试总分
     */
    private Integer totalScore;

    /**
     * 考试总耗时
     */
    private Long totalTime;

    /**
     * 题目id列表
     */
    private List<String> questionIds;

    /**
     * 考试场次,用UUID生成，保证唯一
     */
    private String sequence;
}
