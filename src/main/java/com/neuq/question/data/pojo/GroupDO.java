package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.QuestionBasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * @author yegk7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "group")
@FieldNameConstants
public class GroupDO extends QuestionBasicDO {

    /**
     * 用户Id
     */
    private List<String> memberId;

    private String name;
}
