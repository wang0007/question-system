package com.neuq.question.error;


/**
 * @author yegk7
 * @date 2018/8/29 9:36
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
