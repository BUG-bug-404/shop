package com.keith.modules.controller.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.common.utils.Result;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.service.product.ProProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author Lzy
 * @date 2020/6/2 19:43
 */

@RestController
@RequestMapping("cargo/product")
@Api(tags = {"货源"})
public class CargoController {

    @Autowired
    private ProProductService proProductService;

    @GetMapping("/find")
    @ApiOperation("货源筛选")
    public Result<Page<ProProduct>> find(@RequestParam(value ="lowPrice",required = false)@ApiParam(value = "价格区间")BigDecimal lowPrice,
                                         @RequestParam(value ="highPrice",required = false)@ApiParam(value = "价格区间")BigDecimal highPrice,
                                         @RequestParam(value ="categoryId",required = false)@ApiParam(value = "分类id")Long categoryId,
                                         @RequestParam(value ="sortType",required = false)@ApiParam(value = "排序类型，排序类型，不填为全部->按上架时间排序倒序，0->销量1->浏览量2->铺货量3->新品4->订单量")Long sortType,
                                         @RequestParam(value ="currPage",required = false,defaultValue = "1")Integer currPage,
                                         @RequestParam(value ="pageSize",required = false,defaultValue = "10")Integer pageSize){
        Page<ProProduct> proList = proProductService.getProList(lowPrice,highPrice,categoryId,sortType,currPage,pageSize);
        Result<Page<ProProduct>> result=new Result<Page<ProProduct>>();
            return result.ok(proList);
    }
}
