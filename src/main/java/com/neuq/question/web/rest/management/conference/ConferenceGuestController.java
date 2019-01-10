package com.neuq.question.web.rest.management.conference;


import com.neuq.question.data.dao.ConferenceGuestRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceGuestDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.error.ECIOException;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.service.ConferenceGuestService;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.support.FileDownloadHeaderBuilder;
import com.neuq.question.support.file.FileStorageService;
import com.neuq.question.web.rest.pojo.ExcelImportResult;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 参会人员管理
 *
 * @author yegk7
 * @since 2018/8/1 18:05
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/guest")
@RequiredArgsConstructor
@Api(value = "参会人员管理", description = "参会人员管理")
public class ConferenceGuestController {

    private final ConferenceGuestService conferenceGuestService;

    private final ConferenceGuestRepository conferenceGuestRepository;

    private final FileStorageService fileStorageService;

    private final MessageSource messageSource;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    private final static String UPDATE_GUEST = "update conference guest,guestId=%s,guestDetail=%S";

    /**
     * 新增参会人员
     *
     * @param conferenceId              大会ID
     * @param conferenceParticipantsDTO 参会人员信息
     * @return 参会人员信息
     */
    @ApiOperation(value = "新增参会人员", notes = "新增参会人员")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceGuestDO insert(@PathVariable("conferenceId") String conferenceId,
                                    @RequestParam("memberId") String memberId,
                                    @RequestBody ConferenceParticipantsDTO conferenceParticipantsDTO) {

        ConferenceGuestDO conferenceGuestDO = new ConferenceGuestDO();

        conferenceGuestDO.setCompany(conferenceParticipantsDTO.getCompany());
        conferenceGuestDO.setEmail(conferenceParticipantsDTO.getEmail());
        conferenceGuestDO.setMobile(conferenceParticipantsDTO.getMobile());
        conferenceGuestDO.setName(conferenceParticipantsDTO.getName());
        conferenceGuestDO.setPosition(conferenceParticipantsDTO.getPosition());

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        conferenceGuestRepository.upsert(conferenceGuestDO, conferenceId);

        operationLogService.createOperationLog(inAPIUser, OperationType.ADD, conferenceId,
                null, conferenceGuestDO.getUtime(), "add conference guest" + conferenceGuestDO.toString());

        return conferenceGuestDO;
    }

    /**
     * 获取参会人员列表
     *
     * @param conferenceId 大会ID
     * @param start        起始页
     * @param size         大小
     * @return 参会人员列表
     */
    @ApiOperation(value = "获取参会人员列表", notes = "获取参会人员列表")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<ConferenceGuestDO> list(@PathVariable("conferenceId") String conferenceId,
                                              @RequestParam(defaultValue = "0") int start,
                                              @RequestParam(defaultValue = "20") int size) {

        List<ConferenceGuestDO> guestDOList = conferenceGuestRepository.list(conferenceId, start, size);

        long count = conferenceGuestRepository.count(conferenceId);
        return new PageResult<>(guestDOList, count);
    }

    /**
     * 更新参会人员信息
     *
     * @param conferenceId              大会ID
     * @param guestId                   参会人员ID
     * @param conferenceParticipantsDTO 参会人员信息
     * @return 参会人员信息
     */
    @ApiOperation(value = "更新参会人员信息", notes = "更新参会人员信息")
    @PutMapping(value = "/{guestId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceParticipantsDTO update(@PathVariable("conferenceId") String conferenceId,
                                            @RequestParam("memberId") String memberId,
                                            @PathVariable("guestId") String guestId,
                                            @RequestBody ConferenceParticipantsDTO conferenceParticipantsDTO) {

        conferenceGuestRepository.update(conferenceParticipantsDTO, guestId, conferenceId);
        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, System.currentTimeMillis(), String.format(UPDATE_GUEST, guestId, conferenceParticipantsDTO.toString()));

        return conferenceParticipantsDTO;
    }

    /**
     * 删除参会人员信息
     *
     * @param conferenceId 大会ID
     * @param guestId      参会人员ID
     */
    @ApiOperation(value = "删除参会人员信息", notes = "删除参会人员信息")
    @DeleteMapping(value = "/{guestId}")
    public void delete(@PathVariable("conferenceId") String conferenceId,
                       @RequestParam("memberId") String memberId,
                       @PathVariable("guestId") String guestId) {

        conferenceGuestRepository.delete(guestId, conferenceId);
        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        operationLogService.createOperationLog(inAPIUser, OperationType.DELETE, conferenceId,
                null, System.currentTimeMillis(), "delete conference guest,guestId=" + guestId);
    }

    private static final String TEMPLATE_FILE_NAME = "guests-import-template.xlsx";

    @ApiOperation(value = "下载参会人员导入模板", notes = "下载参会人员导入模板,conferenceId直接用任意字符串代替即可")
    @GetMapping(value = "/template", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<ClassPathResource> template(HttpServletRequest request) {

        String fileName = messageSource
                .getMessage("export_template_file_name", new Object[]{}, LocaleContextHolder.getLocale());
        Map<String, String> headerMap = FileDownloadHeaderBuilder
                .buildCompatibleDownloadFileName(request, fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headerMap);

        return new ResponseEntity<>(new ClassPathResource(TEMPLATE_FILE_NAME), headers, HttpStatus.OK);
    }


    @ApiOperation(value = "导入参会人员")
    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExcelImportResult importParticipants(@PathVariable("conferenceId") String conferenceId,
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

        ExcelImportResult result = conferenceGuestService.importFromExcel(bytes, conferenceId);

        byte[] excelData = result.getExcelData();
        if (excelData != null) {

            String fileName = messageSource.getMessage("export_result_file_name",
                    new Object[]{FilenameUtils.getExtension(originalFilename)}, LocaleContextHolder.getLocale());

            String url = fileStorageService
                    .upload(new ByteArrayInputStream(excelData), true, fileName);

            result.setExcelUrl(url);
            result.setExcelData(null);
        }

        return result;
    }


    @Data
    public static class ConferenceParticipantsDTO {

        private String name;

        private String mobile;

        private String email;

        private String company;

        private String position;

    }
}
