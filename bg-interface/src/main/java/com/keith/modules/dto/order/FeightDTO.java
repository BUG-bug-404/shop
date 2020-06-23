package com.keith.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("运费")
public class FeightDTO {

    @ApiModelProperty("地址id")
    private Integer addressId;

    @ApiModelProperty("数量")
    private Integer num;

    /**
     * 供应商id
     */
    @ApiModelProperty("商品id")
    private Integer productId;

    /**
     * 用户appId
     */
    private String appId;
}
