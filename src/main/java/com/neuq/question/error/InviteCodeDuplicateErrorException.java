package com.neuq.question.error;


/**
 * @author wangshyi
 */
public class InviteCodeDuplicateErrorException extends ECException {
    public InviteCodeDuplicateErrorException(String message) {
        super(message);
    }

    public InviteCodeDuplicateErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.INVITE_CODE_DUPLICATE_ERROR;
    }
}
