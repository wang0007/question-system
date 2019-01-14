package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.SignUpInvitationCodeRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceSignUpInvitationCodeDO;
import com.neuq.question.support.ObjectIdUtil;
import com.neuq.question.web.rest.management.conference.signup.SignUpInvitationCodeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 * @since 2018/11/13 18:20
 */
@Repository
@Slf4j
public class SignUpInvitationCodeRepositoryImpl extends AbstractMongoRepository<ConferenceSignUpInvitationCodeDO> implements SignUpInvitationCodeRepository {

    public SignUpInvitationCodeRepositoryImpl(MongoTemplate template) {
        super(ConferenceSignUpInvitationCodeDO.class, template);
    }

    @Override
    public void create(ConferenceSignUpInvitationCodeDO codeDO) {
        Long ts = System.currentTimeMillis();


        codeDO.setCtime(ts);
        codeDO.setUtime(ts);

        template.save(codeDO);
    }

    @Override
    public long update(String conferenceId, String invitationCodeId, SignUpInvitationCodeController.ConferenceSignUpInvitationCodeDTO codeDTO) {
        Criteria criteria = Criteria.where(ConferenceSignUpInvitationCodeDO.FIELD_ID).is(ObjectIdUtil.getObjectId(invitationCodeId))
                .and(ConferenceSignUpInvitationCodeDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Update update = Update.update(ConferenceSignUpInvitationCodeDO.FIELD_CHANNEL, codeDTO.getChannel())
                .set(ConferenceSignUpInvitationCodeDO.FIELD_UTIME, System.currentTimeMillis());

        if (codeDTO.getCode() != null) {
            update.set(ConferenceSignUpInvitationCodeDO.FIELD_CODE, codeDTO.getCode());
        }

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();

        if (n == 0) {
            log.warn("No matched invitationCode {} setting found when update sign up invitation code", invitationCodeId);
        }

        return n;
    }

    @Override
    public long count(String conferenceId) {
        Criteria criteria = Criteria.where(ConferenceSignUpInvitationCodeDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public List<ConferenceSignUpInvitationCodeDO> list(String conferenceId, int start, int size) {

        Criteria criteria = Criteria.where(ConferenceSignUpInvitationCodeDO.FIELD_CONFERENCE_ID).is(conferenceId);
        Query query = Query.query(criteria).skip(start).limit(size)
                .with(new Sort(Sort.Direction.DESC, ConferenceSignUpInvitationCodeDO.FIELD_UTIME));

        return template.find(query, clazz);
    }

    @Override
    public long changeEnable(String conferenceId, String invitationCodeId, Boolean enable) {

        Criteria criteria = Criteria.where(ConferenceSignUpInvitationCodeDO.FIELD_ID).is(ObjectIdUtil.getObjectId(invitationCodeId))
                .and(ConferenceSignUpInvitationCodeDO.FIELD_CONFERENCE_ID).is(conferenceId);
        Update update = Update.update(ConferenceSignUpInvitationCodeDO.FIELD_ENABLE, enable);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();

        if (n == 0) {
            log.warn("No matched invitationCode {} setting found when update sign up invitation code enable", invitationCodeId);
        }

        return n;
    }

    @Override
    public ConferenceSignUpInvitationCodeDO queryById(String invitationCodeId) {

        Criteria criteria = Criteria.where(ConferenceSignUpInvitationCodeDO.FIELD_ID).is(ObjectIdUtil.getObjectId(invitationCodeId));

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public List<String> invitationCodeList(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceSignUpInvitationCodeDO.FIELD_CONFERENCE_ID).is(conferenceId);
        Query query = new Query(criteria);

        return template.find(query, clazz).stream()
                .map(ConferenceSignUpInvitationCodeDO::getCode)
                .collect(Collectors.toList());
    }


}
