package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lzy
 * @date 2020/6/23 11:58
 */
@Data
public class AddCartSelect {

    @ApiModelProperty(value = "商品id")
    private Long productId;
    @ApiModelProperty(value = "商品规格id-原来的商品规格id")
    private Long skuId;
    @ApiModelProperty(value = "商品数量--")
    private Integer count;
    @ApiModelProperty(value = "商品类型 1:预售 2:云仓 3:现货")
    private Integer type;

    @ApiModelProperty(value = "进货车id")
    private Long id;

    @ApiModelProperty(value = "要修改成的商品的规格id")
    private Long reSkuId;



//    @RequestParam(value = "id") @ApiParam(value = "进货车id",required = true)Long id,
//    @RequestParam(value = "productId")@ApiParam(value = "商品的id",required = true)Long productId,
//    @RequestParam(value = "skuId")@ApiParam(value = "原来的商品的规格id",required = true)Long skuId,
//    @RequestParam(value = "reSkuId")@ApiParam(value = "要修改成的商品的规格id",required = true)Long reSkuId,
//    @RequestParam(value = "count")@ApiParam(value = "数量",required = true)Integer count,

}
