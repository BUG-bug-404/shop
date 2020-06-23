package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountOrderPrice {
    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "规格id")
    private Long skuId;

    @ApiModelProperty(value = "规格的量")
    private Integer count;

    @ApiModelProperty(value = "{收获地址id}")
    private Integer addressId;

    @ApiModelProperty(value = "{收获地址:示例：浙江省-杭州市-萧山区-详细地址}")
    private String addressInfo;

    /*预售-代下单-尾款-云仓-福利商品*/
    @ApiModelProperty(value = "{是0-云仓拿货走人1-福利商品2云仓货物屯仓-不传-现货}")
    private Integer status;

}
