package com.neuq.question.service;

import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.web.rest.pojo.inAPIUserList;

import java.util.List;
import java.util.Map;

/**
 * @author wangshyi
 * @date 2018/12/28  9:50
 */
public interface InAPIUserService {

    /**
     * 获取管理员信息
     * @param memberIds 管理员列表
     * @return memberid:inapiuser
     */
    Map<String, InAPIUser> getUsersByMemberIds(List<String> memberIds);

    /**
     * 获取所有用户信息
     * @return
     */
    inAPIUserList queryAllMemberIds();


}
