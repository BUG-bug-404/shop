package com.keith.modules.controller.ExtProduct;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.annotation.Sign;
import com.keith.common.utils.Result;
import com.keith.modules.entity.app.UserMemberApp;
import com.keith.modules.form.ExtProResult;
import com.keith.modules.service.ExtProduct.ExtProductService;
import com.keith.modules.service.app.AppUserService;
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
public class ExtProductController {
    @Autowired
    private ExtProductService extProductService;

    @Autowired
    private AppUserService appUserService;

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
    @Sign
    @PostMapping("/extProductChoose")
    public Result extProductChoose(@RequestBody ExtProResult extProResult){
        UserMemberApp user = appUserService.getOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getAppId, extProResult.getAppId()));
        Result result=new Result();
        extProductService.updateExtProduct(extProResult, user.getId());
        return result.ok("success");
    }


    @Sign
    @PostMapping("/extProductNormal")
    public Result  extProductNormal(@RequestBody List<Long> proIds,String appId,int type){
        UserMemberApp user = appUserService.getOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getAppId, appId));
        Result result=new Result();
        ExtProResult  extResult = extProductService.extProductNormal(proIds,user.getId(),type);
        return result.ok(extResult);
    }


    @Sign
    @GetMapping("/findExtProduct")
    /**
     * @status 商品货物状态  2-仓库中/已售空 0-铺货中  1-已下架
     * @type   0-已售空  1-仓库中  2-铺货中/已下架
     */
    public Result findExtProduct(int currentPage,int pageSize,@ApiIgnore @RequestAttribute("userId")String appId,int status,int type){
        UserMemberApp user = appUserService.getOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getAppId, appId));
        Result result = new Result();
        Page page = extProductService.findProductNormal(currentPage,pageSize,user.getId(),0,2);
        return result.ok(page);
    }
}
