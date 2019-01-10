package com.neuq.question.support.oss.domain;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DownloadFileRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.neuq.question.error.ECRemoteServiceException;
import com.neuq.question.support.FileDownloadHeaderBuilder;
import com.neuq.question.support.file.FileStorageService;
import com.neuq.question.support.oss.OssProperties;
import com.neuq.question.support.oss.start.ApplicationDirectory;
import com.neuq.question.support.oss.start.FileRemotePathPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author wangshyi
 * @date 2018/12/29  10:43
 */
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceOSSImpl implements FileStorageService {

    private static final String HTTP_PROTOCOL_PLAIN = "http";

    private final OssProperties ossProperties;

    private final OSSClientManager ossClientManager;

    private final FileRemotePathPolicy fileNamePolicy;

    private final ApplicationDirectory applicationDirectory;

    private ObjectMetadata buildDefaultMetadata() {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setExpirationTime(new Date(
                System.currentTimeMillis() + ossProperties.getExpirationDelaySecond() * 1000));
        return metadata;
    }

    @Override
    public String upload(File file, boolean withDownloadUrlPrefix) {
        return upload(file, withDownloadUrlPrefix, buildDefaultMetadata());
    }

    @Override
    public String upload(InputStream stream, boolean withDownloadUrlPrefix, String fileName) {

        ObjectMetadata objectMetadata = buildDefaultMetadata();

        objectMetadata.setContentDisposition(FileDownloadHeaderBuilder.buildDispositionHeaderValue(fileName));

        return upload(stream, FilenameUtils.getExtension(fileName), withDownloadUrlPrefix, objectMetadata);
    }


    public String upload(File file, boolean withDownloadUrlPrefix, ObjectMetadata metadata) {
        String remoteFilePath = fileNamePolicy.buildFileName(FilenameUtils.getExtension(file.getName()));

        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            throw new IllegalArgumentException("invalid ossProperties when upload to oss " + file);
        }

        OSSClient ossClient = ossClientManager.getOSSClient();
        try {
            ossClient.putObject(ossProperties.getBucketName(), remoteFilePath, file, metadata);
        } finally {
            ossClientManager.releaseOSSClient(ossClient);
        }

        if (!withDownloadUrlPrefix) {
            return remoteFilePath;
        }

        return buildDownloadUrl(remoteFilePath);
    }


    @Override
    public String upload(InputStream stream, String fileExtension, boolean withDownloadUrlPrefix) {
        return upload(stream, fileExtension, withDownloadUrlPrefix, buildDefaultMetadata());
    }


    public String upload(InputStream stream, String fileExtension, boolean withDownloadUrlPrefix,
                         ObjectMetadata metadata) {

        String remotePath = fileNamePolicy.buildFileName(fileExtension);

        return uploadToSpecifyPath(stream, remotePath, withDownloadUrlPrefix, metadata);
    }

    @Override
    public String uploadToSpecifyPath(InputStream stream, String remotePath,
                                      boolean withDownloadUrlPrefix) throws UnsupportedOperationException {
        return uploadToSpecifyPath(stream, remotePath, withDownloadUrlPrefix, buildDefaultMetadata());
    }

    public String uploadToSpecifyPath(InputStream stream, String remotePath,
                                      boolean withDownloadUrlPrefix,
                                      ObjectMetadata metadata) throws UnsupportedOperationException {

        OSSClient ossClient = ossClientManager.getOSSClient();

        try {
            ossClient.putObject(ossProperties.getBucketName(), remotePath, stream, metadata);
        } finally {
            ossClientManager.releaseOSSClient(ossClient);
        }

        if (!withDownloadUrlPrefix) {
            return remotePath;
        }

        return buildDownloadUrl(remotePath);
    }

    @Override
    public String buildDownloadUrl(String remotePath) {

        if (remotePath.startsWith(HTTP_PROTOCOL_PLAIN)) {
            return remotePath;
        }
        return ossProperties.concatURL(ossProperties.getDownloadUrlPrefix(), remotePath);
    }

    @Override
    public byte[] downloadFile(String remotePath) {
        try {
            return FileStorageService.super.downloadFile(remotePath);
        } catch (Exception e) {
            return downloadFileByOssClient(remotePath);
        }
    }

    /**
     * 使用ossClient从远程下载文件
     *
     * @param remotePath 远程路径
     * @return 文件的byte数组
     */
    private byte[] downloadFileByOssClient(String remotePath) {

        File tempFolder = applicationDirectory.getTempDirectory();

        String tempPath = tempFolder.getAbsolutePath() + File.separator + UUID.randomUUID().toString();

        String key = remotePath;
        if (remotePath.startsWith(HTTP_PROTOCOL_PLAIN)) {
            key = remotePath.substring(remotePath.indexOf('/', 8) + 1);
        }

        DownloadFileRequest request = new DownloadFileRequest(ossProperties.getBucketName(), key,
                tempPath, 1024 * 100);

        OSSClient ossClient = ossClientManager.getOSSClient();

        try {
            ossClient.downloadFile(request);
        } catch (Throwable e) {
            throw new ECRemoteServiceException("read ossProperties from oss failed", e);
        } finally {
            ossClientManager.releaseOSSClient(ossClient);
        }

        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(new FileInputStream(tempPath));
        } catch (IOException e) {
            throw new ECRemoteServiceException("read ossProperties from local failed", e);
        }

        FileUtils.deleteQuietly(new File(tempPath));
        return bytes;

    }


}

