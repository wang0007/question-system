package com.neuq.question.service;

import com.neuq.question.data.pojo.ExamResultDO;
import com.neuq.question.domain.enums.ExamType;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/11/15  10:19
 */

public interface ExamResultService {

    /**
     * 查询一个用户竞速模式下所用最短时间
     *
     * @param examId   考试id
     * @param memberId 用户id
     * @return 最短时间
     */
    Long queryUserMinTime(String examId, String memberId);

    /**
     * 竞速模式，查询该用户击败多少人
     *
     * @param examId    考试id
     * @param totalTime 该用户本次考试时间
     * @param memberId  用户信息
     * @param examType  考试类型
     * @return 击败人数
     */
    long querySpeedBeatNumber(String examId, Long totalTime, String memberId, ExamType examType);

    /**
     * 获取本次考试竞速模式下所有参与人员排行榜
     *
     * @param examId   考试id
     * @param examType 考试类型
     * @return 考试成绩列表
     */
    List<ExamResultDO> querySpeedSeniority(String examId, ExamType examType);

    /**
     * 得分模式下，查找当前用户做题最好成绩分数
     *
     * @param examId   考试id
     * @param memberId 用户id
     * @return 最好成绩
     */
    Integer queryBestScore(String examId, String memberId);

    /**
     * 查找得分模式下，该用户当前成绩击败的人数
     *
     * @param examId     考试id
     * @param totalScore 用户当前分数
     * @param memberId   用户id
     * @param examType   考试类型
     * @return 击败人数
     */
    long queryScoreBeatNumber(String examId, Integer totalScore, String memberId, ExamType examType);

    /**
     * 得分模式下，用户排行榜
     *
     * @param examId   考试id
     * @param examType 考试类型
     * @return 排行榜
     */
    List<ExamResultDO> queryScoreSeniority(String examId, ExamType examType);
}
