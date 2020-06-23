package com.keith.modules.dto.wxpay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("提现参数")
public class WeiXinEntPayDTO {

    @ApiModelProperty("用户openid")
    private String openId;

    @ApiModelProperty("提现金额")
    private Integer money;

}
