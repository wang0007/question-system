package com.neuq.question.data.dao.impl;


import com.neuq.question.data.dao.OperationLogRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.OperationLogDO;
import com.neuq.question.domain.enums.OperationType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/11/11 11:04
 */
@Repository
public class OperationLogRepositoryImpl extends AbstractMongoRepository<OperationLogDO> implements OperationLogRepository {
    public OperationLogRepositoryImpl(MongoTemplate template) {
        super(OperationLogDO.class, template);
    }

    @Override
    public void create(OperationLogDO operationLogDO) {
        Long ts = System.currentTimeMillis();
        operationLogDO.setCtime(ts);
        operationLogDO.setUtime(ts);

        template.save(operationLogDO);
    }

    @Override
    public List<OperationLogDO> query(String conferenceId, OperationType operationType, Long operationTime) {

        Criteria criteria = new Criteria();
        if (conferenceId != null) {
            criteria.and(OperationLogDO.FIELD_CONFERENCE_ID).is(conferenceId);
        }
        if (operationType != null) {
            criteria.and(OperationLogDO.FIELD_OPERATION_TYPE).is(operationType);
        }
        if (operationTime != null) {
            criteria.and(OperationLogDO.FIELD_OPERATION_TIME).gte(operationTime);
        }

        return template.find(Query.query(criteria), clazz);
    }
}
