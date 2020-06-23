package com.keith.modules.controller.user;

import com.keith.annotation.Login;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Result;
import com.keith.modules.entity.user.UserCollectItem;
import com.keith.modules.form.AddCartSelect;
import com.keith.modules.service.user.UserCollectItemService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;


/**
 * 收藏表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:41:56
 */
@RestController
@RequestMapping("product/colletion")
@Api(tags = {"收藏夹"})
public class UserCollectItemController {
    @Autowired
    private UserCollectItemService userCollectItemService;


    Map<String, Object> map=new HashMap<>();
    Result result=new Result();
    @Login
    @PostMapping("/addOrDelete")
    @ApiOperation("添加到收藏夹,如果已在收藏夹，则取消 （需要token）")
    public Result addOrDelete(@RequestBody AddCartSelect addCartSelect,
                              @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String,Object> colletion = userCollectItemService.setColletion(addCartSelect.getProductId(), userId);
            return result.ok(colletion);
    }

    @Login
    @GetMapping("/status")
    @ApiOperation("查看是否在收藏夹,data返回true在收藏夹，false不在 （需要token）")
    public Result status(@RequestParam(value = "productId",required = true) Long productId,
                              @ApiIgnore @RequestAttribute("userId") long userId){
        Boolean status = userCollectItemService.status(productId, userId);
        if(status) {
            return result.ok("true");
        }
        return result.ok("false");
    }


    @Login
    @GetMapping("/findAll")
    @ApiOperation("分页查询用户收藏夹 （需要token）")
    public Result<UserCollectItem> findAll(@RequestParam(value = "currentPage",required = false,defaultValue = "1") Integer currentPage,
                                           @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                           @ApiIgnore @RequestAttribute("userId") long userId){
        PageUtils all = userCollectItemService.findAll(currentPage, pageSize,userId);
        if(all != null){
            return result.ok(all);
        }else {
            return result.error();
        }
    }

}
