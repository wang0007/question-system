package com.neuq.question.service.impl;

import com.neuq.question.service.VerifyCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author yegk7
 * @date 2018/8/28 20:06
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveToRedis(String key, String verifyCode, int time) {

        redisTemplate.opsForValue().set(key, verifyCode, time * 60, TimeUnit.SECONDS);

    }

    @Override
    public Boolean verifyCodeMatch(String key, String verifyCode) {

        boolean equals = false;
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && value.equals(verifyCode)) {
            equals = true;
        }

        return equals;
    }
}
