package com.keith.modules.form;

import com.keith.modules.entity.ExtProduct.ExtProductSku;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lzy
 * @date 2020/6/6 17:32
 */
@Data
public class ExtProResult {
    /**这是用来输出待编辑的*/
    @ApiModelProperty(value = "图片们")
    private List<String> pic;

    @ApiModelProperty(value = "商品标题")
    private String productName;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "规格们")
    private List<ExtSkusSelect> skus;

    @ApiModelProperty(value = "图文详情")
    private String detail ;

    @ApiModelProperty(value = "是否上架")
    private Integer publisstatus ;

    @ApiModelProperty(value = "成功添加的id列表")
    private List<Long> failProIds;

    @ApiModelProperty(value = "添加失败的id列表")
    private List<Long> successProIds;

    @ApiModelProperty(value = "铺货查询出来的规格们")
    private List<ExtProductSku> exSkus;

    /**
     * 是否云仓
     */
    private int cloudStatus;

    private String appId;
}
