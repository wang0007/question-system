package com.neuq.question.web.rest.pojo;

import lombok.Data;

/**
 * @author yegk7
 * @since 2018/8/11 10:38
 */
@Data
public class NotSignInUserResult {

    private String username;

    private String mobile;

    private String email;

    private String company;

    private String department;

}
