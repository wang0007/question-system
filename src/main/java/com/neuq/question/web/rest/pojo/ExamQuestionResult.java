package com.neuq.question.web.rest.pojo;

import com.neuq.question.domain.enums.QuestionType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author wangshyi
 * @date 2018/12/11  19:41
 */
@Data
public class ExamQuestionResult {

    /**
     * 题目Id
     */
    private String questionId;

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
     * 分数
     */
    private Integer score;

    /**
     * 考试场次
     */
    private String sequence;

    /**
     * 用户的选择，浏览上一题时会存在
     */
    private List<String> userChoose;
}
