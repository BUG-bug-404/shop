package com.keith.modules.dto.order;

import com.sun.istack.NotNull;
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
public class OrderDTO {

    @ApiModelProperty(value="规格id")
    private  Long skuId;

    @ApiModelProperty(value="商品id")
    private Long productId;

    @ApiModelProperty(value="数量")
    private Integer num;
    @ApiModelProperty(value="地址id")
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

    @ApiModelProperty("是否添加到云仓：0->不是；1->是")
    private Integer isCloud;

    /**
     * 用户appId
     */
    @NotNull
    private String appId;

    @NotNull
    private String thirdOrderSn;

    @NotNull
    private String receiveAddress;

    @NotNull
    private String receivePhone;

    List<OrderDTO> orders;

    private Integer type;
}
