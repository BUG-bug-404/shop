package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lzy
 * @date 2020/6/6 17:46
 */
@Data
public class ExtSkusSelect {
    /*这是选择的规格*/
    @ApiModelProperty(value = "填入->规格id,当你是批量修改时，传多个，不批量，单个则分")
    private List<Long> skuIds;

    @ApiModelProperty(value = "填入->设置售价")
    private BigDecimal price;

    @ApiModelProperty(value = "查出来的->库存")
    private Integer stock;

    @ApiModelProperty(value = "查出来的->拿货价")
    private BigDecimal platPrice;

    @ApiModelProperty(value = "查出来的->规格ID")
    private Long skuId;

    @ApiModelProperty(value = "查出来的->规格详情")
    private String skuInfo;

    @ApiModelProperty(value = "铺货查出来->铺货规格ID")
    private String extSkuId;


}
