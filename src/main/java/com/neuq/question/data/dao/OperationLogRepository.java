package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.OperationLogDO;
import com.neuq.question.domain.enums.OperationType;

import java.util.List;

/**
 * 操作日志
 *
 * @author yegk7
 * @date 2018/9/11 10:59
 */
public interface OperationLogRepository {

    /**
     * 新增操作日志
     *
     * @param operationLogDO 操作日志
     */
    void create(OperationLogDO operationLogDO);

    /**
     * 查询操作日志列表
     *
     * @param conferenceId  大会ID
     * @param operationType 操作类型
     * @param operationTime 操作时间
     * @return 日志列表
     */
    List<OperationLogDO> query(String conferenceId, OperationType operationType, Long operationTime);
}
