package com.keith.modules.controller.sub;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import com.keith.common.utils.Result;
import com.keith.modules.dto.sub.SubAccountPayDTO;
import com.keith.modules.dto.sub.SubShopDTO;
import com.keith.modules.service.sub.SubAccountPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 分账订单支付表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:11
 */
@RestController
@RequestMapping("sub/subaccountpay")
@Api(tags = "微信分账支付管理")
public class SubAccountPayController {
    @Autowired
    private SubAccountPayService subAccountPayService;
    Result result=new Result();
    JSONObject obj=new JSONObject();

    @PostMapping("/createOrder_jsapi")
    @ApiOperation("分账")
    public Result createOrder_jsapi(@RequestBody @Validated @ApiParam(value = "统一下单") SubAccountPayDTO subAccountPayDTO, HttpServletRequest request) throws Exception{
        obj=subAccountPayService.createOrder_jsapi(subAccountPayDTO,request);
        return  result.ok(obj);
    }
    @PostMapping("/testProfitSharing")
    //@ApiOperation("分账回调")
    public Result testProfitSharing(  HttpServletRequest request) throws Exception{
        //String=weiXinPayService.getWxpayNotifyInfo(request);
        return  result.ok(subAccountPayService.testProfitSharing(request));
    }


    @PostMapping("/addSubShop")
    @ApiOperation("添加分账用户")
    public Result addSubShop(@RequestBody @Validated @ApiParam(value = "添加分账用户") SubShopDTO subShopDTOList) throws Exception{
        obj=subAccountPayService.addSubShop(subShopDTOList);
        return  result.ok(obj);
    }

}
