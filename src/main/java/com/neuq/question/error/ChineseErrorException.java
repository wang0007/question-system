package com.neuq.question.error;


/**
 * @author wangshyi
 */
public class ChineseErrorException extends ECException {
    public ChineseErrorException(String message) {
        super(message);
    }

    public ChineseErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.CHINESE_ERROR;
    }
}
