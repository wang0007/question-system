package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.AbstractActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 大会报名设置
 *
 * @author liuhaoi
 */
@Document(collection = "activity.lottery.record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityLotteryRecordDO extends AbstractActivity {

    public static final String FIELD_PRIZE_ID = "prize.prizeId";

    public static final String FIELD_USER_MEMBER_ID = "user.memberId";

    public static final String FIELD_CONFIRMED = "confirmed";

    /**
     * 用户
     */
    private InAPIUser user;

    /**
     * 奖品
     */
    private ActivityLotterySettingDO.Prize prize;

    /**
     * 需要前端回传确认结果才认为抽奖完成
     */
    private Boolean confirmed;

    public boolean confirmed() {
        return confirmed != null && confirmed;
    }

}
