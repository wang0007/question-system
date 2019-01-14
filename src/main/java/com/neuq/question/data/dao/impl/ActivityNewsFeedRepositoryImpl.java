package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.neuq.question.data.dao.ActivityNewsFeedRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.ActivityNewsFeedRecordDO;
import com.neuq.question.domain.enums.AuditStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wangshyi
 * @since 2018/7/31 11:07
 */
@Slf4j
@Repository
public class ActivityNewsFeedRepositoryImpl extends AbstractMongoRepository<ActivityNewsFeedRecordDO> implements ActivityNewsFeedRepository {

    public ActivityNewsFeedRepositoryImpl(MongoTemplate template) {
        super(ActivityNewsFeedRecordDO.class, template);
    }

    @Override
    public ActivityNewsFeedRecordDO queryLatestItem(String activityId) {

        Criteria criteria = Criteria.where(ActivityNewsFeedRecordDO.FIELD_ACTIVITY_ID).is(activityId);

        Query query = Query.query(criteria);

        query.with(new Sort(Sort.Direction.DESC, ActivityNewsFeedRecordDO.FIELD_FEED_ID));
        query.limit(1);

        return template.findOne(query, clazz);
    }

    @Override
    public void saveAll(Collection<ActivityNewsFeedRecordDO> records) {

        records.stream().filter(Objects::nonNull).forEach(record -> {
            long ts = System.currentTimeMillis();
            record.setCtime(ts);
            record.setUtime(ts);
            try {
                template.insert(record);
            } catch (DuplicateKeyException e) {
                log.warn("DuplicateKeyException when save news feed with record", record);
            } catch (Exception e) {
                log.warn("Exception when save news feed with record", record);
            }
        });
    }

    @Override
    public List<ActivityNewsFeedRecordDO> query(String activityId, String keyword, Date startTime, Date endTime,
                                                AuditStatus status, int start, int size) {

        Criteria criteria = buildNewsFeedListCriteria(activityId, keyword, startTime, endTime, status);

        Query query = Query.query(criteria);

        query.with(new Sort(Sort.Direction.DESC, ActivityNewsFeedRecordDO.FIELD_FEED_ID));
        query.skip(start).limit(size);

        return template.find(query, clazz);
    }

    private Criteria buildNewsFeedListCriteria(String activityId, String keyword, Date startTime, Date endTime,
                                               AuditStatus status) {
        Criteria criteria = Criteria.where(ActivityNewsFeedRecordDO.FIELD_ACTIVITY_ID).is(activityId);

        if (status != null) {
            criteria = criteria.and(ActivityNewsFeedRecordDO.FIELD_AUDIT_STATUS).is(status);
        }

        if (StringUtils.isNotBlank(keyword)) {
            criteria = criteria.and(ActivityNewsFeedRecordDO.FIELD_FEED_CONTENT).regex(keyword);
        }

        if (startTime != null || endTime != null) {

            criteria = criteria.and(ActivityNewsFeedRecordDO.FIELD_FEED_TS);
            if (startTime != null) {
                criteria = criteria.gte(startTime);
            }
            if (endTime != null) {
                criteria = criteria.lte(endTime);
            }
        }
        return criteria;
    }

    @Override
    public long count(String activityId, String keyword, Date startTime, Date endTime, AuditStatus status) {
        Criteria criteria = buildNewsFeedListCriteria(activityId, keyword, startTime, endTime, status);

        return template.count(Query.query(criteria), clazz);
    }

    @Override
    public long updateStatus(String activityId, List<String> feedIds, AuditStatus status) {

        Criteria criteria = Criteria.where(ActivityNewsFeedRecordDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivityNewsFeedRecordDO.FIELD_FEED_ID).in(feedIds);

        Update update = Update.update(ActivityNewsFeedRecordDO.FIELD_AUDIT_STATUS, status).set(ActivityNewsFeedRecordDO.FIELD_UTIME,System.currentTimeMillis());

        UpdateResult updateResult = template.updateMulti(Query.query(criteria), update, clazz);

        return updateResult.getModifiedCount();
    }

    @Override
    public List<ActivityNewsFeedRecordDO> incrementalPull(String activityId, long timestamp, int size) {
        Criteria criteria = Criteria.where(ActivityNewsFeedRecordDO.FIELD_ACTIVITY_ID).is(activityId)
                .and(ActivityNewsFeedRecordDO.FIELD_AUDIT_STATUS).in(AuditStatus.PASS, AuditStatus.REJECT)
                .and(ActivityNewsFeedRecordDO.FIELD_UTIME).gte(timestamp);

        Query query = Query.query(criteria).limit(size)
                .with(new Sort(Sort.Direction.ASC, ActivityNewsFeedRecordDO.FIELD_FEED_TS));

        return template.find(query, clazz);
    }
}
