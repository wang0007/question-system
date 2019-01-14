package com.neuq.question.service;



import com.neuq.question.data.pojo.ActivityNewsFeedRecordDO;
import com.neuq.question.domain.enums.AuditStatus;

import java.util.Date;
import java.util.List;

/**
 * @author liuhaoi
 */
public interface ActivityNewsFeedService {

    List<ActivityNewsFeedRecordDO> query(String activityId, String keyword, Date startTime, Date endTime,
                                         AuditStatus status, int start, int size);


    long count(String activityId, String keyword, Date startTime, Date endTime, AuditStatus status);

    void synchronize(String activityId);


    List<ActivityNewsFeedRecordDO> incrementalPull(String activityId, long timestamp, int size);
}
