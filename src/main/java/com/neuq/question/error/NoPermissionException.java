package com.neuq.question.error;


/**
 * @author yegk7
 */
public class NoPermissionException extends ECException {

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.NO_PERMISSION_ERROR;
    }
}
