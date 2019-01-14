package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.AbstractActivity;
import com.neuq.question.domain.enums.SignInScope;
import com.neuq.question.domain.enums.SignInScreenType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 大会签到设置
 *
 * @author wangshyi
 */
@Document(collection = "activity.sign_in.setting")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivitySignInSettingDO extends AbstractActivity {

    public static final String FIELD_ENABLE = "enable";
    public static final String FILED_PROJECTION = "projection";
    public static final String FIELD_NOTIFY_SETTING = "notifySetting";
    public static final String FIELD_NOTIFY_SETTING_ENABLE = "notifySetting.enable";
    public static final String FIELD_JOIN_GROUP_SETTING = "joinGroupSetting";
    public static final String FIELD_QR_CODE_SETTING = "qrCodeSetting";
    public static final String FIELD_SCOPE = "scope";

    private Boolean enable;

    /**
     * 允许签到的范围
     */
    private SignInScope scope ;

    private Projection projection;

    private NotifySetting notifySetting;

    private JoinGroupSetting joinGroupSetting;

    private QRCodeSetting qrCodeSetting;

    public boolean enable() {
        return enable != null && enable;
    }

    @Data
    public static class Projection {

        private SignInScreenType projectionType;

        private String projectionBackground;

        /**
         * 背景图缩略图
         */
        private String thumbBackground;
    }

    @Data
    public static class NotifySetting {

        private String successMessageTemplate;

        private String failureMessageTemplate;

        /**
         * 重复签到提示语
         */
        private String alreadySignInMessageTemplate;

        /**
         * 签到结果背景图
         */
        private String backgroundUrl;

        private Boolean enable;

        private String appNotifyMessageTemplate;

        /**
         * 消息链接
         */
        private String messageUrl;

        public boolean enable() {
            return enable != null && enable;
        }

    }


    @Data
    public static class JoinGroupSetting {

        private Boolean enable;

        private String joinGroupID;

        public boolean enable() {
            return enable != null && enable;
        }

    }

    @Data
    public static class QRCodeSetting {

        private Boolean enable;

        private String url;

        private String shortUrl;

        private String imageUrl;

        public boolean enable() {
            return enable != null && enable;
        }

    }

}
