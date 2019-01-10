package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;


/**
 * 大会报名表格设置
 *
 * @author liuhaoi
 */
@Document(collection = "activity.sign_up.form")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConferenceSignUpFormDO extends BasicDO {

    public static final String SIGN_UP_FORM_ID = BasicDO.DEFAULT_ID;

    public static final String FIELD_BACKGROUND_IMAGE = "backgroundImage";
    public static final String FIELD_THUMB_BACKGROUND = "thumbBackground";
    public static final String FIELD_FIELDS = "fields";
    public static final String FIELD_FIELDS_ID = "fields.formFieldId";
    public static final String FIELD_FIELDS_LABEL = "fields.label";
    public static final String FIELD_FIELDS_TYPE = "fields.type";
    public static final String FIELD_FORM_TITLE = "formTitle";
    public static final String FIELD_FIELDS_PLACEHOLDER = "fields.placeHolder";
    public static final String FIELD_FIELDS_REQUIRED = "fields.required";
    public static final String FIELD_FIELDS_ORDER = "fields.order";
    public static final String FIELD_FIELDS_SELECTED = "fields.selected";

    private String backgroundImage;
    /**
     * 背景图缩略图
     */
    private String thumbBackground;

    private String formTitle;

    private List<FormField> fields;


    @Data
    public static class FormField {

        private String formFieldId;

        private Map<String, String> i18nLabel;

        private String label;

        private String placeHolder;

        private Boolean required;

        /**
         * 排列顺序
         */
        private Integer order;

        /**
         * 是否为选中状态
         */
        private Boolean selected;

        /**
         * 是否允许修改
         */
        private Boolean allowModify;
    }

}
