package com.keith.modules.dto.cloud;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lzy
 * @date 2020/6/12 11:16
 */
@Data
public class CloudUserRepoInfo {
    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "规格IDs")
    private List<CloudUserSkuInfo> skus;

    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "商品标题")
    private String productName;

    @ApiModelProperty(value = "此单总件数")
    private Integer allCount;

    @ApiModelProperty(value = "此单总价格")
    private BigDecimal sumPrice;
}
