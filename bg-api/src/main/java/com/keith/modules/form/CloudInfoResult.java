package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Lzy
 * @date 2020/6/9 9:49
 */
@Data
public class CloudInfoResult {
    @ApiModelProperty(value = "商品编号")
    private Long productId;

    @ApiModelProperty(value = "商品标题")
    private String productName;

    @ApiModelProperty(value = "商品图片们")
    private List<String> pic;

    @ApiModelProperty(value = "云仓商品规格参数们")
    private List sku;

    @ApiModelProperty("规格们")
    private List<SkuStockEntityList> skuStockEntityLists;

    @ApiModelProperty(value = "运费模板id")
    private Long templateId;
    @ApiModelProperty(value = "商品所在地区")
    private String area;
    @ApiModelProperty(value = "分类")
    private String category;
//    @ApiModelProperty(value = "已出售")
//    private Long count;
    @ApiModelProperty(value = "库存")
    private Integer stock;
    @ApiModelProperty(value = "其他属性")
    private List<Map<String,String>> attr;
    @ApiModelProperty(value = "货号")
    private String productCode;
    @ApiModelProperty(value = "商品展示")
    private String detail;
    @ApiModelProperty(value = "回收规则-->时间,折扣")
    private CloudRepoInfo repoDetail;



}
