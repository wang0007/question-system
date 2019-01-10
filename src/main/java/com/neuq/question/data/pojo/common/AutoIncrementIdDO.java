package com.neuq.question.data.pojo.common;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author yegk7
 * @create 2018/7/17 20:56
 */
@Document(collection = "common.id.auto-increment")
@ToString(callSuper = true)
@Data
public class AutoIncrementIdDO {

    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_CURRENT = "current";
    public static final String FIELD_U_TIME = "uTime";

    private String category;

    private Long current;

    private Long uTime;

}
