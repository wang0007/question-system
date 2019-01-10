package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.AbstractActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 * 直播设置
 *
 * @author liuhaoi
 */
@Document(collection = "activity.live.setting")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityLiveSettingDO extends AbstractActivity {

    public static final String FIELD_LIVE_URL = "liveUrl";
    public static final String FIELD_BACKGROUND = "background";
    public static final String FIELD_THUMB_BACKGROUND = "thumbBackground";
    public static final String FIELD_ENABLE = "enable";
    public static final String FIELD_START_TIME = "startTime";
    public static final String FIELD_END_TIME = "endTime";
    public static final String FIELD_LIVE_THEME = "liveTheme";

    private Date startTime;

    private Date endTime;

    /**
     * 背景图片地址
     */
    private String background;

    /**
     * 背景图片缩略图
     */
    private String thumbBackground;

    /**
     * 直播地址
     */
    private String liveUrl;

    /**
     * 直播主题
     */
    private String liveTheme;

    private Boolean enable;

    public boolean enable() {
        return enable != null && enable;
    }


}
