package com.neuq.question.error;

import java.io.IOException;

/**
 * @author wangshyi
 * @date 2018/12/27  14:18
 */

public class ECRemoteServiceException extends ECException {

    private String remoteErrorCode;

    public ECRemoteServiceException(String message) {
        super(message);
    }

    public ECRemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ECRemoteServiceException(String remoteErrorCode, String message) {
        super(message);
        this.remoteErrorCode = remoteErrorCode;
    }

    public ECRemoteServiceException(long remoteErrorCode, String message) {
        super(message);
        this.remoteErrorCode = String.valueOf(remoteErrorCode);
    }

    public ECRemoteServiceException(String remoteErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.remoteErrorCode = remoteErrorCode;
    }

    public ECRemoteServiceException(long remoteErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.remoteErrorCode = String.valueOf(remoteErrorCode);
    }

    @Override
    public int getCode() {
        return ECErrorCode.REMOTE_SERVICE;
    }

    public String getRemoteErrorCode() {
        return remoteErrorCode;
    }
}

