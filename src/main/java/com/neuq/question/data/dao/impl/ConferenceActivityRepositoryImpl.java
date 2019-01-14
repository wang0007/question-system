package com.neuq.question.data.dao.impl;

import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.data.pojo.common.BasicDO;
import com.neuq.question.support.ObjectIdUtil;
import com.neuq.question.web.rest.management.conference.ConferenceActivityController;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author wangshyi
 */
@Repository
public class ConferenceActivityRepositoryImpl extends AbstractMongoRepository<ConferenceActivityDO> implements ConferenceActivityRepository {

    public ConferenceActivityRepositoryImpl(MongoTemplate template) {
        super(ConferenceActivityDO.class, template);
    }

    @Override
    public List<ConferenceActivityDO> list(String conferenceId, int start, int size) {

        Criteria criteria = Criteria.where(ConferenceActivityDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Query query = Query.query(criteria).skip(start).limit(size);

        return template.find(query, clazz);
    }

    @Override
    public long count(String conferenceId) {
        Criteria criteria = Criteria.where(ConferenceActivityDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Query query = Query.query(criteria);

        return template.count(query, clazz);
    }

    @Override
    public ConferenceActivityDO query(String conferenceId, String activityId) {

        Criteria criteria = Criteria.where(ConferenceActivityDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(BasicDO.FIELD_ID).is(ObjectIdUtil.getObjectId(activityId));

        Query query = Query.query(criteria);

        return template.findOne(query, clazz);
    }

    @Override
    public ConferenceActivityDO query(String activityId) {
        Criteria criteria = Criteria.where(BasicDO.FIELD_ID).is(ObjectIdUtil.getObjectId(activityId));

        Query query = Query.query(criteria);

        return template.findOne(query, clazz);
    }

    @Override
    public void create(String activityId, ConferenceActivityDO activity) {

        long ts = System.currentTimeMillis();
        activity.setCtime(ts);
        activity.setUtime(ts);

        template.save(activity);
    }

    @Override
    public long update(String conferenceId, String activityId,
                       ConferenceActivityController.ConferenceActivityDTO activity) {

        Criteria criteria = Criteria.where(ConferenceActivityDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(BasicDO.FIELD_ID).is(ObjectIdUtil.getObjectId(activityId));

        Query query = Query.query(criteria);

        Update update = Update.update(ConferenceActivityDO.FIELD_NAME, activity.getName())
                .set(ConferenceActivityDO.FIELD_ACTIVITY_DATE, activity.getActivityDate())
                .set(ConferenceActivityDO.FIELD_START_TIME, activity.getStartTime())
                .set(ConferenceActivityDO.FIELD_END_TIME, activity.getEndTime())
                .set(ConferenceActivityDO.FIELD_UTIME, System.currentTimeMillis());

        return template.updateFirst(query, update, clazz).getModifiedCount();
    }

    @Override
    public long delete(String conferenceId, String activityId) {

        Criteria criteria = Criteria.where(ConferenceActivityDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(BasicDO.FIELD_ID).is(ObjectIdUtil.getObjectId(activityId));

        return template.remove(Query.query(criteria), clazz).getDeletedCount();
    }

    @Override
    public ConferenceActivityDO queryPrincipalActivity(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceActivityDO.FIELD_CONFERENCE_ID).is(conferenceId);

        List<ConferenceActivityDO> activityDOS = template.find(Query.query(criteria), clazz);
        Optional<ConferenceActivityDO> optional = activityDOS.stream()
                .filter(conferenceActivityDO -> conferenceActivityDO.getPrincipal() != null)
                .filter(ConferenceActivityDO::getPrincipal)
                .findFirst();
        return optional.orElse(null);
    }

    @Override
    public long updatePrincipal(String conferenceId, String activityId, boolean principal) {

        Criteria criteria = Criteria.where(ConferenceActivityDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(BasicDO.FIELD_ID).is(ObjectIdUtil.getObjectId(activityId));

        Query query = Query.query(criteria);
        Update update = Update.update(ConferenceActivityDO.FIELD_PRINCIPAL, principal)
                .set(ConferenceActivityDO.FIELD_UTIME, System.currentTimeMillis());

        return template.updateFirst(query, update, clazz).getModifiedCount();
    }
}
