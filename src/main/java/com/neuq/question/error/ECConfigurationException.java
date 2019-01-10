package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2018/12/27  13:50
 */
public class ECConfigurationException extends ECException {

    public ECConfigurationException(String message) {
        super(message);
    }

    public ECConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ECErrorCode.SERVER_CONFIGURATION_ERROR;
    }

}