package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.AbstractActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 发言上墙设置
 *
 * @author liuhaoi
 */
@Document(collection = "activity.feed.setting")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityNewsFeedSettingDO extends AbstractActivity {

    public static final String FIELD_BACKGROUND = "background";
    public static final String FIELD_THUMB_BACKGROUND = "thumbBackground";

    /**
     * 背景图片地址
     */
    private String background;

    /**
     * 背景图片缩略图
     */
    private String thumbBackground;

}
