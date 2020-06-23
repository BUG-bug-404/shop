package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CloudResult {
    @ApiModelProperty(value = "商品id")
    private Long productId;
    @ApiModelProperty(value = "商品标题")
    private String productName;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "倒计时")
    private String endTime;
}
