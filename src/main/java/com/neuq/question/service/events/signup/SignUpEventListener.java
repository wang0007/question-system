package com.neuq.question.service.events.signup;


import com.neuq.question.service.events.signup.pojo.SignUpEvent;

/**
 * 报名事件监听接口
 *
 * @author wangshyi
 */
public interface SignUpEventListener {

    /**
     * 报名事件监听方法
     *
     * @param event 报名事件对象
     */
    void onSignUp(SignUpEvent event);
}
