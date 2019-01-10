package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2018/12/27  13:50
 */
public class ECImplementationException extends ECConfigurationException {

    public ECImplementationException(String message) {
        super(message);
    }

    public ECImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return super.getCode() + 10;
    }
}
