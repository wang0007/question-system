package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author liuhaoi
 */
@Document(collection = "conference.guide")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConferenceGuideDO extends BasicDO {

    public static final String FIELD_ICON = "icon";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_ITEMS = "items";
    public static final String FIELD_ITEM_ID = "items.guideItemId";
    public static final String FIELD_ITEM_TITLE = "items.title";
    public static final String FIELD_ITEM_CONTENT = "items.content";


    private String icon;

    private String name;

    private List<GuideItem> items;

    @Data
    @Validated
    public static class GuideItem {

        private String guideItemId;

        @Valid
        @Size(min = 1, max = 100)
        private String title;

        @Valid
        @Size(max = 10000)
        private String content;
    }


}
