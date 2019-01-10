package com.neuq.question.web.rest.pojo;

import com.neuq.question.data.pojo.InAPIUser;
import lombok.Data;


/**
 * @author wangshyi
 * @date 2018/12/13  19:01
 */
@Data
public class SpeedExamResult {

    public static final String EXAM_RESULT_MEMBER_ID="user.memberId";

    private InAPIUser user;

    /**
     * 考试id
     */
    private String examId;

    /**
     * 该用户答题所用最短时间
     */
    private Long minTime;

    /**
     * 击败人数
     */
    private long beatNumber;
}
