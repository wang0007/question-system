package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author sunhuih
 * @date 2018/8/29  15:48
 */
@Document(collection = "activity.barrage.record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityBarrageRecordDO extends BasicDO {

    public final static String FIELD_PARTITION = "partition";
    public final static String FIELD_TS = "ts";
    public final static String FIELD_CONTENT = "content";
    public final static String FIELD_DELETED = "deleted";

    /**
     * 弹幕分区
     */
    private Integer partition;

    /**
     * 弹幕时间戳
     */
    private Long ts;

    /**
     * 弹幕内容
     */
    private String content;

    /**
     * 删除标记
     */
    private Boolean deleted;

    /**
     * 用户
     */
    private InAPIUser user;

}
