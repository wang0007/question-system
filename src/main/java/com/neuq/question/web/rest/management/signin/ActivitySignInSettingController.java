package com.neuq.question.web.rest.management.signin;


import com.neuq.question.configuration.ApplicationProperties;
import com.neuq.question.data.dao.ActivitySignInSettingRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ActivitySignInSettingDO;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.data.pojo.common.ShortUrlDO;
import com.neuq.question.domain.enums.SignInScope;
import com.neuq.question.service.ConferenceActivityService;
import com.neuq.question.service.ShortUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 活动签到管理
 *
 * @author wangshyi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/activity/{activityId}/signin/setting")
@RequiredArgsConstructor
@Api(value = "活动签到管理接口", description = "活动签到管理接口")
public class ActivitySignInSettingController {

    private final ActivitySignInSettingRepository signInSettingRepository;

    private final UserRepsitory userRepsitory;

    private final ApplicationProperties properties;

    private final ShortUrlService shortUrlService;

    private final ConferenceActivityService activityService;

    /**
     * 获取活动签到设置列表
     *
     * @param activityId 活动id
     * @return 签到列表
     */
    @ApiOperation(value = "获取活动签到设置列表", notes = "获取活动签到设置列表")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ActivitySignInSettingDO list(@PathVariable("activityId") String activityId) {
        return signInSettingRepository.queryByActivityId(activityId);
    }

    /**
     * 获取二维码url
     *
     * @param activityId 活动id
     * @return 二维码详情url
     */
    @ApiOperation(value = "获取二维码", notes = "获取二维码")
    @GetMapping(value = "/qrcode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ShortUrlDO getQRCode(@PathVariable("activityId") String activityId) {


        String originUrl = buildSignInOriginUrl(activityId);

        return shortUrlService.applyShortUrl(originUrl);
    }

    /**
     * 更新大会签到设置，不包括投屏设置
     *
     * @param activityId    活动id
     * @param signInSetting 签到设置
     * @return 签到设置详情
     */
    @ApiOperation(value = "更新大会签到设置，不包括投屏设置", notes = "更新大会签到设置，不包括投屏设置")
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ActivitySignInSettingDO update(@PathVariable("activityId") String activityId,
                                          @RequestBody ActivitySignInSettingDTO signInSetting) {

        ConferenceActivityDO activity = activityService.findAndVerifyActivity(activityId);

        signInSettingRepository.update(activity.getConferenceId(), activityId, signInSetting);
        return signInSettingRepository.queryByActivityId(activityId);
    }

    /**
     * 更新大会签到投屏设置
     *
     * @param activityId    活动ID
     * @param projectionDTO 投屏设置
     * @return 投屏设置信息
     */
    @ApiOperation(value = "更新大会签到投屏设置", notes = "更新大会签到投屏设置")
    @PutMapping(value = "/projection", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SignInProjectionSettingDTO updateProjection(@PathVariable("activityId") String activityId,
                                                       @RequestBody SignInProjectionSettingDTO projectionDTO) {

        signInSettingRepository.updateProjection(activityId, projectionDTO);
        return projectionDTO;
    }

    /**
     * 更新是否需要签到设置
     *
     * @param activityId 活动ID
     * @param enable     是否需要签到
     * @return 是否需要签到
     */
    @ApiOperation(value = "更新是否需要签到设置", notes = "更新是否需要签到设置")
    @PutMapping(value = "/required")
    public Boolean signInRequired(@PathVariable("activityId") String activityId,
                                  @RequestParam("enable") Boolean enable) {

        ConferenceActivityDO activity = activityService.findAndVerifyActivity(activityId);

        signInSettingRepository.updateRequired(activity.getConferenceId(), activityId, enable);
        return enable;
    }


    /**
     * 更新是否需要签到设置
     *
     * @param activityId 活动ID
     * @param scope      签到范围
     * @return 是否需要签到
     */
    @ApiOperation(value = "更新签到人员范围", notes = "更新签到人员范围")
    @PatchMapping(value = "/scope")
    public SignInScope signInRequired(@PathVariable String activityId, @RequestParam SignInScope scope) {

        ConferenceActivityDO activity = activityService.findAndVerifyActivity(activityId);

        signInSettingRepository.updateSignInScope(activityId, scope);

        return scope;
    }

    private String buildSignInOriginUrl(String activityId) {

        String url = properties.concatURL(properties.getHost(),
                properties.getAppPrefix()) + "#/conferenceSignInfo?activityId=%s";

        return String.format(url, activityId);
    }

    /**
     * 签到投屏设置
     */
    @Data
    public static class SignInProjectionSettingDTO {

        private ActivitySignInSettingDO.Projection projection;
    }

    @Data
    public static class ActivitySignInSettingDTO {

        private ActivitySignInSettingDO.NotifySetting notifySetting;

        private ActivitySignInSettingDO.JoinGroupSetting joinGroupSetting;

    }


}
