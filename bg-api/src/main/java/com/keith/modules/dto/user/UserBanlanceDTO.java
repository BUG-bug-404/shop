package com.keith.modules.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("充值参数")
public class UserBanlanceDTO  {

    @ApiModelProperty("充值金额")
    private Integer money;


}
