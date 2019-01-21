package com.neuq.question.data.dao.impl;

import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.InAPIUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/12/27  15:30
 */
@Slf4j
@Repository
public class UserRepsitoryImpl extends AbstractMongoRepository<InAPIUser> implements UserRepsitory {
    public UserRepsitoryImpl(MongoTemplate template) {
        super(InAPIUser.class, template);
    }

    @Override
    public InAPIUser signUp(InAPIUser inAPIUser) {

        //随机生成memberId
        inAPIUser.setMemberId(RandomStringUtils.randomAlphanumeric(16));

        template.save(inAPIUser);
        return inAPIUser;
    }

    @Override
    public InAPIUser isLogin(String loginName, String password) {

        Criteria criteria = Criteria.where(InAPIUser.FIELD_LOGIN_NAME).is(loginName)
                .and(InAPIUser.FIELD_PASSWORD).is(password);

        return template.findOne(Query.query(criteria),clazz);
    }

    @Override
    public boolean isUserName(String loginName) {

        Criteria criteria = Criteria.where(InAPIUser.FIELD_LOGIN_NAME).is(loginName);
        return template.exists(Query.query(criteria),clazz);

    }

    @Override
    public InAPIUser update(InAPIUser inAPIUser) {

        Criteria criteria = Criteria.where(InAPIUser.FIELD_MEMBER_ID).is(inAPIUser.getMemberId());

        Update update = Update.update(InAPIUser.FIELD_NAME,inAPIUser.getName())
                .set(InAPIUser.FIELD_MOBILE,InAPIUser.FIELD_MOBILE)
                .set(InAPIUser.FIELD_EMAIL,inAPIUser.getEmail())
                .set(InAPIUser.FIELD_SEX,inAPIUser.getSex())
                .set(InAPIUser.FIELD_BIRTHDAY,inAPIUser.getBirthday());

        template.upsert(Query.query(criteria),update,clazz);

        return template.findOne(Query.query(criteria),clazz);
    }

    @Override
    public InAPIUser queryById(String memberId) {
        Criteria criteria = Criteria.where(InAPIUser.FIELD_MEMBER_ID).is(memberId);
        return template.findOne(Query.query(criteria),clazz);
    }

    @Override
    public List<InAPIUser> queryAll() {

        return template.findAll(clazz);
    }

    @Override
    public boolean queryByLoginName(String loginName) {

        Criteria criteria = Criteria.where(InAPIUser.FIELD_LOGIN_NAME).is(loginName);
        List<InAPIUser> inAPIUsers = template.find(Query.query(criteria), clazz);
        return inAPIUsers.size() != 0;

    }
}
