package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.QuestionDO;

import java.util.List;

/**
 * @author yegk7
 */
public interface QuestionRepository {

    /**
     * 添加题目
     *
     * @param questionDO 题目
     */
    void save(QuestionDO questionDO);

    /**
     * 修改题目
     *
     * @param questionDO 题目
     * @param questionId 题目id
     * @return 修改结果
     */
    long update(QuestionDO questionDO, String questionId);

    /**
     * 根据id查询相应题目
     *
     * @param id              题目id
     * @param questionBlankId 题库id
     * @return 题目信息
     */
    QuestionDO queryById(String id, String questionBlankId);

    /**
     * 根据题库id，问题id查询题目正确选项
     *
     * @param id              题目id
     * @param questionBlankId 题库id
     * @return 答案列表
     */
    List<String> queryAnswerById(String id, String questionBlankId);

    /**
     * 根据题目id删除相应题目
     *
     * @param questionId      题目id
     * @param questionBlankId 题库id
     */
    void delete(String questionId, String questionBlankId);

    /**
     * 获取题库中所有题目数量
     *
     * @param questionBlankId 题库id
     * @return 题目总数
     */
    long queryQuestionCount(String questionBlankId);

    /**
     * 根据题库id获取题库下所有题目
     *
     * @param questionBlankId 题库id
     * @param keyword         关键字
     * @param start           起始页
     * @param size            每页条数
     * @return 所有题目列表
     */
    List<QuestionDO> queryQuestionsList(String questionBlankId, String keyword, int start, int size);

    /**
     * 查询所有单选题
     *
     * @param questionBlankId 题库id
     * @return 所有单选题列表
     */
    List<QuestionDO> querySingleQuestionList(String questionBlankId);

    /**
     * 查询所有多选题列表
     *
     * @param questionBlankId 题库id
     * @return 所有多选题列表
     */
    List<QuestionDO> queryMultipleQuestionList(String questionBlankId);

    /**
     * 根据题目id修改基础信息
     *
     * @param questionDO 题目基础信息
     * @param questionId 题目id
     * @return 修改数量
     */
    long updateBase(QuestionDO questionDO, String questionId);


}
