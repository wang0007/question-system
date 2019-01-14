package com.neuq.question.data.dao.impl;

import com.neuq.question.data.dao.RedisDoRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.QuestionResultDO;
import com.neuq.question.data.pojo.RedisDo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangshyi
 * @date 2019/1/12  16:09
 */
@Repository
public class RedisDoRepositoryImpl extends AbstractMongoRepository<RedisDo> implements RedisDoRepository {

    public RedisDoRepositoryImpl(MongoTemplate template) {
        super(RedisDo.class, template);
    }

    @Override
    public void saveDo(RedisDo redisDo) {
        template.save(redisDo);

    }

    @Override
    public boolean judge(RedisDo redisDo) {

        Criteria criteria = Criteria.where(RedisDo.FIELD_KEY).is(redisDo.getKey())
                .and(RedisDo.FIELD_VALUE).is(redisDo.getValue());


        return template.exists(Query.query(criteria), clazz);
    }

    @Override
    public void update(RedisDo redisDo) {
        Criteria criteria = Criteria.where(RedisDo.FIELD_KEY).is(redisDo.getKey());

        Update update = Update.update(RedisDo.FIELD_VALUE, redisDo.getValue());

        template.upsert(Query.query(criteria), update, clazz);
    }

    @Override
    public RedisDo queryByKey(String key) {
        Criteria criteria = Criteria.where(RedisDo.FIELD_KEY).is(key);

        return template.findOne(Query.query(criteria), clazz);

    }
}
