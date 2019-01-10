package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.InAPIUser;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/12/27  15:30
 */

public interface UserRepsitory {

    /**
     * 注册
     */
    InAPIUser signUp(InAPIUser inAPIUser);

    /**
     * 判断是否登录成功
     * @param loginName 登录名
     * @param password 密码
     * @return 是否登录
     */
    InAPIUser isLogin(String loginName, String password);

    boolean isUserName(String loginName);

    /**
     * 修改用户信息
     * @param inAPIUser 用户信息
     */
    InAPIUser update(InAPIUser inAPIUser);

    /**
     * 通过memberid查询用户信息
     * @param memberId 用户id
     * @return 用户信息
     */
    InAPIUser queryById(String memberId);

    /**
     * 查询当前会务下所有的用户id
     * @return 用户列表
     */
    List<InAPIUser> queryAll();

}
