package com.keith.modules.dto.cloud;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Lzy
 * @date 2020/6/12 11:22
 */
@Data
public class CloudUserSkuInfo {
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    @ApiModelProperty(value = "规格值")
    private String skuValue;

    @ApiModelProperty(value = "规格回收件数")
    private Integer skuCount;

    @ApiModelProperty(value = "规格回收价格--单价")
    private BigDecimal skuPrice;

    @ApiModelProperty(value = "规格回收折扣")
    private BigDecimal skuDisCount;

    @ApiModelProperty(value = "此规格活动价格---原价格")
    private BigDecimal activityPrice;
}
