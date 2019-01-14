package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.domain.enums.AuditStatus;

import java.util.List;

/**
 * 签到记录表
 *
 * @author wangshyi
 */
public interface SignUpRecordRepository {

    /**
     * 获取大会的报名记录
     *
     * @param conferenceId   大会ID
     * @param auditStatus    状态
     * @param invitationCode 邀请码
     * @param fieldName      字段名
     * @param fieldValue     字段值
     * @param start          起始页
     * @param size           大小
     * @return 报名记录列表
     */
    List<ConferenceSignUpRecordDO> list(String conferenceId, List<AuditStatus> auditStatus, String invitationCode,
                                        String fieldName, String fieldValue, Integer start, Integer size);

    /**
     * 审核大会报名
     *
     * @param conferenceId 大会id
     * @param memberIds    报名用户id list
     * @param status       审核状态
     * @return 成功条数
     */
    long updateAuditRecord(String conferenceId, List<String> memberIds, AuditStatus status);

    /**
     * 创建报名审核信息
     *
     * @param recordDO 报名审核信息
     */
    void create(ConferenceSignUpRecordDO recordDO);

    /**
     * 查询报名信息
     *
     * @param conferenceId 大会id
     * @param memberId     报名用户id
     * @return 报名信息
     */
    ConferenceSignUpRecordDO queryById(String conferenceId, String memberId);

    /**
     * 根据ids查询报名信息
     *
     * @param conferenceId 大会ID
     * @param memberIds    报名用户IDs
     * @return 报名信息列表
     */
    List<ConferenceSignUpRecordDO> queryByIds(String conferenceId, List<String> memberIds);

    /**
     * 查询大会报名人数
     *
     * @param conferenceId   大会ID
     * @param auditStatus    状态
     * @param invitationCode 邀请码
     * @param fieldName      字段名
     * @param fieldValue     字段值
     * @return 人员数量
     */
    Long count(String conferenceId, List<AuditStatus> auditStatus, String invitationCode, String fieldName, String fieldValue);

    /**
     * 获取通过大会报名的列表
     *
     * @param conferenceId 大会ID
     * @return 报名列表
     */
    List<ConferenceSignUpRecordDO> passedList(String conferenceId);

    /**
     * 获取通过大会报名的人员手机号列表
     *
     * @param conferenceId 大会ID
     * @return 手机号列表
     */
    List<String> passedMobileList(String conferenceId);

    /**
     * 获取全部报名人员的手机号列表
     *
     * @param conferenceId 大会ID
     * @return 手机号列表
     */
    List<String> allMobileList(String conferenceId);

    /**
     * 获取大会报名的全部列表
     *
     * @param conferenceId 大会ID
     * @return 报名列表
     */
    List<ConferenceSignUpRecordDO> allList(String conferenceId);

    /**
     * 批量插入数据
     *
     * @param recordDOList 报名记录list
     */
    void insertRecordList(List<ConferenceSignUpRecordDO> recordDOList);

    /**
     * 用户是否报名
     *
     * @param conferenceId 大会ID
     * @param mobile       手机号
     * @return 是否报名
     */
    boolean isUserSignedUp(String conferenceId, String mobile);

    /**
     * 用户是否已经通过报名
     *
     * @param conferenceId 大会ID
     * @param mobile       手机号
     * @return 是否已经通过报名
     */
    boolean isUserSignedUpPassed(String conferenceId, String mobile);

    /**
     * 获取通过该邀请码报名的数量
     *
     * @param invitationCode 邀请码
     * @param auditStatus    审核状态
     * @return 报名数量
     */
    long countByInvitationCode(String invitationCode, AuditStatus auditStatus);

}
