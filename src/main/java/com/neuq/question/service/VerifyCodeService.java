package com.neuq.question.service;

/**
 * @author yegk7
 * @date 2018/8/28 19:18
 */
public interface VerifyCodeService {

    /**
     * 将验证码存入redis
     *
     * @param key        存入redis对应的key
     * @param verifyCode 验证码
     */
    void saveToRedis(String key, String verifyCode);

    /**
     * 判断验证码是否正确
     *
     * @param key        key
     * @param verifyCode 验证码
     * @return 是否正确
     */
    Boolean verifyCodeMatch(String key, String verifyCode);

}
