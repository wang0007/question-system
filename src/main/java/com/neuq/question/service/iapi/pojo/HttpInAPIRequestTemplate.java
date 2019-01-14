package com.neuq.question.service.iapi.pojo;

/**
 * @author wangshyi
 * @date 2019/1/11  15:34
 */

/**
 * @author wangshyi
 */
public interface HttpInAPIRequestTemplate {


        <T> T doPost(String url, String body, Class<T> clazz);



        }

