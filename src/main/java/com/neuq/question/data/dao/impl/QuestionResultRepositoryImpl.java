package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.QuestionResultRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.QuestionResultDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author wangshyi
 * @date 2018/11/15  15:22
 */
@Slf4j
@Repository
public class QuestionResultRepositoryImpl extends AbstractMongoRepository<QuestionResultDO> implements QuestionResultRepository {

    public QuestionResultRepositoryImpl(MongoTemplate template) {
        super(QuestionResultDO.class, template);
    }

    @Override
    public QuestionResultDO queryResultById(String questionId, String memberId, String sequence) {

        Criteria criteria = Criteria.where(QuestionResultDO.FIELD_QUESTION_ID).is(questionId)
                .and(QuestionResultDO.QUESTION_RESULT_MEMBER_ID).is(memberId)
                .and(QuestionResultDO.FIELD_SEQUENCE).is(sequence);

        return template.findOne(Query.query(criteria),clazz);
    }

    @Override
    public void saveResult(QuestionResultDO questionResultDO) {

        questionResultDO.setCtime(System.currentTimeMillis());
        questionResultDO.setUtime(System.currentTimeMillis());

        template.save(questionResultDO);
    }

    @Override
    public long updateResult(QuestionResultDO questionResultDO) {

        Criteria criteria = Criteria.where(QuestionResultDO.FIELD_ID).is(questionResultDO.getId())
                .and(QuestionResultDO.FIELD_SEQUENCE).is(questionResultDO.getSequence());

        Update update = Update.update(QuestionResultDO.FIELD_USER , questionResultDO.getUser())
                .set(QuestionResultDO.FIELD_QUESTION_ID , questionResultDO.getQuestionId())
                .set(QuestionResultDO.FIELD_CHOOSE , questionResultDO.getChoose())
                .set(QuestionResultDO.FIELD_SEQUENCE,questionResultDO.getSequence())
                .set(QuestionResultDO.FIELD_COST_TIME , questionResultDO.getCostTime())
                .set(QuestionResultDO.FIELD_RIGHT_ANSWER , questionResultDO.getRightAnswer())
                .set(QuestionResultDO.FIELD_SCORE , questionResultDO.getScore())
                .set(QuestionResultDO.FIELD_UTIME , System.currentTimeMillis());


        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0){
            log.warn("No matched questionResultId {} found when update exam ", questionResultDO.getId());
        }
        return n;
    }
}
