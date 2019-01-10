package com.neuq.question.web.rest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangshyi
 * @date 2018/12/27  16:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResult {

    /**
     * 判断是否注册或登录成功
     */
    private boolean isLogin;

    private String memberId;
}
