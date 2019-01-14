package com.neuq.question.web.rest.client.common;

import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.service.InAPISmsService;
import com.neuq.question.service.VerifyCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信相关接口
 *
 * @author yegk7
 * @date 2018/8/28 15:57
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("rest/v1/client/sms")
@Api(value = "短信相关接口", description = "短信相关接口")
public class SmsController {

    private static final String STRING_NULL = "_NULL_";

    private final ConferenceRepository conferenceRepository;

    private final VerifyCodeService verifyCodeService;

    private final InAPISmsService inAPISmsService;

    @ApiOperation(value = "获取报名验证码", notes = "获取报名验证码")
    @GetMapping(value = "/{conferenceId}")
    public void getSignUpVerifyCode(@RequestParam("mobile") String mobile,
                                    @PathVariable("conferenceId") String conferenceId) {

        ConferenceDO conferenceDO = conferenceRepository.queryByConferenceId(conferenceId);

        // 生成四位随机数作为验证码
        String verifyCode = RandomStringUtils.randomNumeric(4);
        // 将验证码存入redis
        String key = buildSignUpKey(conferenceId, mobile);
        verifyCodeService.saveToRedis(key, verifyCode);

        Map<String, String> data = new HashMap<>(5);
        data.put("mettingName", conferenceDO.getName());
        data.put("verificationCode", verifyCode);
        data.put("time", "30");

        inAPISmsService.sendSms( mobile, data);
    }

    private String buildSignUpKey(String conferenceId, String mobile) {

        return String.format("sign_up_verify_code:%s:%s", conferenceId == null ? STRING_NULL : conferenceId,
                mobile == null ? STRING_NULL : mobile);
    }


}
