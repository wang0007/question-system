package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.AbstractActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author wangshyi
 */
@Document(collection = "ai.face.activity.setting")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AiFaceActivitySettingDO extends AbstractActivity {

    public static final String FIELD_RECOGNIZE_NOTIFY_SETTING = "recognizeNotifySetting";

    public static final String FIELD_RECOGNIZE_NOTIFY_SETTING_ENABLE = "recognizeNotifySetting.enable";

    public static final String FIELD_RECOGNIZE_NOTIFY_SETTING_SUCCEED_MESSAGE = "recognizeNotifySetting.succeedMessageTemplate";

    public static final String FIELD_RECOGNIZE_NOTIFY_SETTING_FAILED_MESSAGE = "recognizeNotifySetting.failedMessage";

    public static final String FIELD_RECOGNIZE_NOTIFY_SETTING_DUPLICATE_MESSAGE = "recognizeNotifySetting.duplicateMessageTemplate";

    private RecognizeNotifySetting recognizeNotifySetting;

    @Data
    public static class RecognizeNotifySetting {

        private Boolean enable;

        private String succeedMessageTemplate;

        private String failedMessage;

        private String duplicateMessageTemplate;

    }


}
