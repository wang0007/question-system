package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2019/1/3  15:49
 */
public class UserNameNotExistException extends ECException {


    public UserNameNotExistException(String message) {
        super(message);
    }

    public UserNameNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.USER_NOT_EXIST_ERROR;
    }
}