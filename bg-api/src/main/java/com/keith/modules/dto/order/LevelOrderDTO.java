package com.keith.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单传入的数据
 */

@Data
@ApiModel(value="订单传入的数据")
public class LevelOrderDTO {

    /**
     * 订单总金额
     */
    @ApiModelProperty(value="订单总金额")
    private BigDecimal totalAmount;
    /**
     * 应付金额（实际支付金额）
     */
    @ApiModelProperty(value="应付金额（实际支付金额）")
    private BigDecimal payAmount;

}
