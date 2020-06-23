package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lzy
 * @date 2020/6/10 13:51
 */
@Data
public class BuyCloudSelect {
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    @ApiModelProperty(value = "规格数量")
    private Integer count;

//    @ApiModelProperty(value="这里是这个商品的运费金额,多个商品时对应运费，填这里")
//    private BigDecimal freightAmount;
}
