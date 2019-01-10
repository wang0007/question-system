package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ActivityBarrageSettingRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ActivityBarrageSettingDO;
import com.neuq.question.web.rest.management.barrage.ActivityBarrageSettingController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author yegk7
 * @since 2018/8/5 16:38
 */
@Slf4j
@Repository
public class ActivityBarrageSettingRepositoryImpl extends AbstractMongoRepository<ActivityBarrageSettingDO> implements ActivityBarrageSettingRepository {
    public ActivityBarrageSettingRepositoryImpl(MongoTemplate template) {
        super(ActivityBarrageSettingDO.class, template);
    }

    @Override
    public ActivityBarrageSettingDO queryById(String conferenceId) {

        Criteria criteria = Criteria.where(ActivityBarrageSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public long update(ActivityBarrageSettingDO settingDO, String conferenceId) {

        Criteria criteria = Criteria.where(ActivityBarrageSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Update update = Update.update(ActivityBarrageSettingDO.FIELD_BARRAGE_SIZE, settingDO.getBarrageSize())
                .set(ActivityBarrageSettingDO.FIELD_BARRAGE_SPEED, settingDO.getBarrageSpeed())
                .set(ActivityBarrageSettingDO.FIELD_BACKGROUND_URL, settingDO.getBackgroundUrl())
                .set(ActivityBarrageSettingDO.FIELD_CIRCULATION, settingDO.getCirculation())
                .set(ActivityBarrageSettingDO.FIELD_FULL_SCREEN, settingDO.getFullScreen())
                .set(ActivityBarrageSettingDO.FIELD_THUMB_BACKGROUND, settingDO.getThumbBackground())
                .set(ActivityBarrageSettingDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn(" No matched conference {} found when update conference barrage setting", conferenceId);
        }

        return n;
    }

    @Override
    public void save(ActivityBarrageSettingDO setting) {

        long ts = System.currentTimeMillis();
        setting.setCtime(ts);
        setting.setUtime(ts);

        template.save(setting);
    }

    @Override
    public long updateSpeed(ActivityBarrageSettingController.BarrageSettingDTO barrageSettingDTO, String conferenceId) {

        Criteria criteria = Criteria.where(ActivityBarrageSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Update update = Update.update(ActivityBarrageSettingDO.FIELD_BARRAGE_SPEED, barrageSettingDTO.getBarrageSpeed())
                .set(ActivityBarrageSettingDO.FIELD_CLEAR_BARRAGE, barrageSettingDTO.getClearBarrage())
                .set(ActivityBarrageSettingDO.FIELD_ENTER_FREQUENCY, barrageSettingDTO.getEnterFrequency())
                .set(ActivityBarrageSettingDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn(" No matched conference {} found when update conference barrage speed setting", conferenceId);
        }

        return n;
    }
}
