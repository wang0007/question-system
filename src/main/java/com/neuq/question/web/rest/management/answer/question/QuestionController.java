package com.neuq.question.web.rest.management.answer.question;

import com.neuq.question.data.dao.QuestionRepository;
import com.neuq.question.data.pojo.QuestionDO;
import com.neuq.question.domain.enums.QuestionType;
import com.neuq.question.error.AnswerNotExistException;
import com.neuq.question.error.ArgsNullException;
import com.neuq.question.error.ECIOException;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.service.QuestionService;
import com.neuq.question.support.FileDownloadHeaderBuilder;
import com.neuq.question.support.file.FileStorageService;
import com.neuq.question.web.rest.pojo.ExcelImportResult;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yegk7
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "rest/v1/answer/question/{questionBlankId}")
@Api(value = "题目信息接口", description = "题目信息接口")
public class QuestionController {

    private final QuestionRepository repository;

    private final QuestionService questionService;

    private final FileStorageService fileStorageService;

    private final MessageSource messageSource;

    @ApiOperation(value = "增加题目信息", notes = "增加题目信息")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuestionDO insert(@PathVariable("questionBlankId") String questionBlankId,
                             @RequestBody QuestionDTO questionDTO,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ArgsNullException(bindingResult.getFieldError().getDefaultMessage());
        }

        boolean flag = questionService.judgeQuestionAndAnswer(questionDTO.question, questionDTO.answer);

        if (!flag) {
            throw new AnswerNotExistException("The imported topic answer does not have the corresponding option to match it.=");
        }

        QuestionDO questionDO = translateToDO(questionDTO, questionBlankId);

        repository.save(questionDO);

        return questionDO;
    }

    @ApiOperation(value = "更新题目信息", notes = "更新题目信息")
    @PutMapping(value = "/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuestionDO update(@PathVariable("questionBlankId") String questionBlankId,
                             @PathVariable("questionId") String questionId,
                             @RequestBody QuestionDTO questionDTO) {

        boolean flag = questionService.judgeQuestionAndAnswer(questionDTO.question, questionDTO.answer);

        if (!flag) {
            throw new AnswerNotExistException("The imported topic answer does not have the corresponding option to match it.");
        }

        QuestionDO questionDO = translateToDO(questionDTO, questionBlankId);
        repository.update(questionDO, questionId);
        return repository.queryById(questionId, questionBlankId);
    }

    @ApiOperation(value = "更新题目基础信息，不包括题目选项和答案", notes = "更新题目基础信息，不包括题目选项和答案")
    @PutMapping(value = "/{questionId}/base", consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuestionDO update(@PathVariable("questionBlankId") String questionBlankId,
                             @PathVariable("questionId") String questionId,
                             @Valid @RequestBody QuestionBaseDTO questionBaseDTO,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ArgsNullException(bindingResult.getFieldError().getDefaultMessage());
        }

        QuestionDO questionDO = translateToBaseDO(questionBaseDTO, questionBlankId);
        repository.updateBase(questionDO, questionId);
        return repository.queryById(questionId, questionBlankId);
    }

    @ApiOperation(value = "删除题目", notes = "删除题目")
    @DeleteMapping(value = "{questionId}")
    public void delete(@PathVariable("questionBlankId") String questionBlankId,
                       @PathVariable("questionId") String questionId) {

        repository.delete(questionId, questionBlankId);
    }

    @ApiOperation(value = "根据题目id查询题目", notes = "根据题目id查询题目")
    @GetMapping(value = "{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public QuestionDO query(@PathVariable("questionBlankId") String questionBlankId,
                            @PathVariable("questionId") String questionId) {

        return repository.queryById(questionId, questionBlankId);
    }

    @ApiOperation(value = "根据题库id查询所有满足条件的题目列表", notes = "根据题库id查询所有满足条件的题目列表")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<QuestionDO> queryQuestions(@PathVariable("questionBlankId") String questionBlankId,
                                                 @RequestParam(value = "keyword", required = false) String keyword,
                                                 @RequestParam(value = "start", defaultValue = "0") int start,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {

        List<QuestionDO> list = repository.queryQuestionsList(questionBlankId, keyword, start, size);

        long total = repository.queryQuestionCount(questionBlankId);

        return new PageResult<>(list, total);
    }

    private static final String TEMPLATE_FILE_NAME = "question-import-template.xlsx";

    @ApiOperation(value = "下载题目导入模板", notes = "下载题目导入模板")
    @GetMapping(value = "/template", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<ClassPathResource> template(HttpServletRequest request) {

        String fileName = messageSource
                .getMessage("export_question_template_file_name", new Object[]{}, LocaleContextHolder.getLocale());
        Map<String, String> headerMap = FileDownloadHeaderBuilder
                .buildCompatibleDownloadFileName(request, fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headerMap);

        return new ResponseEntity<>(new ClassPathResource(TEMPLATE_FILE_NAME), headers, HttpStatus.OK);
    }


    @ApiOperation(value = "导入Excel中所有题目", notes = "导入Excel中所有题目")
    @PostMapping(value = "import", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExcelImportResult importQuestion(@PathVariable("questionBlankId") String questionBlankId,
                                            MultipartHttpServletRequest request) {

        Collection<MultipartFile> files = request.getFileMap().values();

        if (files.isEmpty()) {
            throw new ECIllegalArgumentException("Excel file needed");
        } else if (files.size() > 1) {
            throw new ECIllegalArgumentException("Excel file only need one");
        }
        MultipartFile multipartFile = files.stream().findAny().get();

        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new ECIOException("IOException when read upload files", e);
        }

        if (bytes.length == 0) {
            throw new ECIllegalArgumentException("Invalid file with 0 byte");
        }

        String originalFilename = multipartFile.getOriginalFilename();

        ExcelImportResult result = questionService.importQuestionFromExcel(bytes, questionBlankId);

        byte[] excelData = result.getExcelData();
        if (excelData != null) {

            String fileName = messageSource.getMessage("export_question_result_file_name",
                    new Object[]{FilenameUtils.getExtension(originalFilename)}, LocaleContextHolder.getLocale());

            String url = fileStorageService
                    .upload(new ByteArrayInputStream(excelData), true, fileName);

            result.setExcelUrl(url);
            result.setExcelData(null);
        }

        return result;
    }

    /**
     * 将传入的基础信息转换为题目信息
     *
     * @param questionBaseDTO 题目基础信息
     * @param questionBlankId 题库Id
     * @return QuestionDO
     */
    private QuestionDO translateToBaseDO(QuestionBaseDTO questionBaseDTO, String questionBlankId) {

        QuestionDO questionDO = new QuestionDO();

        BeanUtils.copyProperties(questionBaseDTO, questionDO);

        questionDO.setQuestionBlankId(questionBlankId);

        return questionDO;
    }

    /**
     * 将传入的题目转换成DO存入数据库，主要是给问题选项设置ID，然后将问题答案对应的id存储
     *
     * @param questionDTO     传入的题目信息
     * @param questionBlankId 题库ID
     * @return QuestionDO
     */
    private QuestionDO translateToDO(QuestionDTO questionDTO, String questionBlankId) {

        QuestionDO questionDO = new QuestionDO();

        BeanUtils.copyProperties(questionDTO, questionDO);

        questionDO.setQuestionBlankId(questionBlankId);
        questionDO.setQuestion(handleQuestionMap(questionDTO.getQuestion()));
        questionDO.setAnswer(questionService.getAnswerIds(questionDTO.getAnswer(), questionDO.getQuestion()));

        return questionDO;
    }

    /**
     * 生成问题选项id与问题选项一一对应存入map
     *
     * @param answers 问题选项列表
     * @return 问题id与内容对于map
     */
    private Map<String, String> handleQuestionMap(List<String> answers) {

        Map<String, String> questionMap = new LinkedHashMap<>(16);

        for (String answer : answers) {
            String randomKey = RandomStringUtils.randomAlphanumeric(16);
            questionMap.put(randomKey, answer);
        }
        return questionMap;
    }

    /**
     * 题目基础信息类
     */
    @Data
    @Validated
    private static class QuestionBaseDTO {

        /**
         * 题目名称
         */
        @Valid
        @NotNull(message = "name can't be null")
        private String name;

        /**
         * 题目描述
         */
        @Valid
        @NotNull(message = "description can't be null")
        private String description;

        /**
         * 优先级
         */
        @Valid
        @NotNull(message = "priority can't be null")
        private Integer priority;

        /**
         * 分数
         */
        @Valid
        @NotNull(message = "score can't be null")
        private Integer score;

    }

    /**
     * 含答案与选项的题目实体类
     */
    @Data
    @Validated
    private static class QuestionDTO {
        /**
         * 题目类型
         */
        @Valid
        @NotNull(message = "questionType can't be null")
        private QuestionType questionType;

        /**
         * 题目名称
         */
        @Valid
        @NotNull(message = "name can't be null")
        private String name;

        /**
         * 题目描述
         */
        @Valid
        @NotNull(message = "description can't be null")
        private String description;

        /**
         * 题目的各个选项内容，前端只传答案值，id后端生成
         */
        @Valid
        @NotNull(message = "question can't be null")
        private List<String> question;

        /**
         * 优先级
         */
        @Valid
        @NotNull(message = "priority can't be null")
        private Integer priority;

        /**
         * 分数
         */
        @Valid
        @NotNull(message = "score can't be null")
        private Integer score;

        /**
         * 答案，存答案对应的值，存list便于多选查询
         */
        @Valid
        @NotNull(message = "answer can't be null")
        private List<String> answer;
    }

}
