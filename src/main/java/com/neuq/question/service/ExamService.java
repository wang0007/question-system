package com.neuq.question.service;

import com.neuq.question.data.pojo.ExamDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.data.pojo.QuestionDO;
import com.neuq.question.web.rest.pojo.ExamQuestionResult;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/11/19  11:03
 */
public interface ExamService {

    /**
     * 开始开始，获取考试题目，导入redis中
     *
     * @param examDO   考试相关属性信息
     * @param user     答题人详细信息
     * @param examId   考试id
     * @param sequence 本场考试的序号
     * @return 考试第一题
     */
    QuestionDO getExamQuestions(ExamDO examDO, InAPIUser user, String examId, String sequence);

    /**
     * 根据请求的下标判断之后的操作，下一题还是之前的题
     *
     * @param examId        考试id
     * @param user          用户信息
     * @param questionIndex 请求的下标
     * @param userAnswers   用户选择的答案
     * @param questionId    当前问题的id
     * @param examOrder     考试场次
     * @return 请求的下一题题目信息
     */
    ExamQuestionResult getNextQuestion(String examId, InAPIUser user, Integer questionIndex, List<String> userAnswers, String questionId, String examOrder);

    /**
     * 得分模式，结束当前考试
     *
     * @param examId      考试id
     * @param questionId  最后一题的题目id
     * @param memberId    考试人员信息
     * @param userAnswers 用户的选择
     * @param totalTime   前端传来的考试耗时
     * @param sequence    考试场次
     */
    void endExam(String examId, String questionId, String memberId, List<String> userAnswers, Long totalTime, String sequence);

    /**
     * 竞速模式下，获取下一题或者上一题
     *
     * @param examId        考试id
     * @param user          人员信息
     * @param questionIndex 请求的题目索引
     * @param userAnswers   用户选择
     * @param questionId    问题id
     * @param sequence      考试场次
     * @return 请求的问题信息
     */
    QuestionDO getNextSpeedQuestion(String examId, InAPIUser user, Integer questionIndex, List<String> userAnswers, String questionId, String sequence);

    /**
     * 竞速模式下的考试结束
     *
     * @param examId      本场考试id
     * @param questionId  问题id
     * @param memberId    用户信息
     * @param userAnswers 用户的选择
     * @param totalTime   前端传入的时间
     * @param sequence    考试场次
     */
    void endSpeedExam(String examId, String questionId, String memberId, List<String> userAnswers, Long totalTime, String sequence);

    /**
     * 判断用户答案
     *
     * @param userAnswers 用户选择
     * @param questionId  当前题目id
     * @param examId      考试id
     * @return 正确true，错误false
     */
    boolean judgePresentResult(List<String> userAnswers, String questionId, String examId);

    /**
     * 排序考试列表
     *
     * @param examDOList 考试列表
     * @return 考试列表
     */
    List<ExamDO> sortExamList(List<ExamDO> examDOList);
}
