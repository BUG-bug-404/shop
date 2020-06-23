package com.keith.modules.controller.product;



import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Result;
import com.keith.modules.entity.product.ConWelfare;
import com.keith.modules.service.product.ConWelfareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * 福利商品
 *
 * @author lzy
 * @email ********
 * @date 2020-06-13 14:55:11
 */
@RestController
@RequestMapping("product/conwelfare")
@Api(tags = {"新人福利商品"})
public class ConWelfareController {
    @Autowired
    private ConWelfareService conWelfareService;


    Result result=new Result();
    @PostMapping(value = {"/list"})
    @ApiOperation(value = "查询福利商品")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public Result<ConWelfare> list(@RequestParam(value = "currentPage",required = false,defaultValue = "1") Integer currentPage,
                       @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        PageUtils pageUtils = conWelfareService.list(currentPage, pageSize);
        return result.ok(pageUtils);
    }




}
