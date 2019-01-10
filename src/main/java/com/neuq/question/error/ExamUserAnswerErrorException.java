package com.neuq.question.error;


/**
 * @author wangshyi
 * @date 2018/11/19  11:19
 */
public class ExamUserAnswerErrorException extends ECException {

    public ExamUserAnswerErrorException(String message) {
        super(message);
    }

    public ExamUserAnswerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ErrorCode.EXAM_USER_ANSWER_ERROR;
    }
}
