package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.SignUpSettingRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceSignUpSettingDO;
import com.neuq.question.web.rest.management.conference.signup.SignUpSettingController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author liuhaoi
 */
@Slf4j
@Repository
public class SignUpSettingRepositoryImpl extends AbstractMongoRepository<ConferenceSignUpSettingDO> implements SignUpSettingRepository {

    public SignUpSettingRepositoryImpl(MongoTemplate template) {
        super(ConferenceSignUpSettingDO.class, template);
    }

    @Override
    public ConferenceSignUpSettingDO queryById(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceSignUpSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void save(ConferenceSignUpSettingDO settingDO) {
        long ts = System.currentTimeMillis();
        settingDO.setCtime(ts);
        settingDO.setUtime(ts);
        template.save(settingDO);
    }

    @Override
    public long update(String conferenceId, SignUpSettingController.SignUpSettingDTO setting) {

        Criteria criteria = Criteria.where(ConferenceSignUpSettingDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Update update = Update.update(ConferenceSignUpSettingDO.FIELD_CLOSING_TIME, setting.getClosingTime())
                .set(ConferenceSignUpSettingDO.FIELD_START_TIME, setting.getStartTime())
                .set(ConferenceSignUpSettingDO.FIELD_NEED_INVITATION_CODE, setting.getNeedInvitationCode())
                .set(ConferenceSignUpSettingDO.FIELD_UTIME, System.currentTimeMillis());


        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();

        if (n == 0) {
            log.warn("No matched conference {} setting found when update sign up form setting", conferenceId);
        }

        return n;
    }
}
