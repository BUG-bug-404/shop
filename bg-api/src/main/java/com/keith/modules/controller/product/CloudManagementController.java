package com.keith.modules.controller.product;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.dto.cloud.CloudRepoCount;
import com.keith.modules.dto.cloud.CloudUserRepoInfo;
import com.keith.modules.entity.product.CloudManagement;
import com.keith.modules.form.*;
import com.keith.modules.service.product.CloudManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 云仓SKU数据表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-04 19:25:40
 */
@RestController
@RequestMapping("product/cloud")
@Api(tags = {"云仓"})
public class CloudManagementController {
    @Autowired
    private CloudManagementService cloudManagementService;

    Map<String, Object> map = new HashMap<>();
    Result result = new Result();

    @GetMapping("/findAll")
    @ApiOperation("云仓首页分页查询云仓全部上架的商品们")
    public Result<CloudResult> findAll(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Map<String, Object> cloudManagement = cloudManagementService.findAll(currentPage, pageSize);
        return result.ok(cloudManagement);
    }

    @GetMapping("/getByProduct")
    @ApiOperation("查询云仓商品的详情")
    public Result<CloudInfoResult> getById(@RequestParam(value = "productId", required = true) Long productId) {
        CloudInfoResult infoResult = cloudManagementService.getByProduct(productId);
        return result.ok(infoResult);
    }

    @GetMapping("/getAll")
    @ApiOperation("查询所有云仓商品")
    public Result<CloudManagement> getAll(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Map<String, Object> map = cloudManagementService.getAll(currentPage, pageSize);
        return result.ok(map);
    }

    @GetMapping("/getUserAll")
    @ApiOperation("查询当前用户所有云仓商品")
    @Login
    public Result<CloudForResult> getUserAll(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                             @ApiIgnore @RequestAttribute("userId") long userId
    ) {
        Map<String, Object> map = cloudManagementService.getUserAll(userId, currentPage, pageSize);
        return result.ok(map);
    }

    @PostMapping("/getInfo")
    @ApiOperation("进入代发货页面")
    @Login
    public Result<CloudForResult> getInfo(@RequestParam(value = "productId", required = true) Long productId,
                                          @RequestParam(value = "skuId", required = true) Long skuId,
                                             @ApiIgnore @RequestAttribute("userId") long userId
    ) {
        CloudForResult cloudForResult = cloudManagementService.getInfo(userId, productId, skuId);
        return result.ok(cloudForResult);
    }

    @PostMapping("/createCloudOrder")
    @ApiOperation("生成云仓订单--货物屯仓不需邮费以及收货地址id")
    @Login
    public Result<BuyCloudProSelect> createCloudOrder(@RequestBody @ApiParam List<BuyCloudProSelect> buyCloudProSelects,
                                   @ApiIgnore @RequestAttribute("userId") long userId){
        Map map = new HashMap();
        String order  = getOrderIdByUUId();
        buyCloudProSelects.stream().forEach(b->{
            Map<String, Object> cloudOrder = cloudManagementService.createCloudOrder(b, userId,order);
            map.putAll(cloudOrder);
        });
        return result.ok(map);
    }

    @Login
    @RequestMapping("/countRepoPrice")
    @ApiOperation("计算商品单规格回收价格")
    public Result<CloudRepoCount> countRepoPrice(@RequestBody AddCartSelect addCartSelect,
                                 @ApiIgnore @RequestAttribute("userId") long userId){
        List<Object> objects = cloudManagementService.countRepoPrice(addCartSelect.getProductId(), userId);
        return result.ok(objects);
    }

    @Login
    @PostMapping("/getGoods")
    @ApiOperation("代下单--")
    public Result<GetGoodsSelect> getGoods(@RequestBody @ApiParam GetGoodsSelect getGoodsSelect,
                                           @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String, Object> map = cloudManagementService.getGoods(getGoodsSelect, userId);
        return result.ok(map);
    }

    @Login
    @PostMapping("/createRepoOrder")
    @ApiOperation("回收申请--")
    public Result<CloudUserRepoInfo> createRepoOrder(@RequestBody @ApiParam List<CloudUserRepoInfo> cloudUserRepoInfos,
                                           @ApiIgnore @RequestAttribute("userId") long userId){
        Map<String, Object> map = cloudManagementService.createRepoOrder(cloudUserRepoInfos, userId);
        return result.ok(map);
    }

    @Login
    @GetMapping("/findRepoInfo")
    @ApiOperation("回收详情--")
    public Result<CloudUserRepoInfo> findRepoInfo(@RequestParam(value = "repoSn")@ApiParam(value = "单号",required = true)Long repoSn,
                                                     @ApiIgnore @RequestAttribute("userId") long userId){
        List<CloudUserRepoInfo> repoInfo = cloudManagementService.findRepoInfo(userId, repoSn);
        return result.ok(repoInfo);
    }

    /**
     * 创建订单编号
     *
     * @return
     */
    public static String getOrderIdByUUId() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(date);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return time + String.format("%011d", hashCodeV);
    }


}
