package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.QuestionRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.QuestionDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.neuq.question.domain.enums.QuestionType.MULTIPLE;
import static com.neuq.question.domain.enums.QuestionType.SINGLE;


/**
 * @author wangshyi
 * @date 2018/11/1  19:43
 */
@Slf4j
@Repository
public class QuestionRepositoryImpl extends AbstractMongoRepository<QuestionDO> implements QuestionRepository {

    public QuestionRepositoryImpl(MongoTemplate template) {
        super(QuestionDO.class, template);
    }

    @Override
    public void save(QuestionDO questionDO) {

        questionDO.setCtime(System.currentTimeMillis());
        questionDO.setUtime(System.currentTimeMillis());

        template.save(questionDO);
    }

    @Override
    public long update(QuestionDO questionDO, String questionId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_ID).is(questionId)
                .and(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionDO.getQuestionBlankId());

        Update update = Update.update(QuestionDO.FIELD_PRIORITY, questionDO.getPriority())
                .set(QuestionDO.FIELD_QUESTION_BLANK_ID, questionDO.getQuestionBlankId())
                .set(QuestionDO.FIELD_QUESTION_TYPE, questionDO.getQuestionType())
                .set(QuestionDO.FIELD_DESCRIPTION, questionDO.getDescription())
                .set(QuestionDO.FIELD_QUESTION, questionDO.getQuestion())
                .set(QuestionDO.FIELD_ANSWER, questionDO.getAnswer())
                .set(QuestionDO.FIELD_SCORE, questionDO.getScore())
                .set(QuestionDO.FIELD_NAME, questionDO.getName())
                .set(QuestionDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched questionId {} found when update the question ", questionId);
        }
        return n;
    }

    @Override
    public QuestionDO queryById(String id, String questionBlankId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_ID).is(id)
                .and(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionBlankId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public List<String> queryAnswerById(String id, String questionBlankId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_ID).is(id)
                .and(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionBlankId);

        QuestionDO questionDO = template.findOne(Query.query(criteria), clazz);
        return questionDO == null ? new ArrayList<>() : questionDO.getAnswer();
    }

    @Override
    public void delete(String questionId, String questionBlankId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_ID).is(questionId)
                .and(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionBlankId);

        template.remove(Query.query(criteria), clazz);
    }

    @Override
    public long queryQuestionCount(String questionBlankId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionBlankId);

        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public List<QuestionDO> queryQuestionsList(String questionBlankId, String keyword, int start, int size) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionBlankId);
        if (keyword != null) {
            Criteria regex = new Criteria(QuestionDO.FIELD_NAME).regex(keyword);
            Criteria regex1 = new Criteria(QuestionDO.FIELD_DESCRIPTION).regex(keyword);

            criteria.orOperator(regex, regex1);
        }
        Query query = Query.query(criteria).skip(start).limit(size)
                .with(new Sort(Sort.Direction.DESC, QuestionDO.FIELD_UTIME));
        return template.find(query, clazz);
    }

    @Override
    public List<QuestionDO> querySingleQuestionList(String questionBlankId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionBlankId)
                .and(QuestionDO.FIELD_QUESTION_TYPE).is(SINGLE);

        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public List<QuestionDO> queryMultipleQuestionList(String questionBlankId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionBlankId)
                .and(QuestionDO.FIELD_QUESTION_TYPE).is(MULTIPLE);

        return template.find(Query.query(criteria), clazz);
    }

    @Override
    public long updateBase(QuestionDO questionDO, String questionId) {

        Criteria criteria = Criteria.where(QuestionDO.FIELD_ID).is(questionId)
                .and(QuestionDO.FIELD_QUESTION_BLANK_ID).is(questionDO.getQuestionBlankId());

        Update update = new Update().set(QuestionDO.FIELD_NAME, questionDO.getName())
                .set(QuestionDO.FIELD_DESCRIPTION, questionDO.getDescription())
                .set(QuestionDO.FIELD_SCORE, questionDO.getScore())
                .set(QuestionDO.FIELD_PRIORITY, questionDO.getPriority())
                .set(QuestionDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched questionId {} found when update the question ", questionId);
        }
        return n;
    }


}
