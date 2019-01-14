package com.neuq.question.data.dao;



import com.neuq.question.data.pojo.ActivityNewsFeedRecordDO;
import com.neuq.question.domain.enums.AuditStatus;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author wangshyi
 */
public interface ActivityNewsFeedRepository {

    ActivityNewsFeedRecordDO queryLatestItem(String activityId);

    void saveAll(Collection<ActivityNewsFeedRecordDO> records);

    List<ActivityNewsFeedRecordDO> query(String activityId, String keyword, Date startTime, Date endTime,
                                         AuditStatus status, int start, int size);

    long count(String activityId, String keyword, Date startTime, Date endTime, AuditStatus status);

    long updateStatus(String activityId, List<String> feedIds, AuditStatus status);

    List<ActivityNewsFeedRecordDO>  incrementalPull(String activityId, long timestamp, int size);


}
