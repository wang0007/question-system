package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.QuestionBlankDO;
import com.neuq.question.web.rest.management.answer.question.QuestionBlankController;

import java.util.List;


/**
 * @author yegk7
 */
public interface QuestionBlankRepository {

    /**
     * 新建题库
     *
     * @param questionBlankDO 题库信息
     */
    void save(QuestionBlankDO questionBlankDO);

    /**
     * 编辑题库
     *
     * @param questionBlankDTO 要修改的题库信息
     * @param blankId          题库id
     * @return 修改数量
     */
    long update(QuestionBlankController.QuestionBlankDTO questionBlankDTO, String blankId);

    /**
     * 通过题库id查找相应的题库
     *
     * @param blankId 题库id
     * @return 题库信息
     */
    QuestionBlankDO queryById(String blankId);

    /**
     * 将题库更改为可用
     *
     * @param blankId 题库id
     */
    void setEnable(String blankId);

    /**
     * 将题库更改为不可用
     *
     * @param blankId 题库id
     */
    void setDisable(String blankId);

    /**
     * 查询题库id下当前状态
     *
     * @param blankId 题库id
     * @return true：可用 false：不可用
     */
    boolean queryEnableById(String blankId);

    /**
     * 根据条件查询对应题库列表
     *
     * @param enable  题库状态
     * @param keyword 关键字
     * @param skip    起始页
     * @param limit   每页条数
     * @return 符合题库列表, 按更新时间排序
     */
    List<QuestionBlankDO> getList(Boolean enable, String keyword, int skip, int limit);

    /**
     * 查询所有题库列表
     *
     * @param keyword 关键字
     * @param skip    起始页
     * @param limit   每页条数
     * @return 符合的题库列表
     */
    List<QuestionBlankDO> queryAll(String keyword, int skip, int limit);


}
