package com.keith.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单传入的数据
 */

@Data
@ApiModel(value="订单传入的数据")
public class OrderDTO {

    @ApiModelProperty(value="规格id|预售购买：单规格|单商品")
    private  Long skuId;

    @ApiModelProperty(value="商品id")
    private Long productId;

    @ApiModelProperty(value="数量")
    private Integer num;


    private List<ProductDTO> productDTOS;

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

}
