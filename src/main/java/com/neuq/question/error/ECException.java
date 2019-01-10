package com.neuq.question.error;

/**
 * @author wangshyi
 * @date 2018/12/27  10:52
 */
public abstract class ECException  extends RuntimeException{
    public ECException(String message) {
        super(message);
    }

    public ECException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return 错误码
     */
    public abstract int getCode();

    /**
     * 获取http的错误code
     *
     * @return http错误码
     */
    public final int getHttpCode() {

        int code = getCode();

        if (code > 9999999) {
            return code / 100000;
        }

        if (code > 999999) {
            return code / 10000;
        }

        if (code > 99999) {
            return code / 1000;
        }

        if (code > 9999) {
            return code / 100;
        }

        if (code > 1000) {
            return code / 10;
        }
        return code;
    }
}
