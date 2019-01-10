package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2018/12/27  19:22
 */
public class ECUnauthorizedException extends ECException {

    public ECUnauthorizedException(String message) {
        super(message);
    }

    public ECUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.UNAUTHORIZED;
    }

}
