package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author wangshyi
 */
@Document(collection = "ai.face.conference.setting")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AiFaceConferenceSettingDO extends BasicDO {

    public static final String FIELD_GROUP_ID = "groupId";

    public static final String FIELD_REGISTRATION_NOTIFY_SETTING = "registrationNotifySetting";

    public static final String FIELD_RECOGNIZE_NOTIFY_SETTING = "recognizeNotifySetting";

    @Indexed
    private String groupId;

    private RegistrationNotifySetting registrationNotifySetting;

    /**
     * pad识别的回调地址，默认为会务地址
     */
    private String callbackUrl;

    private String failedRecognizeMessage;

    @Data
    public static class RegistrationNotifySetting {

        private Boolean enable;

        /**
         * 消息发送人
         */
        private String sender;

        private String messageTemplate;

        public boolean enable() {
            return enable != null && enable;
        }

    }


}
