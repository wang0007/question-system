package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.SignUpRecordRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.domain.enums.AuditStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yegk
 */
@Repository
@Slf4j
public class SignUpRecordRepositoryImpl extends AbstractMongoRepository<ConferenceSignUpRecordDO> implements SignUpRecordRepository {
    public SignUpRecordRepositoryImpl(MongoTemplate template) {
        super(ConferenceSignUpRecordDO.class, template);
    }

    @Override
    public List<ConferenceSignUpRecordDO> list(String conferenceId, List<AuditStatus> auditStatus,
                                               String invitationCode, String fieldName, String fieldValue,
                                               Integer start, Integer size) {

        Query query = buildSearchQuery(conferenceId, auditStatus, invitationCode, fieldName, fieldValue);

        return template.find(query.skip(start).limit(size), clazz);
    }

    private Query buildSearchQuery(String conferenceId, List<AuditStatus> auditStatus, String invitationCode,
                                   String fieldName, String fieldValue) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId);
        if (auditStatus != null) {
            criteria.and(ConferenceSignUpRecordDO.FIELD_STATUS).in(auditStatus);
        }

        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.isNotBlank(invitationCode)) {

            Criteria subCriteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_FIELDS_ID_WITHOUT_FIELDS)
                    .is(ConferenceSignUpRecordDO.FIXED_INVITE_CODE_ID)
                    .and(ConferenceSignUpRecordDO.FIELD_FIELDS_VALUE_WITHOUT_FIELDS).is(invitationCode);
            Criteria elemCriteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_FIELDS).elemMatch(subCriteria);
            criteriaList.add(elemCriteria);

        }
        if (StringUtils.isNoneBlank(fieldName, fieldValue)) {
            Criteria subCriteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_FIELDS_ID_WITHOUT_FIELDS).is(fieldName)
                    .and(ConferenceSignUpRecordDO.FIELD_FIELDS_VALUE_WITHOUT_FIELDS).regex(fieldValue);
            Criteria elemCriteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_FIELDS).elemMatch(subCriteria);
            criteriaList.add(elemCriteria);

        }

        if (criteriaList.size() == 1) {
            criteria.andOperator(criteriaList.get(0));
        } else if (criteriaList.size() > 1) {
            criteria.andOperator(criteriaList.get(0), criteriaList.get(1));
        }

        return Query.query(criteria);
    }

    @Override
    public long updateAuditRecord(String conferenceId, List<String> mobiles, AuditStatus status) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceSignUpRecordDO.FIELD_FIELDS_ID).is(ConferenceSignUpRecordDO.FIXED_MOBILE_ID)
                .and(ConferenceSignUpRecordDO.FIELD_FIELDS_VALUE).in(mobiles.toArray());

        Update update = Update.update(ConferenceSignUpRecordDO.FIELD_STATUS, status)
                .set(ConferenceSignUpRecordDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.updateMulti(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched conference {} and mobiles {} sign up record found when update sign up record",
                    conferenceId, mobiles);
        }

        return n;
    }

    @Override
    public void create(ConferenceSignUpRecordDO recordDO) {

        Long ts = System.currentTimeMillis();
        recordDO.setCtime(ts);
        recordDO.setUtime(ts);

        template.save(recordDO);
    }

    @Override
    public ConferenceSignUpRecordDO queryById(String conferenceId, String mobile) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceSignUpRecordDO.FIELD_FIELDS_ID).is(ConferenceSignUpRecordDO.FIXED_MOBILE_ID)
                .and(ConferenceSignUpRecordDO.FIELD_FIELDS_VALUE).is(mobile);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public List<ConferenceSignUpRecordDO> queryByIds(String conferenceId, List<String> mobiles) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceSignUpRecordDO.FIELD_FIELDS_ID).is(ConferenceSignUpRecordDO.FIXED_MOBILE_ID)
                .and(ConferenceSignUpRecordDO.FIELD_FIELDS_VALUE).in(mobiles);

        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public Long count(String conferenceId, List<AuditStatus> auditStatus, String invitationCode, String fieldName,
                      String fieldValue) {

        Query query = buildSearchQuery(conferenceId, auditStatus, invitationCode, fieldName, fieldValue);

        return template.count(query, clazz);
    }

    @Override
    public List<ConferenceSignUpRecordDO> passedList(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceSignUpRecordDO.FIELD_STATUS).is(AuditStatus.PASS);

        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public List<String> passedMobileList(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ConferenceSignUpRecordDO.FIELD_STATUS).is(AuditStatus.PASS);

        List<ConferenceSignUpRecordDO> recordDOList = template.find(Query.query(criteria), clazz);

        return recordDOList.stream()
                .filter(recordDO -> recordDO.getFields() != null && !recordDO.getFields().isEmpty())
                .map(record -> {
                    Optional<ConferenceSignUpRecordDO.FormFieldValue> first = record.getFields().stream()
                            .filter(field -> ConferenceSignUpRecordDO.FIXED_MOBILE_ID
                                    .equalsIgnoreCase(field.getFieldId()))
                            .findFirst();
                    return first.map(ConferenceSignUpRecordDO.FormFieldValue::getValue).orElse(null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> allMobileList(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId);

        List<ConferenceSignUpRecordDO> recordDOList = template.find(Query.query(criteria), clazz);

        return recordDOList.stream()
                .filter(recordDO -> recordDO.getFields() != null && !recordDO.getFields().isEmpty())
                .map(record -> {
                    Optional<ConferenceSignUpRecordDO.FormFieldValue> first = record.getFields().stream()
                            .filter(field -> ConferenceSignUpRecordDO.FIXED_MOBILE_ID
                                    .equalsIgnoreCase(field.getFieldId()))
                            .findFirst();
                    return first.map(ConferenceSignUpRecordDO.FormFieldValue::getValue).orElse(null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ConferenceSignUpRecordDO> allList(String conferenceId) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId);

        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public void insertRecordList(List<ConferenceSignUpRecordDO> recordDOList) {

        template.insertAll(recordDOList);
    }

    @Override
    public boolean isUserSignedUp(String conferenceId, String mobile) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId);

        List<ConferenceSignUpRecordDO> recordDOList = template.find(Query.query(criteria), clazz);

        return recordDOList.stream()
                .anyMatch(recordDO -> recordDO.getFields().stream()
                        .anyMatch(formFieldValue -> ConferenceSignUpRecordDO.FIXED_MOBILE_ID
                                .equals(formFieldValue.getFieldId())
                                && mobile.equals(formFieldValue.getValue())));
    }

    @Override
    public boolean isUserSignedUpPassed(String conferenceId, String mobile) {

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_CONFERENCE_ID).is(conferenceId);

        List<ConferenceSignUpRecordDO> recordDOList = template.find(Query.query(criteria), clazz);

        return recordDOList.stream()
                .filter(recordDO -> AuditStatus.PASS.equals(recordDO.getStatus()))
                .anyMatch(recordDO -> recordDO.getFields().stream()
                        .anyMatch(formFieldValue -> {
                            String mobileValue = formFieldValue.getValue();
                            return ConferenceSignUpRecordDO.FIXED_MOBILE_ID.equals(formFieldValue.getFieldId())
                                    && mobileValue != null && mobileValue.endsWith(mobile);
                        }));
    }

    @Override
    public long countByInvitationCode(String invitationCode, AuditStatus auditStatus) {

        Criteria subCriteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_FIELDS_ID_WITHOUT_FIELDS)
                .is(ConferenceSignUpRecordDO.FIXED_INVITE_CODE_ID)
                .and(ConferenceSignUpRecordDO.FIELD_FIELDS_VALUE_WITHOUT_FIELDS).is(invitationCode);

        Criteria criteria = Criteria.where(ConferenceSignUpRecordDO.FIELD_FIELDS).elemMatch(subCriteria);

        if (auditStatus != null) {
            criteria.and(ConferenceSignUpRecordDO.FIELD_STATUS).is(auditStatus);
        }

        return template.count(Query.query(criteria), clazz);
    }
}
