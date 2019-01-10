package com.neuq.question.support.oss.start;


import static com.neuq.question.configuration.AbstractApplicationProperties.URL_SPLIT_CHAT;

/**
 * @author wangshyi
 * @date 2019/1/2  9:37
 */
public interface FileRemotePathPolicy {

    String buildFileName();

    String buildFileName(String extension);

    default String concatPath(String path1, String path2) {
        char c1 = path1.charAt(path1.length() - 1);

        char c2 = path2.charAt(0);

        if (c1 == URL_SPLIT_CHAT && c2 == URL_SPLIT_CHAT) {
            return path1 + path2.substring(1);
        }

        if (c1 == URL_SPLIT_CHAT || c2 == URL_SPLIT_CHAT) {
            return path1 + path2;
        }

        return path1 + URL_SPLIT_CHAT + (path2);

    }


}

