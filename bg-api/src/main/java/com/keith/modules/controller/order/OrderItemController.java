package com.keith.modules.controller.order;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.dto.order.OrderDTO;
import com.keith.modules.form.CountOrderPrice;
import com.keith.modules.form.CountPriceSelect;
import com.keith.modules.service.order.OrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单中所包含的商品
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
@RestController
@RequestMapping("order/orderItem")
@Api(tags = "算钱-------------------")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    Map<String, Object> map=new HashMap<>();
    Result result=new Result();
    @Login
    @PostMapping("/CountOrderPrice")
    @ApiOperation("算订单所需支付的钱 （需要token）")
    public Result CountOrderPrice(@RequestBody @Validated @ApiParam(value = "订单") List<CountOrderPrice> countOrderPriceList,
                                  @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String, Object> map = orderItemService.CountOrderPrice(countOrderPriceList, userId);
        return result.ok(map);
    }

    @Login
    @PostMapping("/countCartPrice")
    @ApiOperation("计算购物车价格 （需要token）")
    public Result countCartPrice(@RequestBody CountPriceSelect countPriceSelect, @ApiIgnore @RequestAttribute("userId") long userId){
//        Map<String, Object> map = orderItemService.CountOrderPrice(countOrderPriceList, userId);
        Map<String, Object> map = orderItemService.countCartPrice(countPriceSelect.getId(), userId, countPriceSelect.getAddressId());
        return result.ok(map);
    }




}
