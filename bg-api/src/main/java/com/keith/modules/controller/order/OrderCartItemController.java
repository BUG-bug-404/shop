package com.keith.modules.controller.order;


import com.keith.annotation.Login;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Result;
import com.keith.modules.entity.order.OrderCartItem;
import com.keith.modules.form.AddCartSelect;
import com.keith.modules.service.order.OrderCartItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 进货车表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:26:54
 */
@RestController
@RequestMapping("product/cart")
@Api(tags = {"进货车"})
public class OrderCartItemController {
    @Autowired
    private OrderCartItemService orderCartItemService;
    Map<String, Object> map=new HashMap<>();
    Result result=new Result();


    @Login
    @PostMapping("/add")
    @ApiOperation("添加到进货车 （需要token）")
    public Result add(@RequestBody AddCartSelect addCartSelect,
                      @ApiIgnore @RequestAttribute("userId") long userId){
        if(addCartSelect != null){
            Boolean add = orderCartItemService.add(addCartSelect.getProductId(), addCartSelect.getSkuId(), addCartSelect.getCount(), userId,addCartSelect.getType());
            return   result.ok(add);
        }
        return result.error();



    }

    @Login
    @PostMapping("/deleteCount")
    @ApiOperation("从进货车中删除数量 （需要token）")
    public Result deleteCount(@RequestBody AddCartSelect addCartSelect,
                              @ApiIgnore @RequestAttribute("userId") long userId){
        boolean deleteCount = orderCartItemService.deleteCount(addCartSelect.getProductId(), addCartSelect.getSkuId(), addCartSelect.getCount(), userId);
        return   result.ok(deleteCount);
    }

    @Login
    @GetMapping("/findAll")
    @ApiOperation("当前用户进货车里的商品, （需要token）")
    public Result<OrderCartItem> findAll(@RequestParam(value = "currentPage",required = false,defaultValue = "1") Integer currentPage,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "status")@ApiParam(value = "传入0未失效的1->失效的商品") Integer status,
                                         @ApiIgnore @RequestAttribute("userId") long userId){
        PageUtils orderCartItemServiceAll = orderCartItemService.findAll(currentPage, pageSize, userId,status);
        return   result.ok(orderCartItemServiceAll);
    }

    @Login
    @PostMapping("/deleteByIds")
    @ApiOperation("批量传入进货车id删除进货车里的, （需要token）")
    public Result deleteByIds(@RequestBody @ApiParam(value = "数组，传一个删除单个",required = true)List<Long> idList,
                              @ApiIgnore @RequestAttribute("userId") long userId){

        boolean deleteByIds = orderCartItemService.deleteByIds(idList, userId);
        return  result.ok(deleteByIds);
    }


    @Login
    @PostMapping("/updateSku")
    @ApiOperation("修改进货车商品的规格 （需要token）")
    public Result updateSku(@RequestBody AddCartSelect addCartSelect,
                            @ApiIgnore @RequestAttribute("userId") long userId){
        boolean updateSku = orderCartItemService.updateSku(addCartSelect.getId(), addCartSelect.getProductId(),
                addCartSelect.getSkuId(), userId,addCartSelect.getReSkuId(),addCartSelect.getCount());
        return  result.ok(updateSku);
    }

}
