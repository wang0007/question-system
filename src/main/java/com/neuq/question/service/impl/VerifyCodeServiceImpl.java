package com.neuq.question.service.impl;

import com.neuq.question.data.dao.RedisDoRepository;
import com.neuq.question.data.pojo.RedisDo;
import com.neuq.question.service.VerifyCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wangshyi
 * @date 2018/11/28 20:06
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private final StringRedisTemplate redisTemplate;

    private final RedisDoRepository redisDoRepository;

    @Override
    public void saveToRedis(String key, String verifyCode) {
        RedisDo redisDo = new RedisDo();
        redisDo.setKey(key);
        redisDo.setValue(verifyCode);
        RedisDo redisDo1 = redisDoRepository.queryByKey(key);
        if (redisDo1 == null){


            redisDoRepository.saveDo(redisDo);
            return;
        }

        redisDoRepository.update(redisDo);


    }

    @Override
    public Boolean verifyCodeMatch(String key, String verifyCode) {

        boolean equals = false;
        RedisDo redisDo = new RedisDo();
        redisDo.setKey(key);
        redisDo.setValue(verifyCode);
        RedisDo redisDo1 = redisDoRepository.queryByKey(key);

        String value = redisDo.getValue();
        if (value != null && value.equals(verifyCode)) {
            equals = true;
        }

        return equals;
    }
}
