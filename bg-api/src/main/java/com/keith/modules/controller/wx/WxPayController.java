package com.keith.modules.controller.wx;

import com.alibaba.fastjson.JSONObject;
import com.core.util.SnowflakeIdWorker;
import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.dto.wxpay.WeiXinEntPayDTO;
import com.keith.modules.dto.wxpay.WeiXinPayDTO;
import com.keith.modules.dto.wxpay.WirhDrawDTO;
import com.keith.modules.entity.bank.BankCat;
import com.keith.modules.entity.withdraw.BankDTO;
import com.keith.modules.service.wx.EntPayInfo;
import com.keith.modules.service.wx.WeiXinPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wxPay")
@Api(tags = "微信支付管理")
public class WxPayController {
    private static final Logger log = LoggerFactory.getLogger(WxPayController.class);
    @Autowired
    private WeiXinPayService weiXinPayService;
    Result result=new Result();
    JSONObject obj=new JSONObject();


    @Login
    @PostMapping("/WxPay")
    @ApiOperation("微信支付")
    public Result WxPay(@RequestBody @Validated @ApiParam(value = "统一下单") WeiXinPayDTO weiXinPayDTO, @ApiIgnore @RequestAttribute("userId") long userId, HttpServletRequest request) throws Exception{
        obj=weiXinPayService.createUnifiedOrder(userId,weiXinPayDTO,request);
        return  result.ok(obj);
    }

    //@Login
    @PostMapping("/getWxpayNotifyInfo")
    //@ApiOperation("微信支付回调")
    public Result getWxpayNotifyInfo(  HttpServletRequest request) throws Exception{
        //String=weiXinPayService.getWxpayNotifyInfo(request);
        return  result.ok(weiXinPayService.getWxpayNotifyInfo(request));
    }

    @Login
    @PostMapping("/getWxpayRefund")
    @ApiOperation("微信退款")
    public Result getWxpayRefund( @RequestBody @Validated @ApiParam(value = "订单编号") String orderSn, @ApiIgnore @RequestAttribute("userId") long userId,HttpServletRequest request) throws Exception{
        return  result.ok(weiXinPayService.getWxpayRefund(userId,orderSn, request));
    }

    @Login
    @PostMapping("/getEntPay")
   // @ApiOperation("提现")
    public Result getEntPay(@RequestBody @Validated @ApiParam(value = "提现") WeiXinEntPayDTO weiXinEntPayDTO,  @ApiIgnore @RequestAttribute("userId")long userId, HttpServletRequest request) throws Exception{
        obj=weiXinPayService.getEntPay(userId,weiXinEntPayDTO,request);
        return  result.ok(obj);
    }

    @Login
    @PostMapping("/addWithDraw")
    @ApiOperation("提现提现记录")
    public Result addWithDraw(@RequestBody @Validated @ApiParam(value = "提现") WirhDrawDTO wirhDrawDTO, @ApiIgnore @RequestAttribute("userId") long userId) throws Exception{
        obj=weiXinPayService.addWithDraw(userId,wirhDrawDTO);
        return  result.ok(obj);
    }

    @Login
    @PostMapping("/addBank")
    @ApiOperation("添加银行卡")
    public Result addBank(@RequestBody @Validated @ApiParam(value = "提现") BankDTO bankDTO, @ApiIgnore @RequestAttribute("userId") long userId) throws Exception{
        obj=weiXinPayService.addBank(userId,bankDTO);
        return  result.ok(obj);
    }


    @PostMapping("/getBank")
   // @ApiOperation("提现到银行卡")
    public Result addWithDraw(@RequestBody @Validated @ApiParam(value = "提现") BankDTO bankDTO) throws Exception{
        obj=weiXinPayService.getBank(bankDTO);
        return  result.ok(obj);
    }


    @GetMapping("/findAll")
    @ApiOperation("查询银行卡")
    public Result<BankCat> findAll() throws Exception{
        List<BankCat> list=weiXinPayService.findAll();
        return  result.ok(list);
    }

}
