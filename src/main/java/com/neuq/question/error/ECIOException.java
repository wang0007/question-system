package com.neuq.question.error;

import java.io.IOException;

/**
 * @author wangshyi
 * @date 2018/12/27  13:57
 */
public class ECIOException extends ECException {

    public ECIOException(String message) {
        super(message);
    }

    public ECIOException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.IO_ERROR;
    }
}

