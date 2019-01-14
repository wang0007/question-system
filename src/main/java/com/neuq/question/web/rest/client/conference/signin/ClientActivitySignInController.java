package com.neuq.question.web.rest.client.conference.signin;


import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.*;
import com.neuq.question.domain.enums.SignInType;
import com.neuq.question.error.NoPermissionException;
import com.neuq.question.error.TimeOutException;
import com.neuq.question.service.ActivitySignInService;
import com.neuq.question.service.ConferenceActivityService;
import com.neuq.question.service.ConferenceService;
import com.neuq.question.service.events.signin.pojo.SignInEvent;
import com.neuq.question.support.SpELTemplateRender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author wangshyi
 */
@Api(value = "移动端-大会活动签到接口", description = "移动端-大会活动签到接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/v1/client/{activityId}/signin")
public class ClientActivitySignInController {


    private final ActivitySignInService activitySignInService;

    private final ActivitySignInSettingRepository signInSettingRepository;

    private final SpELTemplateRender spELTemplateRender;


    private final ConferenceActivityService conferenceActivityService;

    private final ConferenceService conferenceService;

    private final MessageSource messageSource;

    private final UserRepsitory userRepsitory;

    /**
     * 参会人员手动扫码签到
     *
     * @param activityId 活动ID
     * @return 签到信息
     */
    @ApiOperation(value = "大会活动签到", notes = "大会活动签到")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SignInResult signInByQRCode(@PathVariable("activityId") String activityId,
                                       @RequestParam("memberId") String memberId) {

        InAPIUser user = userRepsitory.queryById(memberId);

        SignInResult result = new SignInResult();

        ActivitySignInSettingDO signInSetting = signInSettingRepository.queryByActivityId(activityId);

        SignInEvent event;
        ActivitySignInSettingDO.NotifySetting notifySetting = signInSetting.getNotifySetting();
        try {
            event = activitySignInService.activitySignIn(user, activityId, SignInType.SCAN_QR_CODE, null);
        } catch (Exception e) {
            log.warn("QR Sign in failed with exception", e);
            result.setSuccess(false);

            if (notifySetting == null || StringUtils.isBlank(notifySetting.getFailureMessageTemplate())) {
                result.setMessage(messageSource
                        .getMessage("signin_failed_notify", new Object[]{}, LocaleContextHolder.getLocale()));
            } else {
                result.setMessage(notifySetting.getFailureMessageTemplate());
            }

            result.setReason(e.getMessage());
            return result;
        }

        Boolean alreadySignIn = event.getAlreadySignIn();
        result.setAlreadySignIn(alreadySignIn);
        result.setSuccess(true);

        String template = null;
        String alreadySignInMessageTemplate = null;
        if (notifySetting != null) {
            result.setBackgroundUrl(notifySetting.getBackgroundUrl());

            alreadySignInMessageTemplate = notifySetting.getAlreadySignInMessageTemplate();
            template = notifySetting.getSuccessMessageTemplate();
        }

        if (StringUtils.isBlank(template)) {
            template = messageSource
                    .getMessage("signin_success_notify", new Object[]{}, LocaleContextHolder.getLocale());
        }

        if (alreadySignIn && StringUtils.isNotBlank(alreadySignInMessageTemplate)) {
            template = alreadySignInMessageTemplate;
        }

        result.setMessage(spELTemplateRender.resolveSpel(template, event));

        return result;
    }


    /**
     * 通过工作人员扫描个人信息二维码进行签到
     *
     * @param activityId 活动ID
     * @param staffMemberId 工作人员Id
     * @param memberId   成员ID
     * @return 签到结果
     */
    @ApiOperation(value = "通过工作人员扫描个人信息二维码进行签到", notes = "通过工作人员扫描个人信息二维码进行签到")
    @PostMapping(value = "/staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public SignInResult signInByWorker(@PathVariable("activityId") String activityId,
                                       @RequestParam("staffMemberId") String staffMemberId,
                                       @RequestParam("memberId") String memberId,
                                       @RequestParam("ts") Long ts) {

        long expired = 15 * 60 * 1000L;

        if (System.currentTimeMillis() - ts > expired) {
            throw new TimeOutException("qr code expired");
        }
        ConferenceActivityDO activity = conferenceActivityService.findAndVerifyActivity(activityId);

        ConferenceDO conference = conferenceService.findAndVerifyConference(activity.getConferenceId());

        InAPIUser inAPIUser = userRepsitory.queryById(staffMemberId);

        if (inAPIUser.getMemberId().equals(conference.getCreator())) {
            throw new NoPermissionException("no permission to help sign in");
        }

        InAPIUser user = userRepsitory.queryById(memberId);

        ActivitySignInSettingDO signInSetting = signInSettingRepository.queryByActivityId(activityId);

        SignInEvent event;
        ActivitySignInSettingDO.NotifySetting notifySetting = signInSetting.getNotifySetting();

        SignInResult result = new SignInResult();
        try {
            event = activitySignInService
                    .activitySignIn(user, activityId, SignInType.QR_CODE_CERTIFY, inAPIUser.getMemberId());
        } catch (Exception e) {
            log.warn("Worker Sign in failed with exception", e);
            result.setSuccess(false);

            result.setMessage(
                    messageSource.getMessage("signin_staff_failed_notify", new Object[]{user.getName()},
                            LocaleContextHolder.getLocale()));

            result.setReason(e.getMessage());
            return result;
        }
        Boolean alreadySignIn = event.getAlreadySignIn();
        result.setAlreadySignIn(alreadySignIn);
        result.setSuccess(true);

        String template = null;
        String alreadySignInMessageTemplate = null;
        if (notifySetting != null) {
            result.setBackgroundUrl(notifySetting.getBackgroundUrl());

            alreadySignInMessageTemplate = notifySetting.getAlreadySignInMessageTemplate();
            template = notifySetting.getSuccessMessageTemplate();
        }

        if (StringUtils.isBlank(template)) {
            template = messageSource
                    .getMessage("signin_success_notify", new Object[]{}, LocaleContextHolder.getLocale());
        }

        if (alreadySignIn && StringUtils.isNotBlank(alreadySignInMessageTemplate)) {
            template = alreadySignInMessageTemplate;
        }

        result.setMessage(spELTemplateRender.resolveSpel(template, event));

        return result;
    }


    @Data
    public static class SignInResult {

        private Boolean success;

        private Boolean alreadySignIn;

        private String message;

        private String reason;

        private String backgroundUrl;

    }

}
