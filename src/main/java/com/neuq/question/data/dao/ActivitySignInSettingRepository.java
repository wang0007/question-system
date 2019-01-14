package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ActivitySignInSettingDO;
import com.neuq.question.domain.enums.SignInScope;
import com.neuq.question.web.rest.management.signin.ActivitySignInSettingController;

import java.util.List;

/**
 * @author wangshyi
 * @since 2018/11/17 10:06
 */
public interface ActivitySignInSettingRepository {

    /**
     * 根据大会ID获取签到设置
     *
     * @param conferenceId   大会ID
     * @param requiredSignIn 需要签到，null表示不限制
     * @return 签到设置列表
     */
    List<ActivitySignInSettingDO> queryByConferenceId(String conferenceId, Boolean requiredSignIn);

    /**
     * 通过活动id查询活动签到设置
     *
     * @param activityId 活动id
     * @return 签到设置详情
     */
    ActivitySignInSettingDO queryByActivityId(String activityId);

    /**
     * 保存设置
     *
     * @param setting 设置
     */
    void save(ActivitySignInSettingDO setting);

    /**
     * 更新大会签到设置
     *
     * @param activityId    活动id
     * @param signInSetting 签到设置
     * @return 更新成功条数
     */
    long update(String conferenceId, String activityId,
                ActivitySignInSettingController.ActivitySignInSettingDTO signInSetting);

    /**
     * 更新大会签到投屏设置
     *
     * @param activityId           活动ID
     * @param projectionSettingDTO 投屏设置
     * @return 更新成功条数
     */
    long updateProjection(String activityId,
                          ActivitySignInSettingController.SignInProjectionSettingDTO projectionSettingDTO);

    /**
     * 更新活动是否需要签到设置
     *
     * @param activityId 活动ID
     * @param enable     是否需要签到
     * @return 更新成功条数
     */
    long updateRequired(String conferenceId, String activityId, Boolean enable);

    /**
     * 增加签到范围选择
     *
     * @param activityId 活动ID
     * @param scope      范围
     * @return 更改条数
     */
    long updateSignInScope(String activityId, SignInScope scope);
}
