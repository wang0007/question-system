package com.neuq.question.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wangshyi
 * @date 2018/12/28  15:28
 */
public interface InAPISmsService {


    /**
     * 发送短信验证码
     */
    public void sendSms(String mobile, Map<String, String> data);
}
