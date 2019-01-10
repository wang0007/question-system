package com.neuq.question.web.rest.management.conference.signup;


import com.neuq.question.domain.enums.AuditStatus;
import com.neuq.question.service.FileService;
import com.neuq.question.service.SignUpRecordService;
import com.neuq.question.support.SignInRecordExporter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 报名导出接口
 *
 * @author yegk7
 * @date 2018/9/4 10:58
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/signup/export")
@RequiredArgsConstructor
@Api(value = "报名导出接口", description = "报名导出接口")
public class SignUpExportController {

    private final FileService fileService;

    private final MessageSource messageSource;

    private final SignUpRecordService signUpRecordService;

    /**
     * 报名人员导出到excel
     *
     * @param conferenceId   大会ID
     * @param auditStatuses  报名审核状态
     * @param invitationCode 邀请码
     * @param fieldId        字段Id
     * @param fieldValue     字段值
     * @return 人员excel信息
     */
    @GetMapping(value = "")
    @ApiOperation(value = "报名人员导出到excel", notes = "报名人员导出到excel")
    public ResponseEntity<byte[]> signUpRecordExport(@PathVariable("conferenceId") String conferenceId,
                                                     @RequestParam("auditStatuses[]") List<AuditStatus> auditStatuses,
                                                     @RequestParam(required = false) String invitationCode,
                                                     @RequestParam(required = false) String fieldId,
                                                     @RequestParam(required = false) String fieldValue,
                                                     HttpServletResponse response) {


        String signUpExcelName = messageSource
                .getMessage("sign_up_record_excel_name", new Object[]{}, LocaleContextHolder.getLocale());
        StringBuilder fileNameBuilder = fileService.buildFileName(conferenceId, signUpExcelName);
        // 解决中文乱码问题
        String fileName = fileNameBuilder.toString();
        try {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            //ignore
        }

        response.setHeader("Content-Disposition", "attachment;fileName* = UTF-8''" + fileName);

        List<List<Object>> dataList = signUpRecordService
                .getSignUpRecordList(conferenceId, auditStatuses, invitationCode, fieldId, fieldValue, 0, 50000);

        // fileName去掉文件名后缀
        String rowName = messageSource
                .getMessage("export_sign_up_record_row_name", new Object[]{}, LocaleContextHolder.getLocale());
        List<String> rowNameList = Arrays.asList(rowName.split(","));
        SignInRecordExporter workBookData = new SignInRecordExporter(signUpExcelName
                .substring(0, signUpExcelName.length() - 5), rowNameList, dataList);
        byte[] bytes = workBookData.exportExcel();

        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

}
