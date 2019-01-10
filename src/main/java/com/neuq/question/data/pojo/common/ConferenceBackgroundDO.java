package com.neuq.question.data.pojo.common;

import com.neuq.question.domain.enums.BackgroundType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author yegk7
 * @since 2018/7/30 18:40
 */
@EqualsAndHashCode(callSuper = true)
@Document(collection = "conference.background")
@ToString(callSuper = true)
@Data
public class ConferenceBackgroundDO extends BasicDO {

    public static final String BACK_GROUND_ID = "conference::background";
    public static final String SIGN_UP_BACK_GROUND_ID = "conference::signup::background";

    public static final String FIELD_BACK_GROUND = "background";
    public static final String FIELD_QZ_ID = "qzId";
    public static final String FIELD_BACK_GROUND_TYPE = "backgroundType";
    public static final String FIELD_BACK_GROUND_ID = "background.backgroundId";

    /**
     * 背景图以及缩略图
     */
    private List<BackgroundBO> background;


    /**
     * 背景图类型
     */
    private BackgroundType backgroundType;

    @Data
    public static class BackgroundBO {

        private String backgroundId;

        private String background;

        private String thumbBackground;
    }
}
