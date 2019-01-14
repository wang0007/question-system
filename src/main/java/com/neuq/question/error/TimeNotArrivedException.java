package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/11/3 10:22
 */
public class TimeNotArrivedException extends ECException {
    public TimeNotArrivedException(String message) {
        super(message);
    }

    public TimeNotArrivedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.SIGN_UP_TIME_NOT_ARRIVED_ERROR;
    }
}
