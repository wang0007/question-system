package com.neuq.question.error;

import org.apache.poi.UnsupportedFileFormatException;

/**
 * @author wangshyi
 * @date 2018/12/27  13:57
 */
public class ECIllegalArgumentException extends ECException {

    public ECIllegalArgumentException(String message) {
        super(message);
    }

    public ECIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.ILLEGAL_ARGUMENT;
    }



}
