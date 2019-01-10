package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import com.neuq.question.domain.enums.AuditStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * 大会报名记录
 *
 * @author liuhaoi
 */
@Document(collection = "activity.sign_up.record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConferenceSignUpRecordDO extends BasicDO {

    public static final String FIELD_STATUS = "status";
    public static final String FIELD_FIELDS = "fields";
    public static final String FIELD_FIELDS_ID_WITHOUT_FIELDS = "fieldId";
    public static final String FIELD_FIELDS_ID = "fields.fieldId";
    public static final String FIELD_FIELDS_VALUE_WITHOUT_FIELDS = "value";
    public static final String FIELD_FIELDS_VALUE = "fields.value";
    public static final String FIELD_FIELDS_NAME = "fields.fieldName";

    public static final String FIXED_INVITE_CODE_ID = "__invite_code";
    public static final String FIXED_VERIFY_CODE_ID = "__identify_code";
    public static final String FIXED_MOBILE_ID = "__mobile";
    public static final String FIXED_NAME_ID = "__name";

    private AuditStatus status;

    private List<FormFieldValue> fields;

    /**
     * 报名表字段id与对应值
     */
    @Data
    public static class FormFieldValue {

        private String fieldId;

        private String fieldName;

        private String value;

    }

}
