package com.neuq.question.web.rest.start;

import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.error.UserNameNotExistException;
import com.neuq.question.web.rest.pojo.SignUpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangshyi
 * @date 2018/12/27  15:09
 */
@RestController
@RequestMapping(value = "rest/start/signin")
@Api(value = "登录接口", description = "用户登录接口")
@RequiredArgsConstructor
public class SignInController {

    private final UserRepsitory userRepsitory;

    @ApiOperation(value = "判断管理员登录结果", notes = "判断管理员登录结果")
    @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public SignUpResult adminSignIn(@RequestParam(value = "loginName") String loginName,
                                    @RequestParam(value = "password") String password) {

        return juageResult(loginName, password);

    }

    private SignUpResult juageResult(String loginName, String password) {
        boolean isUserName = userRepsitory.isUserName(loginName);
        if (!isUserName) {
            throw new UserNameNotExistException("username is error or not exist");
        }
        InAPIUser inAPIUser = userRepsitory.isLogin(loginName, password);

        SignUpResult signUpResult = new SignUpResult();

        if (inAPIUser == null) {
            throw new UserNameNotExistException("password is error");
        }
        String memberId = inAPIUser.getMemberId();
        signUpResult.setLogin(true);
        signUpResult.setMemberId(memberId);
        return signUpResult;
    }

    @ApiOperation(value = "判断用户登录结果", notes = "判断用户登录结果")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public SignUpResult userSignIn(@RequestParam(value = "loginName") String loginName,
                                   @RequestParam(value = "password") String password) {

        return juageResult(loginName, password);
    }

    /**
     * 编辑
     *
     * @param memberId 用户id
     * @param inAPIUserDTO 用户信息
     * @return 编辑后用户信息
     */
    @ApiOperation(value = "编辑个人信息，不含头像", notes = "编辑个人信息，不含头像")
    @PutMapping(value = "update/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InAPIUserDTO updateUserInfo(@PathVariable(value = "memberId") String memberId,
                                       @RequestBody InAPIUserDTO inAPIUserDTO) {

        InAPIUser inAPIUser = new InAPIUser();

        BeanUtils.copyProperties(inAPIUserDTO, inAPIUser);
        inAPIUser.setMemberId(memberId);

        InAPIUser updateUser = userRepsitory.update(inAPIUser);

        InAPIUserDTO userDTO = new InAPIUserDTO();

        BeanUtils.copyProperties(updateUser, userDTO);

        return userDTO;


    }


    @Data
    private class InAPIUserDTO {
        /**
         * 用户名字
         */
        private String name;

        private String mobile;

        private String email;

        /**
         * 生日
         */
        private String birthday;

        /**
         * 0 男 1女 2 未知 "保密"
         */
        private String sex;
    }
}
