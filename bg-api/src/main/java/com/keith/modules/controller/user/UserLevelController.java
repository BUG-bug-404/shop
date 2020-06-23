package com.keith.modules.controller.user;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.common.validator.ValidatorUtils;
import com.keith.modules.entity.user.UserLevel;
import com.keith.modules.service.user.UserLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.keith.common.utils.PageUtils;
import com.keith.common.utils.R;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 店家等级表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-04 09:53:52
 */
@Api(tags ="店家等级表" )
@RestController
@RequestMapping("/userlevel")
public class UserLevelController {
    @Autowired
    private UserLevelService userLevelService;


    /**
     * 获取所有用户等级权益列表
     * @return
     * @throws Exception
     */
    @PostMapping(value = {"/list"})
    @ApiOperation("获取所有用户等级权益列表")
    public Result<List<UserLevel>> getList() throws Exception {
        Result result = new Result();
        return result.ok(userLevelService.getList());
    }

    /**
     * 通过用户ID获取该用户的等级权益信息
     * @param userId
     * @return
     * @throws Exception
     */
    @Login
    @PostMapping(value = {"/getById"})
    @ApiOperation("通过用户ID获取该用户的等级权益信息")
    public Result<UserLevel> getById(@ApiIgnore @RequestAttribute("userId") long userId) throws Exception {
        Result result = new Result();
        return result.ok(userLevelService.getByUserId(userId));
    }

}
