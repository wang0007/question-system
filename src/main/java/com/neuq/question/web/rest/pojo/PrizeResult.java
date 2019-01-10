package com.neuq.question.web.rest.pojo;

import lombok.Data;

/**
 * @author yegk7
 * @since 2018/8/11 17:03
 */
@Data
public class PrizeResult {

    private String prizeId;

    private String prizeName;

    private String prize;

    private String image;

    private Integer amount;

    /**
     * 奖品已抽数量
     */
    private Integer lotteryAmount;
}
