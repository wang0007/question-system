package com.neuq.question.web.rest.start;

import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.error.AnswerNotExistException;
import com.neuq.question.web.rest.pojo.SignUpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangshyi
 * @date 2018/12/27  15:10
 */
@RestController
@RequestMapping(value = "rest/start/signup")
@Api(value = "注册接口", description = "用户注册接口")
@RequiredArgsConstructor
public class SignUpController {

    private final UserRepsitory userRepsitory;


    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public SignUpResult userSignUp(@RequestBody InAPIUser inAPIUser) {


        boolean flag = userRepsitory.queryByLoginName(inAPIUser.getLoginName());
        if (flag){
            throw new AnswerNotExistException("loginName is exist!");

        }
        InAPIUser inAPIUser1 = transToUser(inAPIUser);
        return singUp(inAPIUser1);

    }

    private SignUpResult singUp(InAPIUser inAPIUser1) {
        InAPIUser user = userRepsitory.signUp(inAPIUser1);
        SignUpResult signUpResult = new SignUpResult();
        String memberId = user.getMemberId();
        if (memberId == null) {
            signUpResult.setLogin(false);
            return signUpResult;
        }
        signUpResult.setLogin(true);
        signUpResult.setMemberId(memberId);

        return signUpResult;

    }

    private InAPIUser transToUser(InAPIUser inAPIUser) {

        InAPIUser user = new InAPIUser();
        user.setLoginName(inAPIUser.getLoginName());
        user.setPassword(inAPIUser.getPassword());
        user.setEmail("");
        user.setAvatar("");
        user.setBirthday("");
        user.setMobile("");
        user.setName("");
        user.setSex("");
        return user;

    }

    @ApiOperation(value = "管理员注册", notes = "管理员注册")
    @PostMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public SignUpResult adminSignUp(@RequestBody InAPIUser inAPIUserVo) {

        boolean flag = userRepsitory.queryByLoginName(inAPIUserVo.getLoginName());
        if (flag){
            throw new AnswerNotExistException("loginName is exist!");

        }
        InAPIUser user1 = transToUser(inAPIUserVo);
        InAPIUser user = userRepsitory.signUp(user1);

        return singUp(user);

    }

    @Data
    public class InAPIUserDTO {

        /**
         * 登录名
         */
        private String loginName;

        private String password;
    }
}
