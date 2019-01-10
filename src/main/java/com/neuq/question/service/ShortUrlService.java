package com.neuq.question.service;


import com.neuq.question.data.pojo.common.ShortUrlDO;

/**
 * @author yegk7
 * @since 2018/8/2 15:10
 */
public interface ShortUrlService {

    /**
     * 申请短二维码地址
     *
     * @param originUrl 原地址
     * @return 二维码信息
     */
    ShortUrlDO applyShortUrl(String originUrl);

    /**
     * 申请短二维码地址
     *
     * @param originUrl  原地址
     * @param expiration 过期时间
     * @return 二维码信息
     */
    ShortUrlDO applyShortUrl(String originUrl, long expiration);

    /**
     * 通过urlId获取原始url
     *
     * @param urlId urlId
     * @return 原始url
     */
    String getOriginalUrl(String urlId);
}
