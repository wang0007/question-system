package com.neuq.question.service;


import com.neuq.question.data.pojo.QuestionBlankDO;

import java.util.List;

/**
 * @author wangshyi
 */
public interface QuestionBlankService {

    /**
     * 查询符合条件的题库
     * @param start 分页起始页
     * @param size 每页条数
     * @param enable 题库状态（可用/不可用）
     * @param keyword 关键字搜索
     * @return 符合条件的题库
     */
    List<QuestionBlankDO> getList(int start, int size, Boolean enable, String keyword);

    /**
     * 查询对应的题库id下的状态
     * @param blankId 题库id
     * @return true:可用 false:不可用
     */
    boolean verifyQuestionBlank(String blankId);
}
