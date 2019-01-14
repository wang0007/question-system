package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/11/29 9:36
 */
public class VerifyCodeErrorException extends ECException {


    public VerifyCodeErrorException(String message) {
        super(message);
    }

    public VerifyCodeErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.VERIFY_CODE_ERROR;
    }
}
