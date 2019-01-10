package com.neuq.question.error;


/**
 * @author yegk7
 * @date 2018/9/3 10:15
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
