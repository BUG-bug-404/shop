package com.keith.modules.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.common.SecurityConsts;
import com.keith.common.utils.Result;
import com.keith.config.shiro.security.JwtUtil;
import com.keith.modules.entity.app.UserMemberApp;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.app.AppUserService;
import com.keith.modules.service.user.UserMemberService;
import com.keith.utils.Guid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户app设置
 *
 * @author gray
 * @version 1.0
 * @date 2020/6/13 15:26
 */
@RestController
@RequestMapping("/app")
@Api(tags = "用户app设置")
public class AppUserController {


    @Autowired
    private AppUserService appUserService;
    @Autowired
    private UserMemberService userMemberService;

    @RequiresAuthentication
    @PostMapping("/add")
    @ApiOperation("添加app详情")
    public Result addAppId(@RequestBody UserMemberApp app){
        Guid guid = new Guid();
        UserMember user = userMemberService.getOne(new LambdaQueryWrapper<UserMember>().eq(UserMember::getMobile, JwtUtil.getClaim(SecurityUtils.getSubject().getPrincipal().toString(), SecurityConsts.ACCOUNT)));
        UserMemberApp dbApp = appUserService.getOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getUserMemberId,user.getId()));
        if (dbApp != null){
            return new Result().error("已存在app");
        }
        app.setUserMemberId(user.getId());
        app.setAppId(guid.App_key());
        app.setAppSecret(guid.App_screct());
        appUserService.save(app);
        return new Result().ok(app);
    }

    @RequiresAuthentication
    @GetMapping("/info")
    @ApiOperation("查询app详情")
    public Result appInfo(Page page){
        UserMember user = userMemberService.getOne(new LambdaQueryWrapper<UserMember>().eq(UserMember::getMobile, JwtUtil.getClaim(SecurityUtils.getSubject().getPrincipal().toString(), SecurityConsts.ACCOUNT)));
        IPage result = appUserService.page(page,new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getUserMemberId,user.getId()));
        return new Result().ok(result);
    }

}
