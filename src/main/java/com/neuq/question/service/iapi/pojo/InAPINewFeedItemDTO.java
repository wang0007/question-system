package com.neuq.question.service.iapi.pojo;

/**
 * @author wangshyi
 * @date 2019/1/11  15:35
 */

        import com.neuq.question.data.pojo.InAPINewFeedItem;
        import com.neuq.question.support.bean.BeanUtils;
        import lombok.Data;
        import org.apache.commons.lang3.StringUtils;

        import java.util.Date;
        import java.util.List;
        import java.util.stream.Collectors;

/**
 * @author wangshyi
 */
        @Data
        public class InAPINewFeedItemDTO {

        private String id;

        private String member_id;

        private String created;

        private String content;

        private String hasFile;

        private MemberInfoDTO memberInfo;

        private FilesDTO files;

        public InAPINewFeedItem build() {
        InAPINewFeedItem result = new InAPINewFeedItem();
        result.setId(id);
        result.setMemberId(member_id);
        if (StringUtils.isNotBlank(created)) {
        long ts = Long.parseLong(created) * 1000L;
        result.setCreated(new Date(ts));
        }

        result.setContent(content);
        result.setHasFile("1".equals(hasFile));
        if (memberInfo != null) {
        result.setMemberInfo(memberInfo.build());
        }

        if (files != null) {
        result.setFiles(files.build());
        }

        return result;
        }

        @Data
        public static class MemberInfoDTO {

        private String id;

        private String nickname;

        private String avatar;

        private Character first_letter;

        private String name;

        public InAPINewFeedItem.MemberInfo build() {
        InAPINewFeedItem.MemberInfo result = new InAPINewFeedItem.MemberInfo();
        BeanUtils.copyPropertiesIgnoresNull(this, result);
        return result;
        }
        }

        @Data
        private static class FileInfoDTO {

        private String id;

        private String fileext;

        private String filepath;

        private String filesize;

        private String name;

        private String ext_class;

        private Integer ext;

        public InAPINewFeedItem.FileInfo build() {

        InAPINewFeedItem.FileInfo fileInfo = new InAPINewFeedItem.FileInfo();
        fileInfo.setFileExt(fileext);
        fileInfo.setFilePath(filepath);
        if (StringUtils.isNotBlank(filesize)) {
        fileInfo.setFileSize(Long.parseLong(filesize));
        }
        fileInfo.setId(id);
        fileInfo.setName(name);
        fileInfo.setExtClass(ext_class);
        fileInfo.setExt(ext);


        return fileInfo;
        }

        }


        @Data
        private static class FilesDTO {
        private List<FileInfoDTO> audio;
        private List<FileInfoDTO> image;
        private List<FileInfoDTO> video;
        private List<FileInfoDTO> file;

        public InAPINewFeedItem.Files build() {
        InAPINewFeedItem.Files filesResult = new InAPINewFeedItem.Files();

        if (audio != null) {
        List<InAPINewFeedItem.FileInfo> audios = audio.stream().map(FileInfoDTO::build)
        .collect(Collectors.toList());
        filesResult.setAudio(audios);
        }
        if (image != null) {
        List<InAPINewFeedItem.FileInfo> images = image.stream().map(FileInfoDTO::build)
        .collect(Collectors.toList());
        filesResult.setImage(images);
        }
        if (video != null) {
        List<InAPINewFeedItem.FileInfo> videos = video.stream().map(FileInfoDTO::build)
        .collect(Collectors.toList());
        filesResult.setVideo(videos);
        }
        if (file != null) {
        List<InAPINewFeedItem.FileInfo> files = file.stream().map(FileInfoDTO::build)
        .collect(Collectors.toList());
        filesResult.setFile(files);
        }

        return filesResult;
        }
        }


        }
