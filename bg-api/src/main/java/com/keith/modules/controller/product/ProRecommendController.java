package com.keith.modules.controller.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.annotation.Login;
import com.keith.common.exception.ErrorCode;
import com.keith.common.utils.Result;
import com.keith.modules.service.product.ProRecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户商品推荐
 *
 * @author gray
 * @version 1.0
 * @date 2020/6/9 10:02
 */
@RestController
@RequestMapping("/prorecommend")
@Api(tags = {"商品推荐"})
public class ProRecommendController {


    @Autowired
    private ProRecommendService proRecommendService;

    Result result = new Result();

    @Login
    @GetMapping("/recommend")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页码，从1开始",paramType = "query",required = true,dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量，默认10",paramType = "query",required = false,dataType = "int"),
            @ApiImplicitParam(name = "type",value = "推荐商品类型 1:收藏夹推荐",paramType = "query",required = true,dataType = "int"),
            @ApiImplicitParam(name = "userId",value = "用户id",paramType = "query",required = true,dataType = "int")
    })
    @ApiOperation("商品推荐")
    /**
     * 商品推荐查询，暂时只有 收藏夹推荐，预留case 2 3方法
     */
    public Result findRecommend(@Param("type") int type, @ApiIgnore @RequestAttribute("userId") long userId, @Param("currentPage") int currentPage, @Param("pageSize")int pageSize){
        if (pageSize==0){
            pageSize = 10;
        }
        if (userId==0){
            return result.error(ErrorCode.NOT_NULL);
        }
        switch (type){
            case 1:
                Page page = proRecommendService.findRecommendByCollect(currentPage,pageSize,userId);
                return  result.ok(page);
            case 2:break;
            case 3:break;
            default:break;
        }
        return result.ok("");
    }

}
