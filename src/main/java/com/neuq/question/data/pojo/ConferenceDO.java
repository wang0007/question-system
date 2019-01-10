package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

/**
 * @author liuhaoi
 */
@Document(collection = "conference")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldNameConstants
public class ConferenceDO extends BasicDO {

    private String creator;

    private String name;

    private Date startTime;

    private Date endTime;

    private String topic;

    private String image;

    /**
     * 背景图缩略图
     */
    private String thumbImage;

    private Boolean enable;

    /**
     * 帮他签到角色
     */
    private Set<String> helpSignInRoles;

    @Override
    public String getConferenceId() {
        return id;
    }

    public boolean enable() {
        return enable != null && enable;
    }

}
