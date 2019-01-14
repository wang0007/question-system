package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ActivityNewsFeedSettingRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ActivityNewsFeedSettingDO;
import com.neuq.question.web.rest.management.feed.NewsFeedSettingController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author yegk7
 * @date 2018/8/30 11:09
 */
@Repository
@Slf4j
public class ActivityNewsFeedSettingRepositoryImpl extends AbstractMongoRepository<ActivityNewsFeedSettingDO> implements ActivityNewsFeedSettingRepository {
    public ActivityNewsFeedSettingRepositoryImpl(MongoTemplate template) {
        super(ActivityNewsFeedSettingDO.class, template);
    }

    @Override
    public long updateProjection(String activityId, NewsFeedSettingController.NewsFeedProjectionSettingDTO settingDTO) {

        Criteria criteria = Criteria.where(ActivityNewsFeedSettingDO.FIELD_ACTIVITY_ID).is(activityId);

        Query query = Query.query(criteria);
        Update update = Update.update(ActivityNewsFeedSettingDO.FIELD_BACKGROUND, settingDTO.getBackground())
                .set(ActivityNewsFeedSettingDO.FIELD_THUMB_BACKGROUND, settingDTO.getThumbBackground())
                .set(ActivityNewsFeedSettingDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(query, update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {} news feed setting found when update news feed setting", activityId);
        }
        return n;
    }

    @Override
    public ActivityNewsFeedSettingDO queryByActivityId(String activityId) {

        Criteria criteria = Criteria.where(ActivityNewsFeedSettingDO.FIELD_ACTIVITY_ID).is(activityId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void save(ActivityNewsFeedSettingDO settingDO) {
        long ts = System.currentTimeMillis();

        settingDO.setCtime(ts);
        settingDO.setUtime(ts);

        template.save(settingDO);
    }
}
