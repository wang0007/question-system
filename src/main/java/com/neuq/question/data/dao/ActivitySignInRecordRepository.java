package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ActivitySignInRecordDO;

import java.util.List;

/**
 * @author yegk7
 * @since 2018/7/17 17:00
 */
public interface ActivitySignInRecordRepository {

    /**
     * 用户是否签到
     *
     * @param memberId   成员ID
     * @param activityId 活动ID
     * @return 用户是否签到
     */
    ActivitySignInRecordDO querySignInRecord(String activityId, String memberId);

    /**
     * 插入签到数据
     *
     * @param signInRecordDO 签到详情
     */
    void insert(ActivitySignInRecordDO signInRecordDO);

    /**
     * 签到记录列表
     *
     * @param activityId   活动id
     * @param phoneKeyword 电话号码模糊匹配
     * @param nameKeyword  名称模糊匹配
     * @param start        开始
     * @param size         大小
     * @return 签到记录列表
     */
    List<ActivitySignInRecordDO> list(String activityId, String phoneKeyword, String nameKeyword, int start, int size);

    /**
     * 签到记录列表
     *
     * @param activityId   活动id
     * @param phoneKeyword 电话号码模糊匹配
     * @param nameKeyword  名称模糊匹配
     * @return 签到记录计数
     */
    long count(String activityId, String phoneKeyword, String nameKeyword);


    /**
     * 实时签到记录列表
     *
     * @param activityId 活动ID
     * @param ts         时间戳
     * @param currentTs  当前时间
     * @return 实时签到记录列表
     */
    List<ActivitySignInRecordDO> incrementalList(String activityId, Long ts, Long currentTs);

    /**
     * 统计活动签到人数
     *
     * @param activityId 活动ID
     * @return 签到人数
     */
    long count(String activityId);

    /**
     * 获取全部列表
     *
     * @param activityId 活动ID
     * @return 列表详情
     */
    List<ActivitySignInRecordDO> getAllRecordList(String activityId);

    /**
     * 获取签到列表
     *
     * @param activityId 活动ID
     * @param size       大小
     * @param start      起始点
     * @return 列表详情
     */
    List<ActivitySignInRecordDO> queryRecordList(String activityId, int start, int size);

    /**
     * 获取全部签到人员memberId列表
     *
     * @param activityId 活动ID
     * @return memberId列表
     */
    List<String> getAllRecordMemberList(String activityId);

    /**
     * 获取全部签到人员手机号列表
     *
     * @param activityId 活动ID
     * @return 手机号列表
     */
    List<String> getAllRecordMobileList(String activityId);

    /**
     * 获取帮他签到列表
     *
     * @param activityId 活动ID
     * @param size       大小
     * @param start      起始点
     * @return 列表详情
     */
    List<ActivitySignInRecordDO> queryHelpRecordList(String activityId, int start, int size);
}
