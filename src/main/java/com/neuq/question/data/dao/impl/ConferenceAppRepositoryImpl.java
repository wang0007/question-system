package com.neuq.question.data.dao.impl;


import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ConferenceAppRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceAppDO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author liuhaoi
 */
@Repository
public class ConferenceAppRepositoryImpl extends AbstractMongoRepository<ConferenceAppDO> implements ConferenceAppRepository {

    public ConferenceAppRepositoryImpl(MongoTemplate template) {
        super(ConferenceAppDO.class, template);
    }

    @Override
    public ConferenceAppDO queryByConferenceId(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceAppDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void save(ConferenceAppDO app) {

        app.setId(null);
        app.setCtime(System.currentTimeMillis());
        app.setUtime(System.currentTimeMillis());

        template.save(app);

    }

    @Override
    public long updateStatus(String conferenceId, String appId, boolean status) {

        Criteria criteria = Criteria.where(ConferenceAppDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceAppDO.FIELD_APP_ID).is(appId);

        Update update = Update.update(ConferenceAppDO.FIELD_APP_ENABLE_UPDATE, status);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        return updateResult.getModifiedCount();
    }
}
