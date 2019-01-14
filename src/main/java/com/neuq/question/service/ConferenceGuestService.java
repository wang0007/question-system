package com.neuq.question.service;


import com.neuq.question.web.rest.pojo.ExcelImportResult;

/**
 * @author wangshyi
 */
public interface ConferenceGuestService {


    /**
     * 导出excel数据，转换为list存入数据库
     *
     * @param bytes         文件数据
     * @param conferenceId  大会ID
     * @return 报名记录表
     */
    ExcelImportResult importFromExcel(byte[] bytes, String conferenceId);

}
