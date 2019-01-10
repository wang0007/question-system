package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2018/12/28  11:02
 */
public class ECIllegalStateException extends ECException {
    public ECIllegalStateException(String message) {
        super(message);
    }

    public ECIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.ILLEGAL_STATE;
    }
}
