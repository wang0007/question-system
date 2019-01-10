package com.neuq.question.error;


/**
 * @author yegk7
 */
public class PrincipalActivityExistException extends ECException {

    public PrincipalActivityExistException(String message) {
        super(message);
    }

    public PrincipalActivityExistException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.PRINCIPAL_ACTIVITY_EXIST;
    }
}
