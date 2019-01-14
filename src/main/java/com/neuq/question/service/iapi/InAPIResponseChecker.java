package com.neuq.question.service.iapi;

/**
 * @author wangshyi
 * @date 2019/1/11  15:30
 */


import com.neuq.question.service.iapi.pojo.InAPIRequest;
import com.neuq.question.service.iapi.pojo.InAPIResponse;
import org.springframework.http.ResponseEntity;

/**
 * @author liuhaoi
 */
public interface InAPIResponseChecker {



    /**
     * 检查REST Template的返回内容, 参照InAPI的格式, 其他接口无效
     *
     * @param response InAPI返回值
     * @param request  签名值
     */
    void check(ResponseEntity<? extends InAPIResponse> response, InAPIRequest request);

}

