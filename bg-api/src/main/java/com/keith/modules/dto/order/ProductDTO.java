package com.keith.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品信息")
public class ProductDTO {

    @ApiModelProperty(value="规格id")
    private  Long skuId;

    @ApiModelProperty(value="商品id")
    private Long productId;

    @ApiModelProperty(value="数量")
    private Integer num;
}
