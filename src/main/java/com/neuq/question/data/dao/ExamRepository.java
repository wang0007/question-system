package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.ExamDO;

import java.util.List;

/**
 * @author yegk7
 */
public interface ExamRepository {

    /**
     * 通过考试Id查询考试信息
     *
     * @param examId 考试id
     * @return 考试信息
     */
    ExamDO queryById(String examId);

    /**
     * 增加考试信息,并将移动端url修改为url+examId 形式
     *
     * @param examDO 考试信息类
     */
    void save(ExamDO examDO);

    /**
     * 修改考试信息,,并将移动端url修改为url+examId 形式
     *
     * @param examId 考试Id
     * @param examDO 考试信息
     * @return 更新条数
     */
    long update(String examId, ExamDO examDO);

    /**
     * 通过考试id删除考试信息
     *
     * @param examId 考试id
     */
    void delete(String examId);

    /**
     * 获取当前条件下全部考试信息
     *
     * @param start    起始页
     * @param size   每页的条数
     * @param keyword 关键字
     * @return 考试信息列表
     */
    List<ExamDO> queryList(int start, int size, String keyword);
}
