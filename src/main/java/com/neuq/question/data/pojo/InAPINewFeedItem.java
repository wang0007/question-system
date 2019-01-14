package com.neuq.question.data.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author wangshyi
 */
@Data
@Document(collection = "user.item")
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
