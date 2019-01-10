package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.AbstractActivity;
import com.neuq.question.domain.enums.AuditStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 发言上墙设置
 *
 * @author liuhaoi
 */
@CompoundIndexes({
        @CompoundIndex(name = "activityId_feedId_unique_index", def = "{'activityId' : 1, 'feed._id': 1}", unique = true, dropDups = true)
})
@Document(collection = "activity.feed.record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityNewsFeedRecordDO extends AbstractActivity {

    public static final String FIELD_FEED_ID = "feed.id";
    public static final String FIELD_FEED_TS = "feed.created";
    public static final String FIELD_FEED_CONTENT = "feed.content";
    public static final String FIELD_AUDIT_STATUS = "auditStatus";

    private InAPIUser feed;

    private AuditStatus auditStatus;

}
