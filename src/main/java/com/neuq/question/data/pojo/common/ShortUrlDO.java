package com.neuq.question.data.pojo.common;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 二维码映射表
 *
 * @author wangshyi
 * @since 2018/8/2 15:12
 */
@Document(collection = "url.short")
@ToString(callSuper = true)
@Data
public class ShortUrlDO {

    public static final String FIELD_URL_ID = "urlId";

    public static final String FIELD_SHORT_URL = "shortUrl";

    public static final String FIELD_ORIGINAL_URL = "originalUrl";

    public static final String FIELD_EXPIRATION = "expiration";

    @Indexed(unique = true)
    private String urlId;

    private String shortUrl;

    @Indexed
    private String originalUrl;

    private Long ctime;

    private Long utime;

    /**
     * 失效时间， -1为永不失效
     */
    private Long expiration;

}
