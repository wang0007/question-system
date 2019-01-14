package com.neuq.question.service;

import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.domain.enums.AuditStatus;

import java.util.List;

/**
 * @author wangshyi
 * @since 2018/11/16 20:03
 */
public interface SignUpRecordService {

    /**
     * 插入报名表信息
     *
     * @param recordDO 报名表信息
     */
    void insertSignUpRecord(ConferenceSignUpRecordDO recordDO);

    /**
     * 获取报名记录列表
     *
     * @param conferenceId   大会ID
     * @param auditStatuses  审核状态
     * @param invitationCode 邀请码
     * @param fieldId        字段ID
     * @param fieldValue     字段值
     * @param start          开始数
     * @param size           大小
     * @return 记录列表
     */
    List<List<Object>> getSignUpRecordList(String conferenceId,
                                           List<AuditStatus> auditStatuses,
                                           String invitationCode,
                                           String fieldId,
                                           String fieldValue,
                                           Integer start,
                                           Integer size);

    /**
     * 审核大会报名
     *
     * @param conferenceId 大会ID
     * @param mobiles      手机号列表
     * @param status       状态
     */
    void updateAuditRecord(String conferenceId, List<String> mobiles, AuditStatus status);
}
