package com.keith.modules.controller.wx;

import com.alibaba.fastjson.JSONObject;
import com.keith.annotation.Sign;
import com.keith.common.utils.Result;
import com.keith.modules.dto.wxpay.WeiXinEntPayDTO;
import com.keith.modules.dto.wxpay.WeiXinPayDTO;
import com.keith.modules.dto.wxpay.WirhDrawDTO;
import com.keith.modules.service.wx.WeiXinPayService;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("wxPay")
public class WxPayController {
    private static final Logger log = LoggerFactory.getLogger(WxPayController.class);
    @Autowired
    private WeiXinPayService weiXinPayService;
    Result result=new Result();
    JSONObject obj=new JSONObject();


    @Sign
    @PostMapping("/WxPay")
    public Result WxPay(@RequestBody @Validated @ApiParam(value = "统一下单") WeiXinPayDTO weiXinPayDTO, @ApiIgnore @RequestAttribute("userId") long userId, HttpServletRequest request) throws Exception{
        obj=weiXinPayService.createUnifiedOrder(userId,weiXinPayDTO,request);
        return  result.ok(obj);
    }

    //@Sign
    @PostMapping("/getWxpayNotifyInfo")
    public Result getWxpayNotifyInfo(  HttpServletRequest request) throws Exception{
        //String=weiXinPayService.getWxpayNotifyInfo(request);
        return  result.ok(weiXinPayService.getWxpayNotifyInfo(request));
    }

    @Sign
    @PostMapping("/getWxpayRefund")
    public Result getWxpayRefund( @RequestBody @Validated @ApiParam(value = "订单编号") String orderSn, @ApiIgnore @RequestAttribute("userId") long userId,HttpServletRequest request) throws Exception{
        return  result.ok(weiXinPayService.getWxpayRefund(userId,orderSn, request));
    }


    @Sign
    @PostMapping("/getEntPay")
    public Result getEntPay(@RequestBody @Validated @ApiParam(value = "提现") WeiXinEntPayDTO weiXinEntPayDTO,  long userId, HttpServletRequest request) throws Exception{
        obj=weiXinPayService.getEntPay(userId,weiXinEntPayDTO,request);
        return  result.ok(obj);
    }


    @PostMapping("/addWithDraw")
    public Result addWithDraw(@RequestBody @Validated @ApiParam(value = "提现") WirhDrawDTO wirhDrawDTO, long userId) throws Exception{
        obj=weiXinPayService.addWithDraw(userId,wirhDrawDTO);
        return  result.ok(obj);
    }




}
