package com.neuq.question.data.dao.impl;

import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.support.ObjectIdUtil;
import com.neuq.question.web.rest.management.conference.ConferenceController;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 大会数据操作类
 *
 * @author wangshyi
 */
@Repository
public class ConferenceRepositoryImpl extends AbstractMongoRepository<ConferenceDO> implements ConferenceRepository {

    public ConferenceRepositoryImpl(MongoTemplate template) {
        super(ConferenceDO.class, template);
    }

    @Override
    public List<ConferenceDO> list(Date endTimeAfter, int start, int size) {

        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ENABLE).ne(false);

        if (endTimeAfter != null) {
            criteria.and(ConferenceDO.FIELD_END_TIME).gt(endTimeAfter);
        }

        Query query = Query.query(criteria).skip(start).limit(size);


        return template.find(query, clazz);
    }

    @Override
    public List<ConferenceDO> listWithoutNoStart() {

        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ENABLE).ne(false)
                .and(ConferenceDO.FIELD_START_TIME).lte(new Date());

        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public long count(Date endTimeAfter) {
        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ENABLE).ne(false);

        if (endTimeAfter != null) {
            criteria.and(ConferenceDO.FIELD_END_TIME).gt(endTimeAfter);
        }
        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public long countByMemberId(String memberId, Date endTimeAfter) {
        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ENABLE).ne(false);

        if (endTimeAfter != null) {
            criteria.and(ConferenceDO.FIELD_END_TIME).gt(endTimeAfter);
        }
        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public ConferenceDO queryByConferenceId(String conferenceId) {
        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ID).is(ObjectIdUtil.getObjectId(conferenceId))
                .and(ConferenceDO.FIELD_ENABLE).ne(false);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public List<ConferenceDO> queryByMemberId(String memberId) {

        return template.findAll(clazz);
    }

    @Override
    public List<ConferenceDO> queryByMemberIdWithPage(String memberId, Date endTimeAfter, int start, int size) {
               Criteria criteria = Criteria.where(ConferenceDO.FIELD_ENABLE).ne(false);

        if (endTimeAfter != null) {
            criteria.and(ConferenceDO.FIELD_END_TIME).gt(endTimeAfter);
        }

        Query query = Query.query(criteria).skip(start).limit(size);
        return template.find(query, clazz);
    }

    @Override
    public List<ConferenceDO> queryAllConference() {
        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ENABLE).ne(false);
        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public Set<String> queryHelpSignInMembers(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ID).is(conferenceId)
                .and(ConferenceDO.FIELD_ENABLE).ne(false);

        ConferenceDO conferenceDO = template.findOne(Query.query(criteria), clazz);
        return conferenceDO == null ? new HashSet<>() : conferenceDO.getHelpSignInRoles();
    }

    @Override
    public void save(ConferenceDO conference) {
        long ctime = System.currentTimeMillis();
        conference.setCtime(ctime);
        conference.setUtime(ctime);
        template.save(conference);
    }

    @Override
    public long update(String conferenceId, ConferenceController.ConferenceDTO conference) {

        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ID).is(ObjectIdUtil.getObjectId(conferenceId));

        Update update = Update.update(ConferenceDO.FIELD_NAME, conference.getName())
                .set(ConferenceDO.FIELD_START_TIME, conference.getStartTime())
                .set(ConferenceDO.FIELD_END_TIME, conference.getEndTime())
                .set(ConferenceDO.FIELD_IMAGE, conference.getImage())
                .set(ConferenceDO.FIELD_THUMB_IMAGE, conference.getThumbImage())
                .set(ConferenceDO.FIELD_TOPIC, conference.getTopic())
                .set(ConferenceDO.FIELD_HELP_SIGN_IN_ROLES, conference.getHelpSignInRoles())
                .set(ConferenceDO.FIELD_UTIME, System.currentTimeMillis());

        return template.updateFirst(Query.query(criteria), update, clazz).getModifiedCount();
    }

    @Override
    public long disable(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceDO.FIELD_ID).is(ObjectIdUtil.getObjectId(conferenceId));

        Update update = Update.update(ConferenceDO.FIELD_ENABLE, false)
                .set(ConferenceDO.FIELD_UTIME, System.currentTimeMillis());

        return template.updateFirst(Query.query(criteria), update, clazz).getModifiedCount();
    }
}
