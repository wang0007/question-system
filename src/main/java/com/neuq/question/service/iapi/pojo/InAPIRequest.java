package com.neuq.question.service.iapi.pojo;

/**
 * @author wangshyi
 * @date 2019/1/11  15:29
 */

import lombok.Data;

import java.util.Map;

/**
 * @author liuhaoi
 */
@Data
public class InAPIRequest {

    /**
     * URL
     */
    private String url;

    /**
     * 参数字符串, 格式"key1=value1&key2=value2"
     */
    private String paramUrl;

    /**
     * 参数
     */
    private Map<String, String> params;

    /**
     * 完整的地址, 适合参数全部在URL上的情况, 如GET/DELETE
     *
     * @return URL
     */
    public String buildURLWithAllParam() {
        return url + "&" + paramUrl;
    }
}
