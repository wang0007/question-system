package com.neuq.question.service;


import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.SignInType;
import com.neuq.question.service.events.signin.pojo.SignInEvent;

/**
 * @author yegk7
 * @since 2018/7/17 19:24
 */
public interface ActivitySignInService {

    /**
     * 活动签到
     *
     * @param user          用户信息
     * @param activityId    活动ID
     * @param type          签到类型
     * @param staffMemberId 工作人员memberId
     * @return 签到详情
     */
    SignInEvent activitySignIn(InAPIUser user, String activityId, SignInType type, String staffMemberId);
}
