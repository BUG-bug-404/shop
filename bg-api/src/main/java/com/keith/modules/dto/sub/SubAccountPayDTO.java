package com.keith.modules.dto.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("分账支付传参")
public class SubAccountPayDTO {

    @ApiModelProperty(value = "{订单编号}")
    private String orderSn;
    @ApiModelProperty(value = "{用户openid}")
    private String userOpenid;
    @ApiModelProperty(value = "{支付金额}")
    private BigDecimal payMoney;



}
