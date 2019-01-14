package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/11/3 10:15
 */
public class TimeExpiredException extends ECException {
    public TimeExpiredException(String message) {
        super(message);
    }

    public TimeExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.SIGN_UP_TIME_EXPIRED_ERROR;
    }
}
