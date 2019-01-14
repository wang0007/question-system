package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.ExamResultDO;

import java.util.List;

/**
 * @author wangshyi
 */
public interface ExamResultRepository {

    /**
     * 通过考试id获取考试结果信息
     *
     * @param examId   考试id
     * @param memberId 考试用户id
     * @param sequence 考试场次
     * @return 考试结果信息
     */
    ExamResultDO queryExamResult(String examId, String memberId, String sequence);

    /**
     * 保存考试信息结果
     *
     * @param examResultDO 考试信息
     */
    void saveExamResult(ExamResultDO examResultDO);

    /**
     * 更新本次考试的结果信息
     *
     * @param examResultDO 考试结果信息
     * @return 更新的条数
     */
    long updateExamResult(ExamResultDO examResultDO);

    /**
     * 删除考试结果信息
     *
     * @param examId   考试id
     * @param memberId 用户id
     * @param sequence 考试场次
     */
    void deleteExamResult(String examId, String memberId, String sequence);

    /**
     * 查询用户相同考试的所有结果列表
     * @param examId 考试id
     * @param memberId 用户id
     * @return 考试的所有结果列表
     */
    List<ExamResultDO> queryExamResultList(String examId, String memberId);

    /**
     * 查询所有完成考试人员信息
     * @param examId 考试id
     * @return 所有结果信息列表
     */
    List<ExamResultDO> queryAllResult(String examId);
}
