package com.neuq.question.service.iapi.pojo;

/**
 * @author wangshyi
 * @date 2019/1/11  15:31
 */

import com.neuq.question.service.iapi.impl.InAPIResponseCheckerImpl;
import lombok.Data;

/**
 * @author liuhaoi
 */
@Data
public class InAPIResponse<T> {

    /**
     * code==0 表示请求成功
     */
    protected Integer code;

    protected Integer level;
    /**
     * 错误信息
     */
    protected String msg;

    protected T data;

    public void check() {
        check("unknown", "unknown");
    }

    public void check(String url, String param) {
        InAPIResponseCheckerImpl.check(this, 200, url, param);
    }

}
