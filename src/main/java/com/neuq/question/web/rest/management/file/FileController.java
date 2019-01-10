package com.neuq.question.web.rest.management.file;

import com.neuq.question.support.file.util.FileNameUtils;
import com.neuq.question.support.file.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangshyi
 * @date 2019/1/7  15:05
 */
@Api(value = "文件上传到本地接口", description = "文件上传到本地接口")
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/file")
@RequiredArgsConstructor
public class FileController {


    private final ResourceLoader resourceLoader;


    @PostMapping("/upload")
    @ResponseBody
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public Map<String, String> upload1(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("[文件类型] - [{}]", file.getContentType());
        log.info("[文件名称] - [{}]", file.getOriginalFilename());
        log.info("[文件大小] - [{}]", file.getSize());

        String originalFilename = file.getOriginalFilename();
        String fileName = FileNameUtils.getFileName(originalFilename);
        String substring = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        String path = "C:/inetpub/wwwroot/files/" + substring;

        String filePath = "http://140.143.133.139/files/" + substring;
        File dest = new File(path);
        //判断文件父目录是否存在
        if (!dest.exists()) {
            dest.mkdir();
        }

        // TODO 将文件写入到指定目录,并且重命名
        file.transferTo(new File(path, fileName));
        Map<String, String> result = new HashMap<>(16);
        result.put("contentType", file.getContentType());
        result.put("fileUrl", filePath + "/" + fileName);
        result.put("fileOriginalName", file.getOriginalFilename());
        result.put("fileSize", file.getSize() + "");
        return result;
    }

    /**
     * 显示单张图片
     *
     * @return
     */
    @ApiOperation(value = "显示单张图片", notes = "显示单张图片")
    @GetMapping("show")
    public ResponseEntity showPhotos(String fileName) {

        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            String path = "C:/inetpub/files";
            return ResponseEntity.ok(resourceLoader.getResource("file:" + path + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Data
    private class FileMessage {

        private String msg;

        private String fileName;


    }
}