package com.neuq.question.service.iapi.impl;

import com.alibaba.fastjson.JSON;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.error.ECRemoteServiceException;
import com.neuq.question.service.iapi.pojo.HttpInAPIRequestTemplate;
import com.neuq.question.service.iapi.pojo.InAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author wangshyi
 * @date 2019/1/11  16:18
 */
@Service
@Slf4j
public class HttpInAPIRequestTemplateImpl implements HttpInAPIRequestTemplate {


    private static final String CONTENT_TYPE = "Content-Type";

    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";

    @Override
    public <T> T doPost(String urlString, String body, Class<T> clazz) {
        byte[] bytes = doPost(urlString, body.getBytes(StandardCharsets.UTF_8), true);

        if (bytes != null) {
            String response = new String(bytes, StandardCharsets.UTF_8);

            if (log.isDebugEnabled()) {
                log.debug("inapi post url {} with response {}", urlString, response);
            }

            if (String.class.isAssignableFrom(clazz)) {
                return (T) response;
            }

            T parseObject = JSON.parseObject(response, clazz);

            if (parseObject instanceof InAPIResponse) {
                ((InAPIResponse) parseObject).check(urlString, body);
            }

            return parseObject;
        }
        return null;
    }

    public byte[] doPost(String urlString, byte[] body, boolean doInput) {
        return doPost(urlString, body, doInput, null);
    }

    public byte[] doPost(String urlString, byte[] body, boolean doInput, Map<String, String> header) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            log.error("Invalid http request Url {}", urlString);
            throw new ECIllegalArgumentException("服务地址配置出错, URL: " + urlString, e);
        }

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            log.error("IOException when http connection, with url " + urlString, e);
            throw new ECIllegalArgumentException("服务连接出错失败", e);
        }

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException ignored) {
            //ignore
        }

        if (header != null) {
            header.forEach(connection::addRequestProperty);
        }

        connection.setDoInput(doInput);
        connection.setRequestProperty(CONTENT_TYPE, FORM_CONTENT_TYPE);
        connection.setConnectTimeout(10 * 1000);
        connection.setReadTimeout(60 * 1000);

        if (body != null && body.length > 0) {
            connection.setDoOutput(true);
            try {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(body);
            } catch (IOException e) {
                throw new ECRemoteServiceException("服务调用失败, 写入流失败, url: " + urlString, e);
            }
        } else {
            connection.setDoOutput(false);
        }

        String result;
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                InputStream errorStream = connection.getErrorStream();

                result = IOUtils.toString(errorStream, String.valueOf(StandardCharsets.UTF_8));

                if (body != null && body.length > 0) {
                    log.error("Request url {} failed with code {} and request body {} and response {}", urlString,
                            responseCode, new String(body, StandardCharsets.UTF_8), result);
                } else {
                    log.error("Request url {} failed with code {} and response {}", urlString, responseCode, result);
                }

                throw new ECRemoteServiceException("服务调用失败, 错误原因: " + result);
            }

            if (doInput) {
                InputStream inputStream = connection.getInputStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);

                if (log.isDebugEnabled()) {
                    log.debug("post request {} with response {}", urlString, new String(bytes, StandardCharsets.UTF_8));
                }

                return bytes;
            } else {
                return new byte[0];
            }

        } catch (IOException e) {
            log.error("IOException when Request " + urlString + " with response", e);
            throw new ECRemoteServiceException("服务调用发生错误,", e);
        }
    }

}
