package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.common.ShortUrlDO;

/**
 * @author yegk7
 * @since 2018/8/3 10:28
 */
public interface ShortUrlRepository {

    /**
     * 保存二维码信息
     *
     * @param shortUrlDO 二维码信息
     */
    void save(ShortUrlDO shortUrlDO);

    /**
     * 通过urlId找到原始url
     *
     * @param urlId urlId
     * @return 二维码信息
     */
    ShortUrlDO query(String urlId);


    /**
     * 根据原始URL查找
     *
     * @param url 地址
     * @return 短链接地址对象
     */
    ShortUrlDO queryByOriginUrl(String url);

}
