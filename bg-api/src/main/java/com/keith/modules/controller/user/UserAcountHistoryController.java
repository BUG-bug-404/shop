package com.keith.modules.controller.user;

import java.util.Arrays;
import java.util.Map;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.common.validator.ValidatorUtils;
import com.keith.modules.dto.PageDTO;
import com.keith.modules.entity.user.UserAcountHistory;
import com.keith.modules.service.user.UserAcountHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.R;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 用户账户历史表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-08 14:36:29
 */
@Api(tags = "用户账户历史表")
@RequestMapping("user/acountHistory")
@RestController
public class UserAcountHistoryController {
    @Autowired
    private UserAcountHistoryService userAcountHistoryService;



    /**
     * 单笔订单收益计算
     */
//    @ApiOperation("单笔订单收益计算")
    @PostMapping("/orderEarnings")
    public Result orderEarnings(@RequestParam(name = "orderId",required = false)Long orderId){
        Result result = new Result();
        userAcountHistoryService.orderEarnings(orderId);
        return result;
    }
    /**
     * 获取返现记录
     */
    @Login
    @ApiOperation("获取返现记录")
    @PostMapping("/getList")
    public Result<PageUtils> getList(@RequestBody PageDTO pageDTO,@ApiIgnore@RequestAttribute("userId") long userId){
        Result result = new Result();

        return result.ok(userAcountHistoryService.getPage(pageDTO,userId));
    }

}
