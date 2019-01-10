package com.neuq.question.support.file;


import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.error.ECRemoteServiceException;
import com.neuq.question.error.ECResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author wangshyi
 * @date 2018/12/27  14:16
 */
public interface FileStorageService {

    String HTTP_PROTOCOL_PLAIN = "http";

    /**
     * 上传文件
     *
     * @param file                  文件对象, 必须存在,可读且不为文件夹
     * @param withDownloadUrlPrefix 是否拼接下载URL前缀
     * @return 文件的下载地址
     * @throws IllegalArgumentException 当文件无效抛出
     */
    String upload(File file, boolean withDownloadUrlPrefix);

    /**
     * 上传文件
     *
     * @param stream                文件的输入流
     * @param withDownloadUrlPrefix 是否拼接下载URL前缀
     * @param fileName              文件名,下载时显示
     * @return 文件的下载地址
     */
    String upload(InputStream stream, boolean withDownloadUrlPrefix, String fileName);

    /**
     * 上传文件
     *
     * @param stream                文件的输入流
     * @param fileExtension         文件后缀
     * @param withDownloadUrlPrefix 是否拼接下载URL前缀
     * @return 文件的下载地址
     */
    String upload(InputStream stream, String fileExtension, boolean withDownloadUrlPrefix);

    /**
     * 上传文件到指定位置
     *
     * @param stream                文件的输入流
     * @param withDownloadUrlPrefix 是否拼接下载URL前缀
     * @param path                  文件路径
     * @return 文件的下载地址
     * @throws UnsupportedOperationException fastdfs等不支持本方法
     */
    String uploadToSpecifyPath(InputStream stream, String path,
                               boolean withDownloadUrlPrefix) throws UnsupportedOperationException;

    /**
     * 构建下载链接
     *
     * @param remotePath 远程相对路径
     * @return 完整的下载地址
     */
    String buildDownloadUrl(String remotePath);


    /**
     * 从远程下载文件
     *
     * @param remotePath 远程路径
     * @return 文件的byte数组
     */
    @RecordTime
    default byte[] downloadFile(String remotePath) {

        URL url = buildURLObject(remotePath);

        byte[] bytes;

        try (InputStream input = url.openStream()) {
            bytes = IOUtils.toByteArray(input);
        } catch (IOException e) {//如果出现异常, 尝试使用OSSClient进行下载
            throw new ECRemoteServiceException("Download File failed", e);
        }

        return bytes;
    }


    /**
     * 根据相对路径或绝对路径获取文件流
     *
     * @param remotePath 远程路径
     * @return 文件流
     */
    default InputStream downloadFileStream(String remotePath) {

        URL url = buildURLObject(remotePath);

        try {
            return url.openStream();
        } catch (IOException e) {
            String msg = "Failed open file " + remotePath + " input stream from oss service";
            throw new ECRemoteServiceException(msg, e);
        }
    }


    /**
     * 下载文件到指定路径
     *
     * @param remotePath 远程路径
     * @param folder     文件夹
     * @param fileName   文件名称
     * @return 文件在本地的完整路径
     */
    @RecordTime
    default File downloadFile(String remotePath, File folder, String fileName) {

        URL url = buildURLObject(remotePath);


        File distFile;
        try (InputStream input = url.openStream()) {
            distFile = new File(folder, fileName);
            IOUtils.copy(input, new FileOutputStream(distFile));
        } catch (IOException e) {
            throw new ECResourceNotFoundException("download ossProperties failed with remote path " + remotePath, e);
        }


        return distFile;
    }

    /**
     * 构建下载地址
     *
     * @param remotePath 下载地址
     * @return 地址对象
     * @throws ECIllegalArgumentException 地址格式不正确是抛出
     */
    default URL buildURLObject(String remotePath) {

        String realRemotePath = buildDownloadUrl(remotePath);

        URL url;
        try {
            url = new URL(realRemotePath);
        } catch (MalformedURLException e) {
            throw new ECIllegalArgumentException("Invalid OSS remote path " + realRemotePath, e);
        }
        return url;
    }

    /**
     * 下载文件到本地
     *
     * @param items  文件条目
     * @param folder 下载到文件夹
     * @return 文件实体列表
     */
    default List<File> downloadAttachments(List<String> items, File folder) {

        List<File> files = new ArrayList<>(items.size());
        items.forEach(aliOSSUrl -> {
            if (StringUtils.isNotBlank(aliOSSUrl) && aliOSSUrl.startsWith(HTTP_PROTOCOL_PLAIN)) {
                File file = downloadFile(aliOSSUrl, folder, UUID.randomUUID().toString());
                files.add(file);
            } else {
                throw new ECIllegalArgumentException("Invalid OSS URL, file download failed with url " + aliOSSUrl);
            }
        });
        return files;
    }

}

