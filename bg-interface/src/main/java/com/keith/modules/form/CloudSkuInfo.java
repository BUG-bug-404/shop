package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Lzy
 * @date 2020/6/9 11:20
 */
@Data
public class CloudSkuInfo {

    @ApiModelProperty(value = "规格id")
    private Long skuId;

    @ApiModelProperty(value = "几个起售-->活动数量")
    private Integer actCount;

    @ApiModelProperty(value = "云仓活动价格")
    private BigDecimal actPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "规格属性sp2")
    private String skuValue;

}
