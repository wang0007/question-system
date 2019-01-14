package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ActivityAgendaRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ActivityAgendaDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author wangshyi
 */
@Slf4j
@Repository
public class ActivityAgendaRepositoryImpl extends AbstractMongoRepository<ActivityAgendaDO> implements ActivityAgendaRepository {
    public ActivityAgendaRepositoryImpl(MongoTemplate template) {
        super(ActivityAgendaDO.class, template);
    }

    @Override
    public ActivityAgendaDO queryById(String activityId) {
        Criteria criteria = Criteria.where(ActivityAgendaDO.FIELD_ACTIVITY_ID).is(activityId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public long updateWithAmPmTitle(String activityId, boolean withAmPmTitle) {
        Criteria criteria = Criteria.where(ActivityAgendaDO.FIELD_ACTIVITY_ID).is(activityId);

        Update update = Update.update(ActivityAgendaDO.FIELD_WITH_AM_PM_TITLE, withAmPmTitle);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {}  found when update activity agenda withAmPmTitle", activityId);
        }
        return n;
    }

    @Override
    public long saveAgendaItem(String activityId, ActivityAgendaDO.Agenda agenda) {
        if (StringUtils.isBlank(agenda.getAgendaId())) {
            agenda.setAgendaId(UUID.randomUUID().toString());
        }
        Criteria criteria = Criteria.where(ActivityAgendaDO.FIELD_ACTIVITY_ID).is(activityId);

        Update update = new Update().push(ActivityAgendaDO.FIELD_AGENDAS, agenda);

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {}  found when save activity agenda", activityId);
        }

        return n;
    }

    @Override
    public void save(ActivityAgendaDO agendaDO) {

        Long ts = System.currentTimeMillis();

        agendaDO.setCtime(ts);
        agendaDO.setUtime(ts);
        template.save(agendaDO);
    }

    @Override
    public long updateAgendaItem(String activityId, String itemId, ActivityAgendaDO.Agenda agenda) {
        Criteria criteria = Criteria.where(ActivityAgendaDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivityAgendaDO.FIELD_AGENDAS_ID).is(itemId);

        agenda.setAgendaId(itemId);
        Update update = new Update().set(ActivityAgendaDO.FIELD_AGENDAS + ".$", agenda);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {}  found when update activity agenda", activityId);
        }
        return n;
    }

    @Override
    public long deleteAgendaItem(String activityId, String itemId) {
        Criteria criteria = Criteria.where(ActivityAgendaDO.FIELD_ACTIVITY_ID).is(activityId);

        ActivityAgendaDO.Agenda activityAgenda = new ActivityAgendaDO.Agenda();
        activityAgenda.setAgendaId(itemId);

        Update update = new Update().pull(ActivityAgendaDO.FIELD_AGENDAS, activityAgenda);

        UpdateResult updateResult = template.updateFirst(Query.query(criteria), update, clazz);
        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched activity {}  found when delete activity agenda", activityId);
        }
        return n;
    }
}
