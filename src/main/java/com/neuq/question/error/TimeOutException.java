package com.neuq.question.error;


/**
 * @author wangshyi
 */
public class TimeOutException extends ECException {
    public TimeOutException(String message) {
        super(message);
    }

    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.TIME_OUT_ERROR;
    }
}
