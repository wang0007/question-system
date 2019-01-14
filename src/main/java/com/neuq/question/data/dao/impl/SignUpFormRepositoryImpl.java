package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.SignUpFormRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceSignUpFormDO;
import com.neuq.question.web.rest.management.conference.signup.SignUpFormController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 */
@Repository
@Slf4j
public class SignUpFormRepositoryImpl extends AbstractMongoRepository<ConferenceSignUpFormDO> implements SignUpFormRepository {

    public SignUpFormRepositoryImpl(MongoTemplate template) {
        super(ConferenceSignUpFormDO.class, template);
    }

    @Override
    public ConferenceSignUpFormDO queryById(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceSignUpFormDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void save(ConferenceSignUpFormDO form) {

        long timeMillis = System.currentTimeMillis();
        form.setCtime(timeMillis);
        form.setUtime(timeMillis);
        form.setId(UUID.randomUUID().toString());

        template.save(form);
    }

    @Override
    public long update(String conferenceId, SignUpFormController.ConferenceSignUpFormDTO form) {

        List<ConferenceSignUpFormDO.FormField> formFields = form.getFields().stream()
                .peek(formField -> {
                    if (formField.getFormFieldId() == null) {
                        formField.setFormFieldId(UUID.randomUUID().toString());
                    }
                })
                .collect(Collectors.toList());

        Criteria criteria = Criteria.where(ConferenceSignUpFormDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Update update = Update.update(ConferenceSignUpFormDO.FIELD_UTIME, System.currentTimeMillis())
                .set(ConferenceSignUpFormDO.FIELD_BACKGROUND_IMAGE, form.getBackgroundImage())
                .set(ConferenceSignUpFormDO.FIELD_THUMB_BACKGROUND, form.getThumbBackground())
                .set(ConferenceSignUpFormDO.FIELD_FIELDS, formFields);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();

        if (n == 0) {
            log.warn("No matched conference {} sign up form found when update sign up form", conferenceId);
        }

        return n;

    }

    @Override
    public long updateFormTitle(String conferenceId, String formTitle) {

        Criteria criteria = Criteria.where(ConferenceSignUpFormDO.FIELD_CONFERENCE_ID).is(conferenceId);

        Update update = Update.update(ConferenceSignUpFormDO.FIELD_FORM_TITLE, formTitle);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();

        if (n == 0) {
            log.warn("No matched conference {} sign up form found when update sign up form title", conferenceId);
        }

        return n;
    }

    @Override
    public long insertField(String conferenceId, ConferenceSignUpFormDO.FormField formField) {

        if (StringUtils.isBlank(formField.getFormFieldId())) {
            formField.setFormFieldId(UUID.randomUUID().toString());
        }
        if (formField.getRequired() == null) {
            formField.setRequired(false);
        }
        formField.setSelected(true);
        if (formField.getAllowModify() == null) {
            formField.setAllowModify(true);
        }

        Criteria criteria = Criteria.where(ConferenceSignUpFormDO.FIELD_CONFERENCE_ID).is(conferenceId);
        Query query = Query.query(criteria);

        Update update = new Update().push(ConferenceSignUpFormDO.FIELD_FIELDS, formField);
        UpdateResult updateResult = template.upsert(query, update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn(" No matched conference {} formField {} found when insert formField item", conferenceId, formField);
        }
        return n;
    }

    @Override
    public long deleteFormField(String conferenceId, String fieldId) {

        Criteria criteria = Criteria.where(ConferenceSignUpFormDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceSignUpFormDO.FIELD_FIELDS_ID).is(fieldId);

        ConferenceSignUpFormDO.FormField formField = new ConferenceSignUpFormDO.FormField();
        formField.setFormFieldId(fieldId);

        Update update = new Update().pull(ConferenceSignUpFormDO.FIELD_FIELDS, formField);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} fieldId {} found when delete field", conferenceId, fieldId);
        }
        return n;
    }


}
