package com.neuq.question.error;


/**
 * @author yegk7
 */
public class ArgsNullException extends ECException {
    public ArgsNullException(String message) {
        super(message);
    }

    public ArgsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.ARGS_NULL_ERROR;
    }
}
