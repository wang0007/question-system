package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.QuestionResultDO;

/**
 * @author yegk7
 */
public interface QuestionResultRepository {

    /**
     * 通过问题id和成员id查询题目结果
     *
     * @param questionId 题目id
     * @param memberId   答题人id
     * @param sequence   考试场次
     * @return 题目结果信息
     */
    QuestionResultDO queryResultById(String questionId, String memberId, String sequence);

    /**
     * 保存答题后每道题的具体结果信息
     *
     * @param questionResultDO 答题结果信息
     */
    void saveResult(QuestionResultDO questionResultDO);

    /**
     * 更新用户的所更改的答案
     *
     * @param questionResultDO 问题答案信息
     * @return 更新的数量
     */
    long updateResult(QuestionResultDO questionResultDO);


}
