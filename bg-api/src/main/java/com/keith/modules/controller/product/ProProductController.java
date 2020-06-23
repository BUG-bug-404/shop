package com.keith.modules.controller.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.common.exception.ErrorCode;
import com.keith.common.utils.Result;
import com.keith.modules.entity.product.ProProduct;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.keith.modules.service.product.ProProductService;

import java.util.List;

/**
 *
 *
 * @author lijinxinag
 * @email ********
 * @date 2020-06-02 10:02:30
 */
@RestController
@RequestMapping("product/proproduct")
@Api(tags = {"首页"})
public class ProProductController {
    @Autowired
    private ProProductService proProductService;

    Result result=new Result();

    @GetMapping("/findAll")
    @ApiOperation("首页先进市场")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true,dataType="int"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", paramType = "query", required = true,dataType="int")
    })
    public Result<ProProduct> findAll(@RequestParam int page, @RequestParam int pageSize) throws Exception{
        List<ProProduct> list =proProductService.findAll(page,pageSize);
        return  result.ok(list);
    }

    @GetMapping("/findById")
    @ApiOperation("商品详情")
    public Result<ProProduct> findById(@RequestParam("parentId") @ApiParam(value = "parentId") Long parentId) throws Exception{
        ProProduct productEntity =proProductService.findById(parentId);
        return  result.ok(productEntity);
    }

    @GetMapping("/findByName")
    @ApiOperation("关键字查询商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页码，从1开始",paramType = "query",required = true,dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量，默认10",paramType = "query",required = false,dataType = "int"),
            @ApiImplicitParam(name = "name",value = "查询商品名",paramType = "query",required = true,dataType = "string")
    })
    public Result findByName(@RequestParam int currentPage,@RequestParam int pageSize,@RequestParam("name") String name) throws Exception{
        /**
         * 判断是否上传每页数量，未上传提供默认值
         */
        if (pageSize==0){
            pageSize=10;
        }
        if (name==null || name.trim().equals("")){
            return result.error(ErrorCode.NOT_NULL);
        }
        Page<ProProduct> productList = proProductService.findByName(currentPage,pageSize,name);
        return result.ok(productList);
    }


}
