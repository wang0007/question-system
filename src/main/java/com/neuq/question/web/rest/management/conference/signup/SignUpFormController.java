package com.neuq.question.web.rest.management.conference.signup;


import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.dao.SignUpFormRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.data.pojo.ConferenceSignUpFormDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.support.LocaleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 大会报名表格设置
 *
 * @author wangshyi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/signup/form")
@RequiredArgsConstructor
@Api(value = "大会报名表格设置", description = "大会报名表格设置")
public class SignUpFormController {

    private final SignUpFormRepository repository;

    private final ConferenceRepository conferenceRepository;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    /**
     * 获取大会报名表
     *
     * @param conferenceId 大会ID
     * @return 报名表详情
     */
    @ApiOperation(value = "获取大会报名表", notes = "获取大会报名表")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceSignUpFormDO getById(@PathVariable("conferenceId") String conferenceId) {

        ConferenceDO conferenceDO = conferenceRepository.queryByConferenceId(conferenceId);

        ConferenceSignUpFormDO formDO = repository.queryById(conferenceId);
        List<ConferenceSignUpFormDO.FormField> formFields = formDO.getFields()
                .stream()
                .peek(formField -> {
                    if (formField.getI18nLabel() != null && formField.getI18nLabel().get(LocaleUtil.getLocale()) != null) {
                        formField.setLabel(formField.getI18nLabel().get(LocaleUtil.getLocale()));
                    }
                })
                .collect(Collectors.toList());
        formDO.setFields(formFields);

        formDO.setFormTitle(conferenceDO.getName());

        return formDO;
    }


    /**
     * 创建大会报名表
     *
     * @param conferenceId 大会ID
     * @param form         报名表详情
     * @return 报名表详情
     */
    @ApiOperation(value = "创建大会报名表", notes = "创建大会报名表")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceSignUpFormDO create(@PathVariable("conferenceId") String conferenceId,
                                         @RequestBody ConferenceSignUpFormDTO form) {

        ConferenceSignUpFormDO formDO = new ConferenceSignUpFormDO();
        formDO.setBackgroundImage(form.getBackgroundImage());
        formDO.setThumbBackground(form.getThumbBackground());

        formDO.setConferenceId(conferenceId);

        List<ConferenceSignUpFormDO.FormField> fields = form.getFields();
        fields.forEach(field -> field.setFormFieldId(
                (StringUtils.isBlank(field.getFormFieldId())) ? UUID.randomUUID().toString() : field.getFormFieldId()));
        formDO.setFields(fields);

        repository.save(formDO);

        return formDO;
    }

    /**
     * 修改大会报名表
     *
     * @param conferenceId 大会id
     * @param form         报名表信息
     * @return 报名表详情
     */
    @ApiOperation(value = "修改大会报名表", notes = "修改大会报名表")
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceSignUpFormDO update(@PathVariable("conferenceId") String conferenceId,
                                         @RequestParam("memberId") String memberId,
                                         @RequestBody ConferenceSignUpFormDTO form) {

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        repository.update(conferenceId, form);

        ConferenceSignUpFormDO conferenceSignUpFormDO = repository.queryById(conferenceId);

        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, conferenceSignUpFormDO.getUtime(), "update conference form setting" + conferenceSignUpFormDO.toString());

        return conferenceSignUpFormDO;
    }

    /**
     * 新增大会报名表字段
     *
     * @param conferenceId 大会ID
     * @param formField    报名表字段信息
     * @return 字段信息
     */
    @ApiOperation(value = "新增大会报名表字段", notes = "新增大会报名表字段")
    @PatchMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceSignUpFormDO.FormField addField(@PathVariable("conferenceId") String conferenceId,
                                                     @RequestBody ConferenceSignUpFormDO.FormField formField) {

        repository.insertField(conferenceId, formField);
        return formField;
    }

    /**
     * 删除报名表指定字段
     *
     * @param conferenceId 大会ID
     * @param fieldId      报名表字段ID
     */
    @ApiOperation(value = "删除报名表指定字段", notes = "删除报名表指定字段")
    @DeleteMapping(value = "/field/{fieldId}")
    public void delete(@PathVariable("conferenceId") String conferenceId,
                       @PathVariable("fieldId") String fieldId) {

        repository.deleteFormField(conferenceId, fieldId);

    }

    /**
     * 获取报名表二维码
     *
     * @param conferenceId 大会ID
     * @return 报名表二维码
     */
    @ApiOperation(value = "获取报名表二维码", notes = "获取报名表二维码")
    @GetMapping(value = "/qrcode", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQRCode(@PathVariable("conferenceId") String conferenceId) {

        String qrCodeUrl = "/rest/s/%s";

        return String.format(qrCodeUrl, conferenceId);
    }

    /**
     * 大会报名表请求对象
     */
    @Data
    public static class ConferenceSignUpFormDTO {

        private String backgroundImage;

        private String thumbBackground;

        private List<ConferenceSignUpFormDO.FormField> fields;

    }

}
