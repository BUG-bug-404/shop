package com.keith.modules.controller.product;

import com.keith.common.utils.Result;
import com.keith.modules.entity.product.ProCategory;
import com.keith.modules.service.product.ProCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class ProCategoryController {
    @Autowired
    private ProCategoryService proCategoryService;

    @GetMapping("/findAll")
    public Result findAll(){
        List<ProCategory> proCategoryEntityList = proCategoryService.findAll();
        Result result=new Result();
        if(proCategoryEntityList != null && proCategoryEntityList.size() > 0){
            return result.ok(proCategoryEntityList);
        }
        return result.ok("操作有误！");
    }


}
