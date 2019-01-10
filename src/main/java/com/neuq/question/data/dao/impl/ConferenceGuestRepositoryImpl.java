package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.ConferenceGuestRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceGuestDO;
import com.neuq.question.web.rest.management.conference.ConferenceGuestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yegk7
 * @since 2018/8/1 19:03
 */
@Slf4j
@Repository
public class ConferenceGuestRepositoryImpl extends AbstractMongoRepository<ConferenceGuestDO> implements ConferenceGuestRepository {
    public ConferenceGuestRepositoryImpl(MongoTemplate template) {
        super(ConferenceGuestDO.class, template);
    }

    @Override
    public void save(ConferenceGuestDO guestDO, String conferenceId) {

        guestDO.setConferenceId(conferenceId);

        Long ts = System.currentTimeMillis();
        guestDO.setCtime(ts);
        guestDO.setUtime(ts);

        template.save(guestDO);
    }

    @Override
    public void upsert(ConferenceGuestDO guestDO, String conferenceId) {

        guestDO.setConferenceId(conferenceId);

        Long ts = System.currentTimeMillis();
        guestDO.setCtime(ts);
        guestDO.setUtime(ts);


        Criteria criteria = Criteria.where(ConferenceGuestDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuestDO.FIELD_MOBILE).is(guestDO.getMobile());

        Update update = Update.update(ConferenceGuestDO.FIELD_COMPANY, guestDO.getCompany())
                .set(ConferenceGuestDO.FIELD_EMAIL, guestDO.getEmail())
                .set(ConferenceGuestDO.FIELD_NAME, guestDO.getName())
                .set(ConferenceGuestDO.FIELD_POSITION, guestDO.getPosition())
                .set(ConferenceGuestDO.FIELD_GENDER, guestDO.getGender())
                .set(ConferenceGuestDO.FIELD_BIRTHDAY, guestDO.getBirthday())
                .set(ConferenceGuestDO.FIELD_DEPARTMENT, guestDO.getDepartment())
                .set(ConferenceGuestDO.FIELD_UTIME, System.currentTimeMillis());

        template.upsert(Query.query(criteria), update, clazz);
    }

    @Override
    public long count(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceGuestDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public List<ConferenceGuestDO> list(String conferenceId, int start, int size) {

        Criteria criteria = Criteria.where(ConferenceGuestDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Query query = Query.query(criteria).skip(start).limit(size);
        return template.find(query, clazz);
    }

    @Override
    public List<String> mobileList(String conferenceId) {
        Criteria criteria = Criteria.where(ConferenceGuestDO.FIELD_CONFERENCE_ID).is(conferenceId);

        List<ConferenceGuestDO> guestDOList = template.find(Query.query(criteria), clazz);
        return guestDOList.stream()
                .map(ConferenceGuestDO::getMobile)
                .collect(Collectors.toList());
    }

    @Override
    public long update(ConferenceGuestController.ConferenceParticipantsDTO guestDTO, String guestId,
                       String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceGuestDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuestDO.FIELD_ID).is(guestId);

        Update update = Update.update(ConferenceGuestDO.FIELD_NAME, guestDTO.getName())
                .set(ConferenceGuestDO.FIELD_COMPANY, guestDTO.getCompany())
                .set(ConferenceGuestDO.FIELD_EMAIL, guestDTO.getEmail())
                .set(ConferenceGuestDO.FIELD_MOBILE, guestDTO.getMobile())
                .set(ConferenceGuestDO.FIELD_POSITION, guestDTO.getPosition());

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} guest {} found when update guest item", conferenceId,
                    guestId);
        }

        return n;
    }

    @Override
    public void delete(String guestId, String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceGuestDO.FIELD_ID).is(guestId)
                .and(ConferenceGuestDO.FIELD_CONFERENCE_ID).is(conferenceId);

        template.remove(Query.query(criteria), clazz);
    }

    @Override
    public boolean isGuest(String conferenceId, String mobile) {

        Criteria criteria = Criteria.where(ConferenceGuestDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuestDO.FIELD_MOBILE).is(mobile);

        return template.exists(Query.query(criteria), clazz);

    }
}
