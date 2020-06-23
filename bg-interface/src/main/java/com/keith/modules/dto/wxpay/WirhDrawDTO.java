package com.keith.modules.dto.wxpay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("提现参数")
public class WirhDrawDTO {

    @ApiModelProperty("提现金额")
    private BigDecimal money;

    @ApiModelProperty(value = "{提现类型 1微信，2银行卡}")
    private Integer type;

    @ApiModelProperty("提现类型 1微信 填写用户openid")
    private String openId;
}
