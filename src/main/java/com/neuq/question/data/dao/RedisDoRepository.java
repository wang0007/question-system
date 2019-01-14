package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.RedisDo;

/**
 * @author wangshyi
 * @date 2019/1/12  16:07
 */
public interface RedisDoRepository {

    void saveDo(RedisDo redisDo);

    boolean judge(RedisDo redisDo);

    void update(RedisDo redisDo);

    RedisDo queryByKey(String key);
}
