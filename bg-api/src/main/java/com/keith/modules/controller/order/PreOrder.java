package com.keith.modules.controller.order;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.dto.order.OrderDTO;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.order.PreOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lzy
 * @date 2020/6/17 16:07
 */
@RestController
@RequestMapping("/preOrder")
@Api(tags = {"预售订单"})
public class PreOrder {

    @Autowired
            private PreOrderService preOrderService;

    Map<String, Object> map=new HashMap<>();
    Result result=new Result();
    @Login
    @PostMapping(value = "/createPreOrder")
    @ApiOperation("生成预售订单 （需要token）")
    public Result createPreOrder(@RequestBody @Validated @ApiParam(value = "订单") OrderDTO orderDTO,
                                 @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String, Object> preOrder = preOrderService.createPreOrder(orderDTO,userId);
        return result.ok(preOrder);
    }

    @Login
    @PostMapping(value = "/payAmount")
    @ApiOperation("付尾款订单 （需要token）")
    public Result payAmount(@RequestParam @Validated @ApiParam(value = "订单号") String orderSn,
                                 @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String, Object> preOrder = preOrderService.payAmount(orderSn,userId);
        return result.ok(preOrder);
    }

    @Login
    @PostMapping(value = "/findByOrdersn")
    @ApiOperation("查看预售订单详情 （需要token）")
    public Result findByOrdersn(@RequestParam @Validated @ApiParam(value = "订单号") String orderSn,
                            @ApiIgnore @RequestAttribute("userId") long userId){
        OrderOrder byOrdersn = preOrderService.findByOrdersn(orderSn,userId);
        return result.ok(byOrdersn);
    }


}
