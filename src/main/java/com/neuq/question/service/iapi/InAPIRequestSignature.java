package com.neuq.question.service.iapi;

/**
 * @author wangshyi
 * @date 2019/1/11  15:28
 */


import com.neuq.question.service.iapi.pojo.InAPIRequest;

import java.util.Map;

/**
 * @author liuhaoi
 */
public interface InAPIRequestSignature {


    /**
     * 对请求的URL进行签名
     *
     * @param url    请求的URL
     * @param params 参数
     * @return 请求构造器
     */
    InAPIRequest sign(String url, Map<String, String> params);



}
