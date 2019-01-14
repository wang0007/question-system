package com.neuq.question.data.pojo;


import com.neuq.question.data.pojo.common.AbstractActivity;
import com.neuq.question.domain.enums.OperationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author wangshyi
 * @date 2018/11/10 16:45
 */
@Document(collection = "operate.log")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class OperationLogDO extends AbstractActivity {

    public final static String FIELD_OPERATION_TYPE = "operationType";

    public final static String FIELD_OPERATION_TIME = "operateTime";

    private InAPIUser inAPIUser;

    private OperationType operationType;

    private Long operateTime;

    private String description;
}
