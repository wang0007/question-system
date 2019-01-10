package com.neuq.question.error;

import java.io.IOException;

/**
 * @author wangshyi
 * @date 2018/12/27  14:19
 */
public class ECResourceNotFoundException extends ECException {

    public ECResourceNotFoundException(String message) {
        super(message);
    }

    public ECResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.RESOURCE_NOT_FOUND;
    }

}