package com.neuq.question.data.dao.impl;

import com.neuq.question.data.dao.ActivitySignInRecordRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ActivitySignInRecordDO;
import com.neuq.question.domain.enums.SignInType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yegk7
 * @since 2018/7/17 17:00
 */
@Slf4j
@Repository
public class ActivitySignInRecordRepositoryImpl extends AbstractMongoRepository<ActivitySignInRecordDO> implements ActivitySignInRecordRepository {

    public ActivitySignInRecordRepositoryImpl(MongoTemplate template) {
        super(ActivitySignInRecordDO.class, template);
    }


    @Override
    public ActivitySignInRecordDO querySignInRecord(String activityId, String memberId) {

        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivitySignInRecordDO.FIELD_USER_MEMBER_ID).is(memberId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void insert(ActivitySignInRecordDO signInRecordDO) {
        Long ts = System.currentTimeMillis();

        signInRecordDO.setCtime(ts);
        signInRecordDO.setUtime(ts);

        template.save(signInRecordDO);
    }

    @Override
    public List<ActivitySignInRecordDO> list(String activityId, String phoneKeyword, String nameKeyword, int start,
                                             int size) {

        Query query = buildSearchQuery(activityId, phoneKeyword, nameKeyword);
        return template.find(query.skip(start).limit(size), clazz);
    }

    @Override
    public long count(String activityId, String phoneKeyword, String nameKeyword) {
        Query query = buildSearchQuery(activityId, phoneKeyword, nameKeyword);
        return template.count(query, clazz);
    }

    private Query buildSearchQuery(String activityId, String phoneKeyword, String nameKeyword) {
        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId);

        if (StringUtils.isNotBlank(phoneKeyword)) {
            criteria.and(ActivitySignInRecordDO.FIELD_USER_PHONE).regex(phoneKeyword);
        }

        if (StringUtils.isNotBlank(nameKeyword)) {
            criteria.and(ActivitySignInRecordDO.FIELD_USER_NAME).regex(nameKeyword);
        }

        return Query.query(criteria);
    }


    @Override
    public List<ActivitySignInRecordDO> incrementalList(String activityId, Long ts, Long currentTs) {

        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivitySignInRecordDO.FIELD_TIMESTAMP).gte(ts);
        Criteria criteria1 = Criteria.where(ActivitySignInRecordDO.FIELD_TIMESTAMP).lt(currentTs);
        criteria.andOperator(criteria1);

        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public long count(String activityId) {
        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId);

        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public List<ActivitySignInRecordDO> getAllRecordList(String activityId) {

        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId);
        Query query = Query.query(criteria);

        return template.find(query, clazz);
    }

    @Override
    public List<ActivitySignInRecordDO> queryRecordList(String activityId, int start, int size) {
        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId);
        Query query = Query.query(criteria).skip(start).limit(size);

        return template.find(query, clazz);
    }

    @Override
    public List<String> getAllRecordMemberList(String activityId) {

        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId);
        Query query = Query.query(criteria);

        List<ActivitySignInRecordDO> objectList = template.find(query, clazz);

        return objectList.stream().map(signInRecordDO -> signInRecordDO.getUser().getMemberId())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllRecordMobileList(String activityId) {

        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId);
        Query query = Query.query(criteria);

        List<ActivitySignInRecordDO> objectList = template.find(query, clazz);

        return objectList.stream().map(signInRecordDO -> signInRecordDO.getUser().getMobile())
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivitySignInRecordDO> queryHelpRecordList(String activityId, int start, int size) {

        Criteria criteria = Criteria.where(ActivitySignInRecordDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivitySignInRecordDO.FIELD_TYPE).is(SignInType.HELP_SIGN_IN);
        Query query = Query.query(criteria).skip(start).limit(size);

        return template.find(query, clazz);
    }
}
