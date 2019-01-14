package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * @author wangshyi
 */
@Document(collection = "conference.app")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ConferenceAppDO extends BasicDO {

    public static final String DEFAULT_APP_ID = "__default";

    public static final String CONFERENCE_ID_PLACE_HOLDER = "${conferenceId}";

    public static final String FIELD_APP = "apps";

    public static final String FIELD_APP_ID = "apps.appId";
    public static final String FIELD_APP_ENABLE = "apps.enable";
    public static final String FIELD_APP_ENABLE_UPDATE = "apps.$.enable";


    private List<App> apps;

    @Data
    public static class App {

        private String appId;

        private String icon;

        private Map<String, String> i18nName;

        private String name;

        private Boolean enable;

        private String url;

        public boolean enable() {
            return enable != null && enable;
        }


    }

}
