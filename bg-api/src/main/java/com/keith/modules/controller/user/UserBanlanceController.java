package com.keith.modules.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.dto.user.UserBanlanceDTO;
import com.keith.modules.service.user.UserBanlanceService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;


/**
 * 用户余额表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-02 15:36:25
 */
@RestController
@RequestMapping("/userbanlance")
@Api(tags = {"用户钱包余额"})
@Slf4j
public class UserBanlanceController {
    @Autowired
    private UserBanlanceService userBanlanceService;

    JSONObject json = new JSONObject();
    Result result = new Result();

    @Login
    @GetMapping(value = {"/find"})
    @ApiOperation(value = "")
    public Result<Map<String,BigDecimal>> find(@ApiIgnore @RequestAttribute("userId") long userId) throws Exception {
        return result.ok(userBanlanceService.getBanlance(userId));
    }

    @Login
    @PostMapping(value = {"/rechargeUser"})
    @ApiOperation(value = "充值")
    public Result rechargeUser(@RequestBody @Validated @ApiParam(value = "充值")UserBanlanceDTO banlanceDTO, Long userMemberId, HttpServletRequest request) {
        json = userBanlanceService.rechargeUser(banlanceDTO, userMemberId, request);
        return result.ok(json);
    }

    //微信充值回调
    @PostMapping("/wxPayNotifyPay")
    public Result wxPayNotifyPay(HttpServletRequest request) throws Exception {
        log.info("微信充值钻石回调，第三方异步通知");
        return result.ok(userBanlanceService.getWxpayNotifyInfo(request));
    }


}
