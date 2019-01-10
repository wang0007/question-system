package com.neuq.question.data.dao.impl;


import com.neuq.question.data.dao.ActivityBarrageRecordRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ActivityBarrageRecordDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sunhuih
 * @date 2018/8/29  15:58
 */
@Slf4j
@Repository
public class ActivityBarrageRecordRepositoryImpl extends AbstractMongoRepository<ActivityBarrageRecordDO> implements ActivityBarrageRecordRepository {

    public ActivityBarrageRecordRepositoryImpl(MongoTemplate template) {
        super(ActivityBarrageRecordDO.class, template);
    }

    @Override
    public void deleteBarrage(String conferenceId) {

        Criteria criteria = Criteria.where(ActivityBarrageRecordDO.FIELD_CONFERENCE_ID).is(conferenceId);
        Update update = Update.update(ActivityBarrageRecordDO.FIELD_DELETED, true);
        template.updateMulti(Query.query(criteria), update, clazz);
    }

    @Override
    public void saveBarrage(ActivityBarrageRecordDO barrageDo) {

        long ts = System.currentTimeMillis();

        barrageDo.setUtime(ts);
        barrageDo.setCtime(ts);
        template.save(barrageDo);
    }

    @Override
    public List<ActivityBarrageRecordDO> queryBarrage(String conferenceId, List<Integer> partitions, Long ts) {

        Criteria criteria = Criteria.where(ActivityBarrageRecordDO.FIELD_CONFERENCE_ID).is(conferenceId)
                .and(ActivityBarrageRecordDO.FIELD_DELETED).is(false)
                .and(ActivityBarrageRecordDO.FIELD_PARTITION).in(partitions)
                .and(ActivityBarrageRecordDO.FIELD_CTIME).gt(ts);

        return template.find(Query.query(criteria).limit(2000), clazz);
    }
}
