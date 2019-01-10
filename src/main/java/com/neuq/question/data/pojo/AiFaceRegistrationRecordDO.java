package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author liuhaoi
 */
@Document(collection = "ai.face.registration.record")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AiFaceRegistrationRecordDO extends BasicDO {

    public static final String FIELD_MEMBER_ID = "memberId";
    public static final String FIELD_YHT_USER_ID = "yhtUserId";
    public static final String FIELD_IMAGES = "images";

    private String memberId;

    private String yhtUserId;

    private List<ImageEntry> images;


    @Data
    public static class ImageEntry {

        private String conferenceId;

        private String url;

        private String groupId;

        private String tenantId;

        private String qzId;

        private Long ctime;
    }


}
