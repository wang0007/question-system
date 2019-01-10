package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/12/6  16:43
 */
public class AnswerNotExistException extends ECException {

    public AnswerNotExistException(String message) {
        super(message);
    }

    public AnswerNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.ANSWER_NOT_EXIST_ERROR;
    }
}
