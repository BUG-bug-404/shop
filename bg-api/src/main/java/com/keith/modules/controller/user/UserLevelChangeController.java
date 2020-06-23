package com.keith.modules.controller.user;

import java.util.Arrays;
import java.util.Map;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.common.validator.ValidatorUtils;
import com.keith.modules.entity.user.UserLevelChange;
import com.keith.modules.service.user.UserLevelChangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.keith.common.utils.PageUtils;
import com.keith.common.utils.R;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 店家等级变化表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-04 09:53:52
 */
@Api(tags = "店家等级变化表")
@RestController
@RequestMapping("/userlevelchange")
public class UserLevelChangeController {
    @Autowired
    private UserLevelChangeService userLevelChangeService;

    @Login
    @PostMapping("/updateLevelA")
    @ApiOperation("用户购买提示店长等级")
    public Result toAlevelA(@ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result result = new Result();
            userLevelChangeService.updatelevelA(userId);
        return result;
    }
    @Login
    @PostMapping("/updateLevelB")
    @ApiOperation("用户购买提示店长等级")
    public Result toAlevelB(@ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result result = new Result();
        userLevelChangeService.updatelevelB(userId);
        return result;
    }
    @Login
    @PostMapping("/updateLevelC")
    @ApiOperation("用户购买提示店长等级")
    public Result toAlevelC(@ApiIgnore @RequestAttribute("userId") long userId)throws Exception{
        Result result = new Result();
        userLevelChangeService.updatelevelC(userId);
        return result;
    }

}
