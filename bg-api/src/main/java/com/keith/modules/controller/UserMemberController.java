package com.keith.modules.controller;

import java.util.List;

import com.core.utils.PageBean;
import com.keith.annotation.Login;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Result;
import com.keith.modules.dto.PageDTO;
import com.keith.modules.dto.SplitPageDTO;
import com.keith.modules.dto.user.AddUserDTO;
import com.keith.modules.dto.user.UpdateUserDTO;
import com.keith.modules.dto.user.UserForgetDTO;
import com.keith.modules.entity.token.TokenEntity;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.user.UserMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
//import com.keith.common.utils.PageBean;

/**
 * 用户表
 *
 * @author JohnSon
 * @email 380847163@qq.com
 * @date 2020-05-13 18:15:37
 */
@RestController
@RequestMapping("user/usermember")
@Api(tags = "用户信息接口")
public class UserMemberController {
    @Autowired
    private UserMemberService userMemberService;

    @PostMapping("/findAll")
    @ApiOperation("查询所有的用户信息")
    public Result<PageUtils> findAll(@RequestBody PageDTO pageDTO) throws Exception{
        Result result=new Result();
        return result.ok(userMemberService.getPage(pageDTO));
    }


    @Login
    @PostMapping(value = {"/updatePassword"})
    @ApiOperation("支付密码修改")
    public Result updatePassword (@RequestBody  @ApiParam("个人支付密码修改") UpdateUserDTO updateUserDTO, @ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result r = new Result();
        userMemberService.updatePassword(updateUserDTO,userId);
        return r;
    }
    @Login
    @PostMapping(value = {"/add"})
    @ApiOperation("保存用户信息")
    public Result insert(HttpServletRequest request, @RequestBody @ApiParam("保存用户信息") AddUserDTO addUserDTO,@ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result r = new Result();
        String token = request.getHeader("token");
        userMemberService.insertWx(addUserDTO,userId);
        return r;
    }

    @Login
    @PostMapping(value = {"/setPayCode"})
    @ApiOperation("设置支付密码(验证过后传PaymentCode就行)")
    public Result setPayCode(@RequestBody  UpdateUserDTO updateUserDTO, @ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result r = new Result();
        userMemberService.setPayCode(updateUserDTO,userId);
        return r;
    }

    @Login
    @PostMapping(value = {"/validatePayCode"})
    @ApiOperation("支付密码验证（只需要传PaymentCode）")
    public Result<Boolean> validatePayCode(@RequestBody  UpdateUserDTO updateUserDTO, @ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result r = new Result();

        return r.ok(userMemberService.validatePayCode(updateUserDTO,userId));
    }

    @Login
    @PostMapping(value = {"/getById"})
    @ApiOperation("通过Id获取用户信息")
    public Result<UserMember> getByOpenId(@ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result r = new Result();
        return r.ok(userMemberService.getInfo(userId));
    }

    @Login
    @PostMapping(value = {"/validateExsit"})
    @ApiOperation("通过Id获取用户信息")
    public Result<Boolean> validateExsit(@ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result r = new Result();
        return r.ok(userMemberService.validateExsit(userId));
    }
    @Login
    @PostMapping(value = {"/updateLogin"})
    @ApiOperation("修改登录密码（只需传newPassword）")
    public Result updateLoginPassword(@RequestBody  UpdateUserDTO updateUserDTO,@ApiIgnore @RequestAttribute("userId") long userId) throws Exception{
        Result result = new Result();
        userMemberService.updateLoginPassword(updateUserDTO,userId);
        return result;
    }
    @Login
    @PostMapping(value = {"/forgetLogin"})
    @ApiOperation("忘记登录密码")
    public Result forgetLoginPassword(@RequestBody UserForgetDTO userForgetDTO,@ApiIgnore @RequestAttribute("userId") long userId) throws Exception{
        Result result = new Result();
        userMemberService.forgetLoginPassword(userForgetDTO,userId);
        return result;
    }
    @Login
    @PostMapping(value = {"/forgetPay"})
    @ApiOperation("忘记支付密码")
    public Result forgetPayPassword(@RequestBody UserForgetDTO userForgetDTO,@ApiIgnore @RequestAttribute("userId") long userId) throws Exception{
        Result result = new Result();
        userMemberService.forgetPayPassword(userForgetDTO,userId);
        return result;
    }

    /**
     * 生成邀请码
     * @param userId
     * @return
     * @throws Exception
     */
    @Login
    @PostMapping(value = {"/getInviteCode"})
    @ApiOperation("生成邀请码")
    public Result getCode(@ApiIgnore @RequestAttribute("userId") long userId) throws Exception {
        Result result = new Result();
        return result.ok(userMemberService.inviteCode(userId));
    }

    /**
     * 获取所有没有下级的用户
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/getNoSub")
    @ApiOperation("获取所有没有下级的用户")
    public Result<List<UserMember>> getNoSub() throws Exception{
        Result result = new Result();
        return result.ok(userMemberService.noSubUser());
    }

    /**
     * 获取所有没有下级的用户
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/testFY")
    @ApiOperation("分佣测试")
    public Result testFY() throws Exception{
        Result result = new Result();
//        userMemberService.testFY();
        userMemberService.forNoP();
        return result;
    }
    @Login
    @GetMapping(value = "/getQRcode")
    @ApiOperation("获取二维码")
    public Result getQRcode(@ApiIgnore @RequestAttribute("userId") long userId) throws  Exception{
        Result result = new Result();

        return  result.ok(userMemberService.getCodes(userId));
    }

    @Login
    @PostMapping(value = "/getMyEarnings")
    @ApiOperation("获取收益信息")
    public Result myEarnings(@ApiIgnore @RequestAttribute("userId") long userId) throws  Exception{
        Result result = new Result();

        return  result.ok(userMemberService.myEarnings(userId));
    }
}
