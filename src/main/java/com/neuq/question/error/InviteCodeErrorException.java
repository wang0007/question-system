package com.neuq.question.error;


/**
 * @author wangshyi
 * @since  2018/11/17 14:05
 */
public class InviteCodeErrorException extends ECException {
    public InviteCodeErrorException(String message) {
        super(message);
    }

    public InviteCodeErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.INVITE_CODE_ERROR;
    }
}
