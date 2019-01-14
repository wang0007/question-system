package com.neuq.question.web.rest.pojo;

import lombok.Data;

/**
 * @author wangshyi
 * @since 2018/11/11 10:38
 */
@Data
public class NotSignInUserResult {

    private String username;

    private String mobile;

    private String email;

    private String company;

    private String department;

}
