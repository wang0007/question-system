package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2018/12/28  10:46
 */
public class ECUnsupportedOperationException extends ECException {
    public ECUnsupportedOperationException(String message) {
        super(message);
    }

    public ECUnsupportedOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.UNSUPPORTED_OPERATION;
    }
}
