package com.neuq.question.web.rest.pojo;

import com.neuq.question.data.pojo.InAPIUser;
import lombok.Data;

/**
 * @author wangshyi
 * @date 2018/12/20  19:59
 */
@Data
public class ScoreExamResult {

    private InAPIUser user;

    /**
     * 考试id
     */
    private String examId;

    /**
     * 该用户答题所用最短时间
     */
    private Integer bestScore;

    /**
     * 击败人数
     */
    private long beatNumber;
}
