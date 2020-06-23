package com.keith.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("完成订单")
public class EndOrderDTO {

    @ApiModelProperty(value = "订单编号")
    private String orderSn;
}
