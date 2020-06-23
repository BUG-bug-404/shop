package com.keith.modules.controller.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keith.annotation.Sign;
import com.keith.common.utils.Result;
import com.keith.modules.dto.order.FeightDTO;
import com.keith.modules.dto.order.OrderDTO;
import com.keith.modules.entity.app.UserMemberApp;
import com.keith.modules.entity.order.Order;
import com.keith.modules.service.app.AppUserService;
import com.keith.modules.service.order.OrderOrderService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 订单表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
@RestController
@RequestMapping("order/orderorder")
public class OrderOrderController {
    @Autowired
    private OrderOrderService orderOrderService;

    @Autowired
    private AppUserService appUserService;

    Map<String, Object> map=new HashMap<>();
    Result result=new Result();

    @Sign
    @PostMapping("/commitOrder")
    public Result commitOrder(@RequestBody @Validated @ApiParam(value = "订单") OrderDTO orderDTO){
        UserMemberApp user = appUserService.getOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getAppId, orderDTO.getAppId()));
        map=orderOrderService.createOrder(orderDTO.getOrders(),orderDTO.getType(),user.getUserMemberId());
        return result.ok(map);
    }

    @Sign
    @PostMapping("/feightMoney")
    public Result feightMoney(@RequestBody @Validated @ApiParam(value = "运费信息") FeightDTO feightDTO){
        UserMemberApp user = appUserService.getOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getAppId, feightDTO.getAppId()));
        map=orderOrderService.feightMoney(feightDTO,user.getUserMemberId());
        return result.ok(map);
    }


    @Sign
    @PostMapping("/findByOne")
    public Result findByOne( @RequestParam String orderSn,String appId){
        UserMemberApp user = appUserService.getOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getAppId, appId));
        Order order=orderOrderService.findByOne(orderSn,user.getUserMemberId());
        return result.ok(order);
    }

}
