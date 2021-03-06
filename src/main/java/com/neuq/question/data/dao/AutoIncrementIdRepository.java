package com.neuq.question.data.dao;

/**
 * @author wangshyi
 * @create 2018/11/17 21:01
 */
public interface AutoIncrementIdRepository {

    /**
     * 获取下一个序号
     *
     * @param category 分类ID
     * @return 下一个序号
     */
    Long nextID(String category);

}
