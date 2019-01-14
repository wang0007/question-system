package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.AbstractActivity;
import com.neuq.question.domain.enums.LotteryScope;
import com.neuq.question.domain.enums.LotteryScreenType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * 大会报名设置
 *
 * @author wangshyi
 */
@Document(collection = "activity.lottery.setting")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityLotterySettingDO extends AbstractActivity {

    public static final String FILED_CAST_SCREEN = "projection";
    public static final String FILED_SCOPE_SETTING = "scopeSetting";
    public static final String FILED_WINNER_MESSAGE_TEMPLATE = "winnerMessageTemplate";
    public static final String FILED_BLACK_LIST = "blacklist";
    public static final String FILED_PRIZES = "prizes";
    public static final String FIELD_PRIZES_ID = "prizes.prizeId";
    public static final String FIELD_NAME_PRIZES_ID = "prizeId";
    public static final String FIELD_PRIZE_NAME = "prizes.$.prizeName";
    public static final String FIELD_PRIZE_PRIZE = "prizes.$.prize";
    public static final String FIELD_PRIZE_IMAGE = "prizes.$.image";
    public static final String FIELD_PRIZE_AMOUNT = "prizes.$.amount";
    public static final String FIELD_PRIZE_BLACKLIST = "prizes.$.blacklist";
    public static final String FIELD_PRIZE_WHITELIST = "prizes.$.whitelist";

    /**
     * 抽奖投屏
     */
    private Projection projection;

    /**
     * 范围设置
     */
    private ScopeSetting scopeSetting;

    /**
     * 中奖消息
     */
    private String winnerMessageTemplate;

    /**
     * 全局用户黑名单
     */
    private List<String> blacklist;

    /**
     * 奖品列表
     */
    private List<Prize> prizes;


    @Data
    public static class Projection {

        private LotteryScreenType projectionType;

        private String projectionBackground;
        /**
         * 背景图缩略图
         */
        private String thumbBackground;
    }

    @Data
    public static class ScopeSetting {

        private LotteryScope scope;

        private String qzId;

        private String deptId;

    }

    @Data
    public static class Prize {

        private String prizeId;

        /**
         * 奖项
         */
        private String prizeName;

        /**
         * 奖品
         */
        private String prize;

        /**
         * 奖品图片地址
         */
        private String image;

        /**
         * 奖品总数量
         */
        private Integer amount;

        /**
         * 奖品用户黑名单
         */
        private List<String> blacklist;

        /**
         * 奖品用户白名单
         */
        private List<String> whitelist;

    }


}
