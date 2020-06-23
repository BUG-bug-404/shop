package com.keith.modules.dto.cloud;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lzy
 * @date 2020/6/12 10:46
 */
@Data
public class CloudRepoCount {
    @ApiModelProperty(value = "商品id")
    private Long productId;
    @ApiModelProperty(value = "商品规格id")
    private Long skuId;

}
