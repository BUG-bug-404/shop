package com.keith.modules.controller.order;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.dto.order.*;
import com.keith.modules.entity.order.Order;
import com.keith.modules.service.order.OrderOrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
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
@Api(tags = "订单")
public class OrderOrderController {
    @Autowired
    private OrderOrderService orderOrderService;

    Map<String, Object> map=new HashMap<>();
    Result result=new Result();
    @Login
    @PostMapping("/commitOrder")
    @ApiOperation("提交订单 （需要token）")
    public Result commitOrder(@RequestBody @Validated @ApiParam(value = "订单") List<OrderDTO> orderDTO, @ApiIgnore @RequestAttribute("userId") long userId){
        map=orderOrderService.createOrder(orderDTO,userId);
        return result.ok(map);
    }


    @Login
    @PostMapping("/commitLevelA")
    @ApiOperation("提交会员等级购买铜牌订单 （需要token）")
    public Result commitLevelA(@ApiIgnore @RequestAttribute("userId") long userId){
        map=orderOrderService.createOrderLevelA(userId);
        return result.ok(map);
    }

    @Login
    @PostMapping("/commitLevelB")
    @ApiOperation("提交会员等级购买银牌订单 （需要token）")
    public Result commitLevelB(@ApiIgnore @RequestAttribute("userId") long userId){
        map=orderOrderService.createOrderLevelB(userId);
        return result.ok(map);
    }
    @Login
    @PostMapping("/commitLevelC")
    @ApiOperation("提交会员等级购买金牌订单 （需要token）")
    public Result commitLevelC(@ApiIgnore @RequestAttribute("userId") long userId){
        map=orderOrderService.createOrderLevelC(userId);
        return result.ok(map);
    }

    @Login
    @PostMapping("/findAllOrder")
    @ApiOperation("订单中心 （需要token）")
    public Result<Order> findAllOrder(@RequestBody @Validated @ApiParam(value = "订单") MyOrderDTO orderDTO, @ApiIgnore @RequestAttribute("userId") long userId){
        List<Order> list=orderOrderService.findAllOrder(orderDTO,userId);
        return result.ok(list);
    }

    @Login
    @GetMapping("/findOrderByExt")
    @ApiOperation("查询第三方订单")
    public Result<Order> findOrderByExt(@RequestParam("current")Integer page, @RequestParam("size")Integer size,@ApiParam("订单状态 0未发 1已发")@RequestParam("type")Integer type, @RequestParam("date") Integer date, @ApiIgnore @RequestAttribute("userId")long userId){
        return result.ok(orderOrderService.findOrderByExt(page,size,type,date,userId));
    }

    @Login
    @PostMapping("/feightMoney")
    @ApiOperation("查询运费 （需要token）")
    public Result feightMoney(@RequestBody @Validated @ApiParam(value = "运费信息") FeightDTO feightDTO, @ApiIgnore @RequestAttribute("userId") long userId){
        map=orderOrderService.feightMoney(feightDTO,userId);
        return result.ok(map);
    }


    @Login
    @GetMapping("/findByOne")
    @ApiOperation("订单详情 （需要token）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType="String")
    })
    public Result findByOne( @RequestParam String orderSn,@ApiIgnore @RequestAttribute("userId") long userId){
        Order order=orderOrderService.findByOne(orderSn,userId);
        return result.ok(order);
    }

    @Login
    @PostMapping("/orderTrack")
    @ApiOperation("订单跟踪 （需要token）")
    public Result orderTrack(@RequestBody @Validated @ApiParam(value = "运费信息") OrderTrackDTO orderTrackDTO, @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String,Object> list=orderOrderService.orderTrack(orderTrackDTO,userId);
        return result.ok(list);
    }
    @Login
    @PostMapping("/endOrder")
    @ApiOperation("订单完成 （需要token）")
    public Result orderTrack(@RequestBody @Validated @ApiParam(value = "运费信息") EndOrderDTO endOrderDTO, @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String,Object> list=orderOrderService.endOrder(endOrderDTO,userId);
        return result.ok(list);
    }

}
