package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.BasicDO;
import com.neuq.question.domain.enums.BarrageSize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 互动弹幕设置
 *
 * @author yegk7
 * @since 2018/8/5 15:35
 */
@Document(collection = "activity.barrage.setting")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityBarrageSettingDO extends BasicDO {

    public final static String FIELD_BARRAGE_SIZE = "barrageSize";
    public final static String FIELD_BARRAGE_SPEED = "barrageSpeed";
    public final static String FIELD_BACKGROUND_URL = "backgroundUrl";
    public final static String FIELD_THUMB_BACKGROUND = "thumbBackground";
    public final static String FIELD_ENTER_FREQUENCY = "enterFrequency";
    public final static String FIELD_CIRCULATION = "circulation";
    public final static String FIELD_FULL_SCREEN = "fullScreen";
    public final static String FIELD_CLEAR_BARRAGE = "clearBarrage";
    public final static int FIELD_PARTITION = 8;
    /**
     * 弹幕字体大小
     */
    private BarrageSize barrageSize;

    /**
     * 弹幕速度
     */
    private Integer barrageSpeed;

    private String backgroundUrl;

    private String thumbBackground;

    /**
     * 是否清空弹幕
     */
    private Boolean clearBarrage;

    /**
     * 多少毫秒允许进入一个弹幕
     */
    private Integer enterFrequency;

    /**
     * 无数据时是否自动循环
     */
    private Boolean circulation;

    /**
     * 是否全屏
     */
    private Boolean fullScreen;

}
