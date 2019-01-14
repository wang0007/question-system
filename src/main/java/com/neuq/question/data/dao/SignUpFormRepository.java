package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ConferenceSignUpFormDO;
import com.neuq.question.web.rest.management.conference.signup.SignUpFormController;

/**
 * @author wangshyi
 */
public interface SignUpFormRepository {

    /**
     * 根据大会ID获取签到表
     *
     * @param conferenceId 大会ID
     * @return 签到表
     */
    ConferenceSignUpFormDO queryById(String conferenceId);

    /**
     * 保存签到表单
     *
     * @param form 表单
     */
    void save(ConferenceSignUpFormDO form);

    /**
     * 更新签到表单
     *
     * @param conferenceId 大会ID
     * @param form         表单
     * @return 更新数量
     */
    long update(String conferenceId, SignUpFormController.ConferenceSignUpFormDTO form);

    /**
     * 更新报名表名称，在修改大会名称时同步更新
     *
     * @param conferenceId 大会ID
     * @param formTitle    标题名称
     * @return 更新数量
     */
    long updateFormTitle(String conferenceId, String formTitle);

    /**
     * 插入单条字段
     *
     * @param conferenceId 大会ID
     * @param formField    报名表字段
     * @return 插入条数
     */
    long insertField(String conferenceId, ConferenceSignUpFormDO.FormField formField);

    /**
     * 删除报名表单条字段
     *
     * @param conferenceId 大会ID
     * @param fieldId      字段ID
     * @return 删除条数
     */
    long deleteFormField(String conferenceId, String fieldId);

}
