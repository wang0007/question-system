package com.neuq.question.service;

/**
 * @author wangshyi
 * @date 2019/1/11  15:23
 */

import com.neuq.question.data.pojo.InAPINewFeedItem;

import java.util.Date;
import java.util.List;

/**
 * @author liuhaoi
 */
public interface InAPINewsFeedRestService {


    /**
     * 拉取动态列表
     *
     * @param startFeedId 起始动态ID
     * @param startDate   起始时间
     * @param limit       拉取的数量,最大只能100
     * @return 动态列表
     */
    List<InAPINewFeedItem> queryNewsFeed(String startFeedId, Date startDate, int limit);


}

