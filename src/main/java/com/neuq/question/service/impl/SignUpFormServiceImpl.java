package com.neuq.question.service.impl;

import com.neuq.question.data.dao.SignUpFormRepository;
import com.neuq.question.data.pojo.ConferenceAppDO;
import com.neuq.question.data.pojo.ConferenceSignUpFormDO;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.data.pojo.ConferenceSignUpSettingDO;
import com.neuq.question.service.SignUpFormService;
import com.neuq.question.web.rest.management.conference.signup.SignUpSettingController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author wangshyi
 * @since 2018/11/10 18:37
 */
@Service
@RequiredArgsConstructor
public class SignUpFormServiceImpl implements SignUpFormService {

    private final SignUpFormRepository formRepository;

    @Override
    public void processInvitationCodeField(String conferenceId, SignUpSettingController.SignUpSettingDTO setting,
                                           ConferenceSignUpSettingDO oldSetting) {
        // 修改配置时查看大会报名表是否存在
        ConferenceSignUpFormDO conferenceSignUpFormDO = formRepository.queryById(conferenceId);

        if (setting.getNeedInvitationCode().equals(oldSetting.getNeedInvitationCode())) {
            return;
        }

        Optional<ConferenceSignUpFormDO.FormField> field = conferenceSignUpFormDO.getFields().stream()
                .filter(formField -> ConferenceSignUpRecordDO.FIXED_INVITE_CODE_ID.equals(formField.getFormFieldId()))
                .findFirst();

        if (setting.getNeedInvitationCode()) {
            if (field.isPresent()) {
                return;
            }
            // 获取报名表默认字段
            List<ConferenceSignUpFormDO.FormField> fields = formRepository.queryById(ConferenceAppDO.DEFAULT_APP_ID).getFields();

            // 如果需要验证码，但是验证码不存在，添加初始验证码字段
            Optional<ConferenceSignUpFormDO.FormField> tempField = fields.stream()
                    .filter(formField -> ConferenceSignUpRecordDO.FIXED_INVITE_CODE_ID
                            .equals(formField.getFormFieldId()))
                    .findFirst();
            tempField.ifPresent(formField -> formRepository.insertField(conferenceId, formField));

        } else {
            // 如果不需要验证码，但是验证码存在，删除报名表中的验证码
            field.ifPresent(formField -> formRepository.deleteFormField(conferenceId, formField.getFormFieldId()));
        }
    }

}
