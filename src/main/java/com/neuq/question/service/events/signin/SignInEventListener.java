package com.neuq.question.service.events.signin;


import com.neuq.question.service.events.signin.pojo.SignInEvent;

/**
 * 签到事件监听接口
 *
 * @author wangshyi
 */
public interface SignInEventListener {

    /**
     * 签到事件监听方法
     *
     * @param event 事件对象
     */
    void onSignIn(SignInEvent event);

}
