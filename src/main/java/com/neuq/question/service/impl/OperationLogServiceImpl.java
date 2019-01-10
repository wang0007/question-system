package com.neuq.question.service.impl;


import com.neuq.question.data.dao.OperationLogRepository;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.data.pojo.OperationLogDO;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yegk7
 * @date 2018/9/10 16:39
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogRepository repository;

    @Override
    public void addOperationLog(OperationLogDO operationLogDO) {
        repository.create(operationLogDO);
    }

    @Override
    public void createOperationLog(InAPIUser user, OperationType operationType, String conferenceId,
                                   String activityId, Long operateTime, String description) {

        OperationLogDO operationLogDO = new OperationLogDO();
        operationLogDO.setInAPIUser(user);
        operationLogDO.setOperationType(operationType);
        operationLogDO.setConferenceId(conferenceId);
        operationLogDO.setActivityId(activityId);
        operationLogDO.setOperateTime(operateTime);
        operationLogDO.setDescription(description);

        repository.create(operationLogDO);
    }

    @Override
    public List<OperationLogDO> queryOperationLog(String conferenceId, OperationType operationType, Long operationTime) {
        return repository.query(conferenceId, operationType, operationTime);
    }
}
