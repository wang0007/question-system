package com.neuq.question.web.rest.client.userinfo;


import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.InAPIUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息相关接口
 *
 * @author yaoshw@yonyou.com
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/client/user")
@RequiredArgsConstructor
@Api(value = "用户信息接口", description = "用户信息接口")
public class UserInfoController {

    private final UserRepsitory userRepsitory;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserInfoVo getUserInfo(@RequestParam("memberId") String memberId) {

        InAPIUser user = userRepsitory.queryById(memberId);
        String id = user.getMemberId();
        String qrCodeUrl = buildUrl(id);
        return UserInfoVo.builder().inAPIUser(user).qrCodeUrl(qrCodeUrl).build();
    }

    /**
     * 拼接Url地址
     * @param memberId 用户memberID
     * @return qzID和memberId拼接而成的Url
     */
    private static String buildUrl( String memberId) {

        String userInfoUrl = "/rest/s/%s";
        return String.format(userInfoUrl, memberId);
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class UserInfoVo {

        /**
         * 用户信息
         */
        InAPIUser inAPIUser;

        /**
         * 用户信息二维码url
         */
        String qrCodeUrl;
    }

}
