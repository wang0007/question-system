package com.neuq.question.error;


/**
 * @author yegk7
 */
public class InviteCodeLengthErrorException extends ECException {
    public InviteCodeLengthErrorException(String message) {
        super(message);
    }

    public InviteCodeLengthErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.INVITE_CODE_DUPLICATE_ERROR;
    }
}