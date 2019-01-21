package com.neuq.question.data.pojo;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author wangshyi
 * @date 2018/12/27  15:07
 */
@Data
@Document(collection = "user")
@FieldNameConstants
public class InAPIUser {

    @Id
    private String id;

    private String memberId;

    /**
     * 用户名字
     */
    private String name;

    /**
     * 登录名
     */
    private String loginName;
    /**
     * 头像
     **/
    private String avatar;

    private String password;

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


    private Boolean hasFile;

    private InAPINewFeedItem.MemberInfo memberInfo;

    private InAPINewFeedItem.Files files;
}
