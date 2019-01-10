package com.neuq.question.service;


import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.data.pojo.OperationLogDO;
import com.neuq.question.domain.enums.OperationType;

import java.util.List;

/**
 * @author yegk7
 * @date 2018/9/10 16:39
 */
public interface OperationLogService {

    /**
     * 添加操作日志
     *
     * @param operationLogDO 操作日志
     */
    void addOperationLog(OperationLogDO operationLogDO);

    void createOperationLog(InAPIUser user, OperationType operationType, String conferenceId,
                            String activityId, Long operateTime, String description);

    /**
     * 查询操作日志
     *
     * @param conferenceId  大会ID
     * @param operationType 操作类型
     * @param operationTime 操作时间
     * @return 操作日志列表
     */
    List<OperationLogDO> queryOperationLog(String conferenceId, OperationType operationType, Long operationTime);
}
