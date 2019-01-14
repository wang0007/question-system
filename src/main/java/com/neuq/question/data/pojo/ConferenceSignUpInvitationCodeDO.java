package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 大会邀请码
 *
 * @author wangshyi
 */
@Document(collection = "activity.sign_up.invitation.code")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConferenceSignUpInvitationCodeDO extends BasicDO {

    public static final String FIELD_CODE = "code";
    public static final String FIELD_CHANNEL = "channel";
    public static final String FIELD_ENABLE = "enable";

    private String code;

    /**
     * 是否为自定义
     */
    private Boolean customizable;

    /**
     * 邀请码渠道
     */
    private String channel;

    /**
     * 是否启用
     */
    private Boolean enable;

    public boolean enable() {
        return enable != null && enable;
    }


}
