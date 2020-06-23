package com.keith.modules.controller;

import com.keith.common.utils.Result;
import com.keith.modules.dto.SignInDTO;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.user.UserMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理用户登入接口
 * @author gray
 * @version 1.0
 * @date 2020/6/12 10:53
 */
@RestController
@Api(tags = {"登入"})
public class AdminLoginController {


    @Autowired
    private UserMemberService userMemberService;
    @Autowired
    private RedisTemplate redisTemplate;



    @PostMapping(value = {"/sign-in"})
    @ApiOperation(value = "登录")
    public Result signIn(@RequestBody @Validated @ApiParam(value = "登录数据",required = true)
                                 SignInDTO signInDTO){
        Result result = new Result();
        String  code = String.valueOf(redisTemplate.opsForValue().get("smsCode"+signInDTO.getMobile()+"1"));
        if("".equals(code)||code==null){
            return result.error("验证码失效");
        }
        UserMember userMember = userMemberService.userMemberLogin(signInDTO);
        if (userMember==null){
            return result.error("用户不存在");
        }
        return result.ok(userMember);
    }


    @GetMapping(value = "/test1")
    public Result test(){
        return new Result().error("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50VGltZU1pbGxpcyI6IjE1OTIwMjg4NDIyMTQiLCJleHAiOjE1OTIxMTUyNDIsImFjY291bnQiOiIxODY2NzIzMTA3MSJ9.mGciEJUNevqsAAj5CPbgLiekMAYd8eyF8EQsSbtXXjA");
    }
}
