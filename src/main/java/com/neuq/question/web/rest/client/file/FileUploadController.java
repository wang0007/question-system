package com.neuq.question.web.rest.client.file;

import com.alibaba.fastjson.JSON;

import com.neuq.question.error.ECIOException;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.support.file.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;

/**
 * @author wangshyi
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rest/v1/client/file")
@Api(value = "文件上传接口", description = "文件上传接口")
public class FileUploadController {

    private final FileStorageService fileStorageService;


    @ApiOperation(value = "文件上传", notes = "文件上传")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FileUploadResponse uploadFile(MultipartHttpServletRequest request) {

        MultipartFile multipartFile = getSingleMultipartFile(request);

        String url;
        try {
            url = fileStorageService
                    .upload(multipartFile.getInputStream(), true, multipartFile.getOriginalFilename());
        } catch (IOException e) {
            throw new ECIOException("IOException when upload multipart file to oss", e);
        }

        return FileUploadResponse.builder().url(url).build();
    }


    @ApiOperation(value = "文件上传,返回text/html", notes = "文件上传")
    @PostMapping(value = "/compatible", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String uploadFileWithTextHtmlResponse(MultipartHttpServletRequest request) {

        FileUploadResponse fileUploadResponse = uploadFile(request);

        return JSON.toJSONString(fileUploadResponse);
    }


    @NotNull
    private MultipartFile getSingleMultipartFile(MultipartHttpServletRequest request) {
        Collection<MultipartFile> files = request.getFileMap().values();
        if (files.isEmpty()) {
            throw new ECIllegalArgumentException("Image file needed");
        }

        return files.stream().findAny().get();
    }


    @Data
    @Builder
    public static class ImageUploadResponse {

        private String originUrl;

        private String compressUrl;

        private String thumbUrl;

    }

    @Data
    @Builder
    public static class FileUploadResponse {

        private String url;

    }


}
