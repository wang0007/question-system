package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.ExamResultRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ExamResultDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author wangshyi
 * @date 2018/11/15  12:06
 */
@Slf4j
@Repository
public class ExamResultRepositoryImpl extends AbstractMongoRepository<ExamResultDO> implements ExamResultRepository {

    public ExamResultRepositoryImpl(MongoTemplate template) {
        super(ExamResultDO.class, template);
    }

    @Override
    public ExamResultDO queryExamResult(String examId, String memberId, String sequence) {

        Criteria criteria = Criteria.where(ExamResultDO.FIELD_EXAM_ID).is(examId)
                .and(ExamResultDO.EXAM_RESULT_MEMBER_ID).is(memberId)
                .and(ExamResultDO.FIELD_SEQUENCE).is(sequence);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void saveExamResult(ExamResultDO examResultDO) {

        examResultDO.setCtime(System.currentTimeMillis());
        examResultDO.setUtime(System.currentTimeMillis());

        template.save(examResultDO);
    }

    @Override
    public long updateExamResult(ExamResultDO examResultDO) {
        Criteria criteria = Criteria.where(ExamResultDO.FIELD_EXAM_ID).is(examResultDO.getExamId())
                .and(ExamResultDO.EXAM_RESULT_MEMBER_ID).is(examResultDO.getUser().getMemberId())
                .and(ExamResultDO.FIELD_SEQUENCE).is(examResultDO.getSequence());

        Update update = Update.update(ExamResultDO.FIELD_EXAM_ID, examResultDO.getExamId())
                .set(ExamResultDO.FIELD_USER, examResultDO.getUser())
                .set(ExamResultDO.FIELD_SEQUENCE, examResultDO.getSequence())
                .set(ExamResultDO.FIELD_QUESTION_IDS, examResultDO.getQuestionIds())
                .set(ExamResultDO.FIELD_TOTAL_SCORE, examResultDO.getTotalScore())
                .set(ExamResultDO.FIELD_TOTAL_TIME, examResultDO.getTotalTime())
                .set(ExamResultDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched examResultId {} found when update exam ", examResultDO.getId());
        }

        return n;

    }

    @Override
    public void deleteExamResult(String examId, String memberId, String sequence) {
        Criteria criteria = Criteria.where(ExamResultDO.FIELD_EXAM_ID).is(examId)
                .and(ExamResultDO.EXAM_RESULT_MEMBER_ID).is(memberId)
                .and(ExamResultDO.FIELD_SEQUENCE).is(sequence);

        template.remove(Query.query(criteria), clazz);
    }

    @Override
    public List<ExamResultDO> queryExamResultList(String examId, String memberId) {
        Criteria criteria = Criteria.where(ExamResultDO.FIELD_EXAM_ID).is(examId)
                .and(ExamResultDO.EXAM_RESULT_MEMBER_ID).is(memberId);

        return template.find(Query.query(criteria), clazz);

    }

    @Override
    public List<ExamResultDO> queryAllResult(String examId) {
        Criteria criteria = Criteria.where(ExamResultDO.FIELD_EXAM_ID).is(examId);

        return template.find(Query.query(criteria), clazz);
    }


}
