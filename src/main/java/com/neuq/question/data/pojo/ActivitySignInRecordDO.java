package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.AbstractActivity;
import com.neuq.question.domain.enums.SignInType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 签到记录
 *
 * @author wangshyi
 */
@Document(collection = "activity.sign_in.record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivitySignInRecordDO extends AbstractActivity {

    public static final String FIELD_USER = "user";
    public static final String FIELD_USER_QZ_ID = "user.qzId";
    public static final String FIELD_USER_MEMBER_ID = "user.memberId";
    public static final String FIELD_USER_NAME = "user.name";
    public static final String FIELD_USER_PHONE = "user.mobile";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_STAFF_MEMBER_ID = "staffMemberId";

    private InAPIUser user;

    private SignInType type;

    private Long timestamp;

    /**
     * 序号
     */
    private Integer sequence;

    /**
     * 工作人员memberID
     */
    private String staffMemberId;


}
