package com.neuq.question.web.rest.pojo;

import lombok.Data;

@Data
public class InAPIUserDTO {
    /**
     * 用户名字
     */
    private String name;

    private String mobile;

    private String email;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 0 男 1女 2 未知 "保密"
     */
    private String sex;
}