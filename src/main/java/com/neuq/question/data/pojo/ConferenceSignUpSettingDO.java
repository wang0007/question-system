package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 大会报名设置
 *
 * @author wangshyi
 */
@Document(collection = "activity.sign_up.setting")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConferenceSignUpSettingDO extends BasicDO {

    public static final String FIELD_CLOSING_TIME = "closingTime";
    public static final String FIELD_START_TIME = "startTime";
    public static final String FIELD_NEED_INVITATION_CODE = "needInvitationCode";

    private Long closingTime;

    private Long startTime;

    private Boolean needInvitationCode;

}