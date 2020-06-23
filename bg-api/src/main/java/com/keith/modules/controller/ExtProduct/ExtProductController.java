package com.keith.modules.controller.ExtProduct;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.form.ExtProResult;
import com.keith.modules.service.ExtProduct.ExtProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 铺货商品表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-05 14:52:58
 */
@RestController
@RequestMapping("product/ext")
@Api(tags = {"铺货"})
public class ExtProductController {
    @Autowired
    private ExtProductService extProductService;


    Map<String, Object> map=new HashMap<>();

//    @Login
//    @PostMapping("/extProduct")
//    @ApiOperation("铺货,默认 （需要token）")
//    public Result extProduct(@RequestParam(value = "productId")@ApiParam(value = "商品的id",required = true)Long productId,
//                            @RequestParam(value = "skuId")@ApiParam(value = "商品的规格id，list",required = true)List<Long> skuId,
//                            @ApiIgnore @RequestAttribute("userId") long userId,@RequestParam(value = "type") int type){
//        extProductService.extProduct(productId,skuId,userId);
//        return result.ok("success");
//    }
//
//    @Login
//    @PostMapping("/findProInfo")
//    @ApiOperation("铺货,选择不默认时编辑 （需要token）")
//    public Result<ExtProResult> findProInfo(@RequestParam(value = "productId")@ApiParam(value = "商品的id",required = true)Long productId,
//                             @ApiIgnore @RequestAttribute("userId") long userId){
//        ExtProResult proInfo = extProductService.findProInfo(productId);
//        return result.ok(proInfo);
//    }
//
//
    @Login
    @PostMapping("/extProductChoose")
    @ApiOperation("铺货，编辑更新")
    public Result extProductChoose(@RequestBody  @ApiParam("商品信息") ExtProResult extProResult,@ApiIgnore @RequestAttribute("userId") long userId){
        Result result=new Result();
        extProductService.updateExtProduct(extProResult, userId);
        return result.ok("success");
    }


    @Login
    @PostMapping("/extProductNormal")
    @ApiOperation("铺货")
    public Result  extProductNormal(@RequestBody @ApiParam("商品id") List<Long> proIds, @ApiIgnore @RequestAttribute("userId") long userId){
        Result result=new Result();
      ExtProResult  extResult = extProductService.extProductNormal(proIds,userId);
      return result.ok(extResult);
    }


    @Login
    @GetMapping("/findExtProduct")
    @ApiOperation("铺货列表")
    /**
     * @status 商品货物状态  2-仓库中/已售空 0-铺货中  1-已下架
     * @type   0-已售空  1-仓库中  2-铺货中/已下架
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页码，从1开始",paramType = "query",required = true,dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量，默认10",paramType = "query",required = false,dataType = "int"),
            @ApiImplicitParam(name = "userId",value = "用户id",paramType = "query",required = true,dataType = "string"),
            @ApiImplicitParam(name = "status",value = " 商品货物状态  2-仓库中/已售空 0-铺货中  1-已下架",paramType = "query",required = false,dataType = "int"),
            @ApiImplicitParam(name = "type",value = "商品货物类型 0-已售空  1-仓库中  2-铺货中/已下架",paramType = "query",required = true,dataType = "string")
    })
    public Result findExtProduct(int currentPage,int pageSize,@ApiIgnore @RequestAttribute("userId")long userId,int status,int type){
        Result result=new Result();
        Page page = extProductService.findProductNormal(currentPage,pageSize,userId,status,type);
        return result.ok(page);
    }

    @Login
    @DeleteMapping("/delete")
    @ApiOperation("删除铺货商品")
    public Result deleteProduct(@RequestParam("productId")@ApiParam("商品id") long productId,@ApiIgnore @RequestAttribute("userId")long userId){
        ExtProduct extProduct = new ExtProduct();
        extProduct.setProductId(productId);
        extProduct.setDeleteStatus(1);
        ExtProduct e = extProductService.getOne(new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getProductId,productId).eq(ExtProduct::getUserMemberId,userId));
        if (e ==null){
            return new Result().error("商品不存在");
        }

        if (e.getCloudStatus()==1){
            return new Result().error("云仓商品不能删除");
        }
        extProduct.setId(e.getId());
        extProductService.updateById(extProduct);
        return new Result().ok("success");
    }

}
