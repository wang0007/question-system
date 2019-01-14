package com.neuq.question.service.impl;

/**
 * @author wangshyi
 * @date 2019/1/11  15:25
 */


import com.neuq.question.data.pojo.InAPINewFeedItem;
import com.neuq.question.service.InAPINewsFeedRestService;
import com.neuq.question.service.iapi.InAPIRequestSignature;
import com.neuq.question.service.iapi.InAPIResponseChecker;
import com.neuq.question.service.iapi.pojo.HttpInAPIRequestTemplate;
import com.neuq.question.service.iapi.pojo.InAPINewFeedItemDTO;
import com.neuq.question.service.iapi.pojo.InAPINewFeedItemResponse;
import com.neuq.question.service.iapi.pojo.InAPIRequest;
import com.neuq.question.support.file.RecordTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuhaoi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InAPINewsFeedRestServiceImpl implements InAPINewsFeedRestService {

    private final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final String URL_NEWS_FEED = "/feed/getFeedWall";

    private final InAPIRequestSignature inAPIRequestSignature;

    private final InAPIResponseChecker responseChecker;

    private final HttpInAPIRequestTemplate inAPIRequestTemplate;


    @Override
    @RecordTime(errorThresholdMs = 5000)
    public List<InAPINewFeedItem> queryNewsFeed( String startFeedId, Date startDate, int limit) {

        List<InAPINewFeedItem> result = new ArrayList<>();
        int remain = limit;
        String latestFeedId = startFeedId;

        List<InAPINewFeedItem> items;
        do {
            items = doQueryWithLimit(latestFeedId, startDate, Math.min(remain, 100));
            remain = remain - items.size();
            if (items.isEmpty()) {
                break;
            }
            latestFeedId = items.get(items.size() - 1).getId();
            result.addAll(items);
        } while (remain > 0);

        return result;
    }

    private List<InAPINewFeedItem> doQueryWithLimit( String startFeedId, Date startDate, int limit) {
        Map<String, String> params = new HashMap<>(4);
        if (StringUtils.isNotBlank(startFeedId)) {
            params.put("feedId", startFeedId);
        }
        params.put("count", String.valueOf(limit));
        if (startDate != null) {
            DateTime dateTime = new DateTime(startDate);
            params.put("startDate", dateTime.toString(format));
        }

        InAPIRequest sign = inAPIRequestSignature.sign(URL_NEWS_FEED, params);
        InAPINewFeedItemResponse entity = inAPIRequestTemplate
                .doPost(sign.getUrl(), sign.getParamUrl(), InAPINewFeedItemResponse.class);

        log.info("news feed response: {}", entity);

        List<InAPINewFeedItemDTO> data = entity.getData();

        return data.stream().map(InAPINewFeedItemDTO::build).collect(Collectors.toList());
    }


}
