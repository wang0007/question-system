package com.neuq.question.domain.enums;

/**
 * 签到方式
 *
 * @author wangshyi
 */

public enum SignInType {

    /**
     * 用户扫描二维码
     */
    SCAN_QR_CODE,

    /**
     * 出示二维码, 工作人员扫描
     */
    QR_CODE_CERTIFY,

    /**
     * 扫脸
     */
    FACE,

    /**
     * 帮他签到人员签到
     */
    HELP_SIGN_IN,

    /**
     * 其他
     */
    OTHER

}
