package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.QuestionBlankRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.QuestionBlankDO;
import com.neuq.question.web.rest.management.answer.question.QuestionBlankController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/11/5  14:10
 */
@Slf4j
@Repository
public class QuestionBlankRepositoryImpl extends AbstractMongoRepository<QuestionBlankDO> implements QuestionBlankRepository {

    public QuestionBlankRepositoryImpl(MongoTemplate template) {
        super(QuestionBlankDO.class, template);
    }

    @Override
    public void save(QuestionBlankDO questionBlankDO) {

        questionBlankDO.setCtime(System.currentTimeMillis());
        questionBlankDO.setUtime(System.currentTimeMillis());
        questionBlankDO.setEnable(true);
        template.save(questionBlankDO);
    }

    @Override
    public long update(QuestionBlankController.QuestionBlankDTO questionBlankDTO, String blankId) {

        Criteria criteria = Criteria.where(QuestionBlankDO.FIELD_ID).is(blankId);

        Update update = Update.update(QuestionBlankDO.FIELD_NAME, questionBlankDTO.getName())
                .set(QuestionBlankDO.FIELD_DESCRIPTION, questionBlankDTO.getDescription())
                .set(QuestionBlankDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();

        if (n == 0) {
            log.warn("No matched blankId {} found when update the questionBlank ", blankId);
        }

        return n;
    }

    @Override
    public QuestionBlankDO queryById(String blankId) {

        Criteria criteria = Criteria.where(QuestionBlankDO.FIELD_ID).is(blankId);
        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void setEnable(String blankId) {

        Criteria criteria = Criteria.where(QuestionBlankDO.FIELD_ID).is(blankId);
        Update update = Update.update(QuestionBlankDO.FIELD_ENABLE, true)
                .set(QuestionBlankDO.FIELD_UTIME, System.currentTimeMillis());

        template.updateFirst(Query.query(criteria), update, clazz);
    }

    @Override
    public void setDisable(String blankId) {

        Criteria criteria = Criteria.where(QuestionBlankDO.FIELD_ID).is(blankId);
        Update update = Update.update(QuestionBlankDO.FIELD_ENABLE, false)
                .set(QuestionBlankDO.FIELD_UTIME, System.currentTimeMillis());

        template.updateFirst(Query.query(criteria), update, clazz);
    }

    @Override
    public boolean queryEnableById(String blankId) {

        Criteria criteria = Criteria.where(QuestionBlankDO.FIELD_ID).is(blankId);
        QuestionBlankDO blankDO = template.findOne(Query.query(criteria), clazz);
        return blankDO == null ? false : blankDO.getEnable();
    }

    @Override
    public List<QuestionBlankDO> getList(Boolean enable, String keyword, int skip, int limit) {

        Criteria criteria = new Criteria();

        if (enable != null) {
            criteria.and(QuestionBlankDO.FIELD_ENABLE).is(enable);
        }

        if (keyword != null) {
            Criteria nameCriteria = new Criteria(QuestionBlankDO.FIELD_NAME).regex(keyword);
            Criteria descriptionCriteria = new Criteria(QuestionBlankDO.FIELD_DESCRIPTION).regex(keyword);

            criteria.orOperator(nameCriteria, descriptionCriteria);
        }
        Query query = Query.query(criteria).skip(skip).limit(limit)
                .with(new Sort(Sort.Direction.DESC, QuestionBlankDO.FIELD_CTIME));

        return template.find(query, clazz);
    }


    @Override
    public List<QuestionBlankDO> queryAll(String keyword, int skip, int limit) {
        Criteria criteria = new Criteria();
        if (keyword != null) {
            Criteria regex = new Criteria(QuestionBlankDO.FIELD_NAME).regex(keyword);
            Criteria regex1 = new Criteria(QuestionBlankDO.FIELD_DESCRIPTION).regex(keyword);

            criteria.orOperator(regex, regex1);
        }
        Query query = Query.query(criteria).skip(skip).limit(limit)
                .with(new Sort(Sort.Direction.DESC, QuestionBlankDO.FIELD_UTIME));

        return template.find(query, clazz);
    }


}
