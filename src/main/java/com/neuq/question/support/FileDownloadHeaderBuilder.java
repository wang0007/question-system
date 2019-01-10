package com.neuq.question.support;


import com.neuq.question.error.ECConfigurationException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangshyi
 * @date 2018/12/27  14:23
 */

public class FileDownloadHeaderBuilder {


    private static final String DISPOSITION_HEADER = "Content-disposition";

    /**
     * 构建下载头的header,指定文件名
     *
     * @param request  请求
     * @param fileName 文件名
     * @return
     */
    public static Map<String, String> buildCompatibleDownloadFileName(final HttpServletRequest request,
                                                                      final String fileName) {


        boolean isMSBrowser = isMSBrowser(request);

        Map<String, String> header = new HashMap<>(1);
        header.put(DISPOSITION_HEADER, buildCompatibleDispositionHeaderValue(fileName, isMSBrowser));

        return header;

    }

    /**
     * 构造文件名指定的header的值
     *
     * @param fileName    文件名
     * @param isMSBrowser 是否是IE浏览器
     * @return
     */
    public static String buildCompatibleDispositionHeaderValue(final String fileName, boolean isMSBrowser) {

        String name = fileName;

        if (isMSBrowser) {
            //IE浏览器的乱码问题解决
            try {
                name = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new ECConfigurationException("UnsupportedEncodingException", e);
            }
        } else {
            //万能乱码问题解决
            name = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        return buildDispositionHeaderValue(name);

    }

    public static String buildDispositionHeaderValue(final String fileName) {
        return "attachment;filename=\"" + fileName + "\"";
    }

    private static String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};

    public static boolean isMSBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal)) {
                return true;
            }
        }
        return false;
    }

}
