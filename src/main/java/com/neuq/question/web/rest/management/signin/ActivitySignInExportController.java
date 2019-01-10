package com.neuq.question.web.rest.management.signin;


import com.neuq.question.service.ActivitySignInRecordService;
import com.neuq.question.service.FileService;
import com.neuq.question.support.SignInRecordExporter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 签到导出接口
 *
 * @author yegk7
 * @since 2018/7/19 16:18
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/activity/{activityId}/signin/export")
@RequiredArgsConstructor
@Api(value = "签到导出接口", description = "签到导出接口")
public class ActivitySignInExportController {

    private final FileService fileService;

    private final MessageSource messageSource;

    private final ActivitySignInRecordService activitySignInRecordService;

    /**
     * 将已签到人员导出到excel
     *
     * @param activityId   活动ID
     * @param conferenceId 大会ID
     * @param response     包括header和数据
     * @return byte数组，浏览器会自动下载
     */
    @ApiOperation(value = "将已签到人员导出到excel", notes = "将已签到人员导出到excel")
    @GetMapping(value = "/{conferenceId}",produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> signInExportExcel(@PathVariable("activityId") String activityId,
                                                    @PathVariable("conferenceId") String conferenceId,
                                                    HttpServletResponse response) {
        String signInExcelName = messageSource
                .getMessage("sign_in_excel_name", new Object[]{}, LocaleContextHolder.getLocale());
        StringBuilder fileNameBuilder = fileService.buildFileName(conferenceId, activityId, signInExcelName);
        // 解决中文乱码问题
        String fileName = fileNameBuilder.toString();
        try {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            //ignore
        }

        response.setHeader("Content-Disposition", "attachment;fileName* = UTF-8''" + fileName);

        List<List<Object>> dataList = activitySignInRecordService.getSignInDataList(activityId, conferenceId);

        // fileName去掉文件名后缀
        String rowName = messageSource
                .getMessage("export_sign_in_record_row_name", new Object[]{}, LocaleContextHolder.getLocale());
        List<String> rowNameList = Arrays.asList(rowName.split(","));
        SignInRecordExporter workBookData = new SignInRecordExporter(signInExcelName.substring(0,
                signInExcelName.length() - 5), rowNameList, dataList);
        byte[] bytes = workBookData.exportExcel();

        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }


    /**
     * 将未签到人员导出到excel
     *
     * @param activityId   活动ID
     * @param conferenceId 大会ID
     * @param response     包括header和数据
     * @return byte数组，浏览器会自动下载
     */
    @ApiOperation(value = "将未签到人员导出到excel", notes = "将未签到人员导出到excel")
    @GetMapping(value = "/not/{conferenceId}",produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> notSignInExportExcel(@PathVariable("activityId") String activityId,
                                                       @PathVariable("conferenceId") String conferenceId,
                                                       HttpServletResponse response) {

        String signInExcelName = messageSource
                .getMessage("not_sign_in_excel_name", new Object[]{}, LocaleContextHolder.getLocale());
        StringBuilder fileNameBuilder = fileService.buildFileName(conferenceId, activityId, signInExcelName);
        // 解决中文乱码问题
        String fileName = fileNameBuilder.toString();
        try {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        response.setHeader("Content-Disposition", "attachment;fileName* = UTF-8''" + fileName);

        List<List<Object>> dataList = activitySignInRecordService.getNotSignInDataList(activityId, conferenceId);

        // fileName去掉文件名后缀
        String rowName = messageSource
                .getMessage("export_sign_in_record_row_name", new Object[]{}, LocaleContextHolder.getLocale());
        List<String> rowNameList = Arrays.asList(rowName.split(","));
        SignInRecordExporter workBookData = new SignInRecordExporter(signInExcelName.substring(0,
                signInExcelName.length() - 5), rowNameList, dataList);
        byte[] bytes = workBookData.exportExcel();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

}
