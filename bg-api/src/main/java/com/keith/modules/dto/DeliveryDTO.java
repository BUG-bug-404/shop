package com.keith.modules.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author Lzy
 * @date 2020/6/11 17:15
 */
@Data
public class DeliveryDTO {
    @ApiModelProperty(value = "物流编号")
    private String number;
    @ApiModelProperty(value = "物流标志")

    private String type;
    @ApiModelProperty(value = "手机号")
    private String phone;
}
