package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/11/30 15:15
 */
public class MobileDuplicateErrorException extends ECException {

    public MobileDuplicateErrorException(String message) {
        super(message);
    }

    public MobileDuplicateErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.MOBILE_DUPLICATE_ERROR;
    }
}
