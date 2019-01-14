package com.neuq.question.web.rest.pojo;

import lombok.Data;

/**
 * @author wangshyi
 */
@Data
public class ExcelImportResult {

    private Integer succeed;

    private Integer failed;

    /**
     * 失败的excel结果
     */
    private String excelUrl;

    private byte[] excelData;

    public void successOnce() {
        succeed++;
    }

    public void failedOnce() {
        failed++;
    }

}
