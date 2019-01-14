package com.neuq.question.data.pojo;

/**
 * @author wangshyi
 * @date 2019/1/11  15:24
 */

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuhaoi
 */
@Data
public class InAPINewFeedItem {

    private String id;

    private String memberId;

    private Date created;

    private String content;

    private Boolean hasFile;

    private MemberInfo memberInfo;

    private Files files;

    @Data
    public static class MemberInfo {

        private String id;


        private String avatar;


        private String name;
    }


    @Data
    public static class Files {
        private List<FileInfo> audio;
        private List<FileInfo> image;
        private List<FileInfo> video;
        private List<FileInfo> file;
    }

    @Data
    public static class FileInfo {

        private String id;

        private String fileExt;

        private String filePath;

        private Long fileSize;

        private String name;

        private String extClass;

        private Integer ext;

    }

}
