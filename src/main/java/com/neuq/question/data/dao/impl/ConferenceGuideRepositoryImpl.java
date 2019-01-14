package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ConferenceGuideRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceGuideDO;
import com.neuq.question.support.ObjectIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author wangshyi
 */
@Repository
@Slf4j
public class ConferenceGuideRepositoryImpl extends AbstractMongoRepository<ConferenceGuideDO> implements ConferenceGuideRepository {

    public ConferenceGuideRepositoryImpl(MongoTemplate template) {
        super(ConferenceGuideDO.class, template);
    }

    @Override
    public List<ConferenceGuideDO> list(String conferenceId, int skip, int limit) {

        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId);
        Query query = Query.query(criteria).skip(skip).limit(limit);

        return template.find(query, clazz);
    }

    @Override
    public long count(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public ConferenceGuideDO queryById(String conferenceId, String guideId) {
        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuideDO.FIELD_ID).is(guideId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void save(ConferenceGuideDO guide) {

        long ts = System.currentTimeMillis();

        guide.setCtime(ts);
        guide.setUtime(ts);
        if (!guide.getItems().isEmpty()) {
            List<ConferenceGuideDO.GuideItem> guideItems = guide.getItems();
            guideItems.forEach(guideItem -> guideItem.setGuideItemId(UUID.randomUUID().toString()));
            guide.setItems(guideItems);
        }
        template.save(guide);
    }

    @Override
    public void saveAll(Collection<ConferenceGuideDO> guides) {
        long ts = System.currentTimeMillis();
        guides.forEach(guide -> {
            guide.setUtime(ts);
            guide.setCtime(ts);
        });
        template.insertAll(guides);
    }

    @Override
    public long saveGuideItem(String conferenceId, String guideId, ConferenceGuideDO.GuideItem item) {

        if (StringUtils.isBlank(item.getGuideItemId())) {
            item.setGuideItemId(UUID.randomUUID().toString());
        }
        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuideDO.FIELD_ID).is(ObjectIdUtil.getObjectId(guideId));

        Update update = new Update().push(ConferenceGuideDO.FIELD_ITEMS, item);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} guide {} found when save guide item", conferenceId, guideId);
        }

        return n;
    }

    @Override
    public long updateName(String conferenceId, String guideId, String newName, String icon) {
        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuideDO.FIELD_ID).is(ObjectIdUtil.getObjectId(guideId));

        Update update = Update.update(ConferenceGuideDO.FIELD_NAME, newName)
                .set(ConferenceGuideDO.FIELD_ICON, icon);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} guide {} found when update guide name", conferenceId, guideId);
        }

        return n;

    }

    @Override
    public long updateGuideItem(String conferenceId, String guideId, String itemId, ConferenceGuideDO.GuideItem item) {
        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuideDO.FIELD_ID).is(guideId)
                .and(ConferenceGuideDO.FIELD_ITEM_ID).is(itemId);

        if (StringUtils.isBlank(item.getGuideItemId())) {
            item.setGuideItemId(itemId);
        }
        Update update = new Update().set(ConferenceGuideDO.FIELD_ITEMS + ".$", item);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} guide {} found when update guide item", conferenceId, guideId);
        }
        return n;
    }

    @Override
    public long delete(String conferenceId, String guideId) {

        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuideDO.FIELD_ID).is(ObjectIdUtil.getObjectId(guideId));

        DeleteResult deleteResult = template.remove(Query.query(criteria), clazz);

        long n = deleteResult.getDeletedCount();
        if (n == 0) {
            log.warn("No matched conference {} guide {} found when delete guide", conferenceId, guideId);
        }
        return n;

    }

    @Override
    public long deleteItem(String conferenceId, String guideId, String itemId) {

        Criteria criteria = Criteria.where(ConferenceGuideDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceGuideDO.FIELD_ID).is(ObjectIdUtil.getObjectId(guideId));

        ConferenceGuideDO.GuideItem guideItem = new ConferenceGuideDO.GuideItem();
        guideItem.setGuideItemId(itemId);

        Update update = new Update().pull(ConferenceGuideDO.FIELD_ITEMS, guideItem);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} guide {} found when delete guide item", conferenceId, guideId);
        }
        return n;
    }

}
