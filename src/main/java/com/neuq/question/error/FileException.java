package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2019/1/7  15:14
 */
public class FileException extends ECException  {
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() { return ErrorCode.NO_PERMISSION_ERROR;
    }
}
