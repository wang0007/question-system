package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/12/6  19:04
 */
public class QuestionQuantityException extends ECException {

    public QuestionQuantityException(String message) {
        super(message);
    }

    public QuestionQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.QUESTION_QUANTITY_ERROR;
    }
}
