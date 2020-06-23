package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Lzy
 * @date 2020/6/11 17:48
 */
@Data
public class GetGoodsSelect {
    @ApiModelProperty(value = "邮费金额")
    private BigDecimal freightAmount;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "规格id")
    private Long skuId;

    @ApiModelProperty(value = "代发货数量")
    private Integer count;

    @ApiModelProperty(value="收货地址id")
    private Long addressId;

}
