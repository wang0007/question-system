package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2018/12/28  11:04
 */
public class ECInterruptException extends ECException {
    public ECInterruptException(String message) {
        super(message);
    }

    public ECInterruptException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.INTERRUPTED;
    }
}
