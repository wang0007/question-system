package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ActivitySignInSettingDO;
import com.neuq.question.domain.enums.SignInScope;
import com.neuq.question.web.rest.management.signin.ActivitySignInSettingController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangshyi
 * @since 2018/7/17 10:06
 */
@Slf4j
@Repository
public class ActivitySignInSettingRepositoryImpl extends AbstractMongoRepository<ActivitySignInSettingDO> implements ActivitySignInSettingRepository {

    public ActivitySignInSettingRepositoryImpl(MongoTemplate template) {
        super(ActivitySignInSettingDO.class, template);
    }

    @Override
    public List<ActivitySignInSettingDO> queryByConferenceId(String conferenceId, Boolean requiredSignIn) {

        Criteria criteria = Criteria.where(ActivitySignInSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        if (requiredSignIn != null) {
            criteria.and(ActivitySignInSettingDO.FIELD_ENABLE).is(requiredSignIn);
        }

        Query query = Query.query(criteria);

        return template.find(query, clazz);
    }


    @Override
    public ActivitySignInSettingDO queryByActivityId(String activityId) {

        Criteria criteria = Criteria.where(ActivitySignInSettingDO.FIELD_ACTIVITY_ID).is(activityId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void save(ActivitySignInSettingDO setting) {
        long ts = System.currentTimeMillis();

        setting.setCtime(ts);
        setting.setUtime(ts);

        template.save(setting);
    }


    @Override
    public long update(String conferenceId, String activityId,
                       ActivitySignInSettingController.ActivitySignInSettingDTO signInSetting) {

        Criteria criteria = Criteria.where(ActivitySignInSettingDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivitySignInSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Query query = Query.query(criteria);
        Update update = Update
                .update(ActivitySignInSettingDO.FIELD_JOIN_GROUP_SETTING, signInSetting.getJoinGroupSetting())
                .set(ActivitySignInSettingDO.FIELD_NOTIFY_SETTING, signInSetting.getNotifySetting())
                .set(ActivitySignInSettingDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(query, update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {} sign in setting found when update sign in setting", activityId);
        }
        return n;
    }


    @Override
    public long updateProjection(String activityId,
                                 ActivitySignInSettingController.SignInProjectionSettingDTO projectionSettingDTO) {

        Criteria criteria = Criteria.where(ActivitySignInSettingDO.FIELD_ACTIVITY_ID).is(activityId);

        Query query = Query.query(criteria);
        Update update = Update.update(ActivitySignInSettingDO.FILED_PROJECTION, projectionSettingDTO.getProjection())
                .set(ActivitySignInSettingDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(query, update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {} sign in projection setting found when update sign in projection setting",
                    activityId);
        }
        return n;
    }


    @Override
    public long updateRequired(String conferenceId, String activityId, Boolean enable) {

        Criteria criteria = Criteria.where(ActivitySignInSettingDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivitySignInSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Update update = Update.update(ActivitySignInSettingDO.FIELD_ENABLE, enable)
                .set(ActivitySignInSettingDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {} sign in enable setting found when update sign in enable setting",
                    activityId);
        }
        return n;
    }

    @Override
    public long updateSignInScope(String activityId, SignInScope scope) {
        Criteria criteria = Criteria.where(ActivitySignInSettingDO.FIELD_ACTIVITY_ID).is(activityId);

        Update update = Update.update(ActivitySignInSettingDO.FIELD_SCOPE, scope)
                .set(ActivitySignInSettingDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {} sign in enable setting found when update sign in enable setting",
                    activityId);
        }
        return n;
    }
}
