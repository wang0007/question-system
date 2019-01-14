package com.neuq.question.data.dao.impl;


import com.neuq.question.data.dao.ShortUrlRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.common.ShortUrlDO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author wangshyi
 * @since 2018/8/3 10:28
 */
@Repository
public class ShortUrlRepositoryImpl extends AbstractMongoRepository<ShortUrlDO> implements ShortUrlRepository {
    public ShortUrlRepositoryImpl(MongoTemplate template) {
        super(ShortUrlDO.class, template);
    }

    @Override
    public void save(ShortUrlDO shortUrlDO) {

        template.save(shortUrlDO);
    }

    @Override
    public ShortUrlDO query(String urlId) {
        Criteria criteria = Criteria.where(ShortUrlDO.FIELD_URL_ID).is(urlId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public ShortUrlDO queryByOriginUrl(String url) {
        Criteria criteria = Criteria.where(ShortUrlDO.FIELD_ORIGINAL_URL).is(url);

        return template.findOne(Query.query(criteria), clazz);
    }
}
