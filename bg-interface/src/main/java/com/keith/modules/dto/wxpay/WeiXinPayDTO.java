package com.keith.modules.dto.wxpay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微信支付传参")
public class WeiXinPayDTO {

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("openid")
    private String openId;
}
