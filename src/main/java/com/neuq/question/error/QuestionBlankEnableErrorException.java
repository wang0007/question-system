package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/11/19  11:19
 */
public class QuestionBlankEnableErrorException extends ECException {

    public QuestionBlankEnableErrorException(String message) {
        super(message);
    }

    public QuestionBlankEnableErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.QUESTION_BLANK_ENABLE_ERROR;
    }
}
