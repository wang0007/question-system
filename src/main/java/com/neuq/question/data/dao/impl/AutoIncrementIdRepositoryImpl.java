package com.neuq.question.data.dao.impl;

import com.neuq.question.data.dao.AutoIncrementIdRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.common.AutoIncrementIdDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author wangshyi
 * @create 2018/7/17 21:02
 */
@Slf4j
@Repository
public class AutoIncrementIdRepositoryImpl extends AbstractMongoRepository<AutoIncrementIdDO> implements AutoIncrementIdRepository {

    public AutoIncrementIdRepositoryImpl(MongoTemplate template) {
        super(AutoIncrementIdDO.class, template);
    }

    @Override
    public Long nextID(String category) {

        Criteria criteria = Criteria.where(AutoIncrementIdDO.FIELD_CATEGORY).is(category);

        Update update = Update.update(AutoIncrementIdDO.FIELD_U_TIME, System.currentTimeMillis())
                .inc(AutoIncrementIdDO.FIELD_CURRENT, 1);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

        return template.findAndModify(Query.query(criteria), update, options, clazz).getCurrent();
    }
}
