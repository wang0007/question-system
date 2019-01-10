package com.neuq.question.service.impl;

import com.neuq.question.service.InAPISmsService;
import com.neuq.question.support.HttpUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangshyi
 * @date 2018/12/28  15:31
 */
@Service
public class InAPISmsServiceImpl implements InAPISmsService {
    @Override
    public void sendSms(String mobile, Map<String, String> data) {

        String host = "http://cowsms.market.alicloudapi.com";
        String path = "/intf/smsapi";
        String method = "GET";
        String appcode = "1ac37c5edee14415b0b398d60ffba937";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("paras", data.get("verificationCode"));
        querys.put("sign", "消息通");
        querys.put("tpid", "001");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpUtils.doGet(host, path, method, headers, querys);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

