package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ExamRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ExamDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/11/1  14:50
 */
@Slf4j
@Repository
public class ExamRepositoryImpl extends AbstractMongoRepository<ExamDO> implements ExamRepository {

    public ExamRepositoryImpl(MongoTemplate template) {
        super(ExamDO.class, template);
    }

    @Override
    public ExamDO queryById(String examId) {

        Criteria criteria = Criteria.where(ExamDO.FIELD_ID).is(examId);
        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public void save(ExamDO examDO) {

        examDO.setCtime(System.currentTimeMillis());
        examDO.setUtime(System.currentTimeMillis());
        template.save(examDO);

        String id = examDO.getId();
        Criteria criteria = Criteria.where(ExamDO.FIELD_ID).is(id);
        String url = examDO.getExamUrl() + "?examId=" + id;
        Update update = Update.update(ExamDO.FIELD_EXAM_URL, url);

        template.findAndModify(Query.query(criteria), update, clazz);
    }

    @Override
    public long update(String examId, ExamDO examDO) {

        Criteria criteria = Criteria.where(ExamDO.FIELD_ID).is(examId);

        String url = examDO.getExamUrl() + "?examId=" + examId;

        examDO.setExamUrl(url);

        Update update = Update.update(ExamDO.FIELD_START_TIME, examDO.getStartTime())
                .set(ExamDO.FIELD_END_TIME, examDO.getEndTime())
                .set(ExamDO.FIELD_EXAM_TYPE, examDO.getExamType())
                .set(ExamDO.FIELD_NAME, examDO.getName())
                .set(ExamDO.FIELD_MULTIPLE_QUESTION_COUNT, examDO.getMultipleQuestionCount())
                .set(ExamDO.FIELD_SINGLE_QUESTION_COUNT, examDO.getSingleQuestionCount())
                .set(ExamDO.FIELD_ORDER, examDO.getOrder())
                .set(ExamDO.FIELD_EXAM_URL, examDO.getExamUrl())
                .set(ExamDO.FIELD_INTERVAL, examDO.getInterval())
                .set(ExamDO.FIELD_EXAM_REGULATION, examDO.getExamRegulation())
                .set(ExamDO.FIELD_QUESTION_BLANK_ID, examDO.getQuestionBlankId())
                .set(ExamDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();

        if (n == 0) {
            log.warn("No matched examId {} found when update exam ", examDO.getId());
        }
        return n;
    }

    @Override
    public void delete(String examId) {

        Criteria criteria = Criteria.where(ExamDO.FIELD_ID).is(examId);

        template.remove(Query.query(criteria), clazz);
    }

    @Override
    public List<ExamDO> queryList(int start, int size, String keyword) {

        Criteria criteria = new Criteria();
        if (keyword != null) {
            Criteria regex = new Criteria(ExamDO.FIELD_NAME).regex(keyword);
            Criteria regex1 = new Criteria(ExamDO.FIELD_EXAM_REGULATION).regex(keyword);

            criteria.orOperator(regex, regex1);
        }
        Query query = Query.query(criteria).skip(start).limit(size);

        return template.find(query, clazz);
    }

}
