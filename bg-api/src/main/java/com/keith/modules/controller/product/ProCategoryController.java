package com.keith.modules.controller.product;
import com.keith.common.utils.Result;
import com.keith.modules.entity.product.ProCategory;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keith.modules.service.product.ProCategoryService;
import io.swagger.annotations.Api;

import java.util.List;


/**
 * 商品分类表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-02 13:38:48
 */
@RestController
@RequestMapping("product/procategory")
@Api(tags = {"商品分类"})
public class ProCategoryController {
    @Autowired
    private ProCategoryService proCategoryService;

    @GetMapping("/findAll")
    @ApiOperation("查询一级分类二级分类")
    public Result findAll() {
        List<ProCategory> proCategoryEntityList = proCategoryService.findAll();
        Result result = new Result();
        return result.ok(proCategoryEntityList);
    }



}
