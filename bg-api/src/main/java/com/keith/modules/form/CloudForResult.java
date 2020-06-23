package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lzy
 * @date 2020/6/9 18:59
 */
@Data
public class CloudForResult {
    @ApiModelProperty(value = "商品id")
    private Long productId;
    @ApiModelProperty(value = "商品名字")
    private String  productName;
    @ApiModelProperty(value = "图片")
    private String pic;
//    @ApiModelProperty(value = "规格")
//    private List skus;
    @ApiModelProperty(value = "剩余多少件")
    private Long stock;
    @ApiModelProperty(value = "规格值--null时为统一规格")
     private  String skuValue;
    @ApiModelProperty(value = "云仓活动的价格")
    private BigDecimal acitivityPrice;
    @ApiModelProperty(value = "运费模板id")
    private Long templateId;
    @ApiModelProperty(value = "平台进货价格")
    private BigDecimal platformPrice;
    @ApiModelProperty(value = "这是销售价格")
    private  BigDecimal platformSalePrice;
}
