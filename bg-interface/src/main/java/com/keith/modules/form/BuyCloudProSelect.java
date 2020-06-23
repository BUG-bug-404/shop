package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lzy
 * @date 2020/6/10 13:54
 */
@Data
public class BuyCloudProSelect {
    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "这个商品的规格们")
    private List<BuyCloudSelect> skus;

    @ApiModelProperty(value="地址id---云仓货物屯仓无邮费")
    private Long addressId;

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
    /**
     * 运费金额
     */
    @ApiModelProperty(value="运费金额")
    private BigDecimal freightAmount;


}
