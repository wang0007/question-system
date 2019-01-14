package com.neuq.question.service.impl;


import com.neuq.question.configuration.ApplicationProperties;
import com.neuq.question.data.dao.ShortUrlRepository;
import com.neuq.question.data.pojo.common.ShortUrlDO;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author wangshyi
 * @since 2018/11/3 9:52
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortUrlServiceImpl implements ShortUrlService {

    public static final String SHORT_URL_PREFIX = "/s/";

    private final ShortUrlRepository shortUrlRepository;

    private final ApplicationProperties properties;

    @Override
    public ShortUrlDO applyShortUrl(String originUrl) {
        return applyShortUrl(originUrl, -1);
    }

    @Override
    public ShortUrlDO applyShortUrl(String originUrl, long expiration) {

        ShortUrlDO urlDO = shortUrlRepository.queryByOriginUrl(originUrl);

        if (urlDO != null && expiration == -1 && urlDO.getExpiration() == expiration) {
            return urlDO;
        }

        urlDO = new ShortUrlDO();
        urlDO.setExpiration(expiration);
        urlDO.setOriginalUrl(originUrl);

        String randomUrlId = RandomStringUtils.randomAlphanumeric(20);
        urlDO.setUrlId(randomUrlId);
        urlDO.setShortUrl(properties.concatURL(properties.getHost(), SHORT_URL_PREFIX, randomUrlId));

        try {
            shortUrlRepository.save(urlDO);
        } catch (DuplicateKeyException | com.mongodb.DuplicateKeyException e) {
            log.warn("Duplicate Key when save short url object", e);
            return applyShortUrl(originUrl, expiration);
        }

        return urlDO;
    }


    @Override
    public String getOriginalUrl(String shortUrlId) {

        ShortUrlDO shortUrlDO = shortUrlRepository.query(shortUrlId);
        if (shortUrlDO == null) {
            throw new ECIllegalArgumentException("qr code is not exist");
        }
        return shortUrlDO.getOriginalUrl();
    }
}
