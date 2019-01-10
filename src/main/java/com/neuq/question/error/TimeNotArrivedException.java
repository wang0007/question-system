package com.neuq.question.error;


/**
 * @author yegk7
 * @date 2018/9/3 10:22
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
