package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ConferenceBackgroundRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.common.ConferenceBackgroundDO;
import com.neuq.question.domain.enums.BackgroundType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author wangshyi
 * @since 2018/11/30 19:21
 */
@Slf4j
@Repository
public class ConferenceBackgroundRepositoryImpl extends AbstractMongoRepository<ConferenceBackgroundDO> implements ConferenceBackgroundRepository {
    public ConferenceBackgroundRepositoryImpl(MongoTemplate template) {
        super(ConferenceBackgroundDO.class, template);
    }

    @Override
    public void insert(ConferenceBackgroundDO conferenceBackgroundDO) {

        long ts = System.currentTimeMillis();
        conferenceBackgroundDO.setCtime(ts);
        conferenceBackgroundDO.setUtime(ts);

        template.save(conferenceBackgroundDO);
    }

    @Override
    public ConferenceBackgroundDO query(String conferenceId, BackgroundType backgroundType) {

        Criteria criteria = Criteria.where(ConferenceBackgroundDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceBackgroundDO.FIELD_BACK_GROUND_TYPE).is(backgroundType);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public long insertBackgroundItem(String conferenceId, ConferenceBackgroundDO.BackgroundBO backgroundBO, BackgroundType backgroundType) {

        if (StringUtils.isBlank(backgroundBO.getBackgroundId())) {
            backgroundBO.setBackgroundId(UUID.randomUUID().toString());
        }
        Criteria criteria = Criteria.where(ConferenceBackgroundDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceBackgroundDO.FIELD_BACK_GROUND_TYPE).is(backgroundType);

        Update update = new Update().push(ConferenceBackgroundDO.FIELD_BACK_GROUND, backgroundBO);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} background {} found when insert background item", conferenceId, backgroundBO.getBackgroundId());
        }
        return n;
    }

    @Override
    public long delete(String conferenceId, String backgroundId, BackgroundType backgroundType) {

        Criteria criteria = Criteria.where(ConferenceBackgroundDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceBackgroundDO.FIELD_BACK_GROUND_TYPE).is(backgroundType);

        ConferenceBackgroundDO.BackgroundBO backgroundBO = new ConferenceBackgroundDO.BackgroundBO();
        backgroundBO.setBackgroundId(backgroundId);

        Update update = new Update().pull(ConferenceBackgroundDO.FIELD_BACK_GROUND, backgroundBO);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} background {} found when delete background item", conferenceId, backgroundId);
        }
        return n;
    }

    @Override
    public ConferenceBackgroundDO getDefaultBackground(BackgroundType backgroundType) {

        Criteria criteria = Criteria.where(ConferenceBackgroundDO.FIELD_BACK_GROUND_TYPE).is(BackgroundType.COMMON);

        if (BackgroundType.SIGN_UP.equals(backgroundType)) {
            criteria.and(ConferenceBackgroundDO.FIELD_ID).is(ConferenceBackgroundDO.SIGN_UP_BACK_GROUND_ID);
        }else {
            criteria.and(ConferenceBackgroundDO.FIELD_ID).is(ConferenceBackgroundDO.BACK_GROUND_ID);
        }

        return template.findOne(Query.query(criteria), clazz);
    }
}

