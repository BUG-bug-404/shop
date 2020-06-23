package com.keith.modules.entity.product;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 福利商品
 *
 * @author lzy
 * @email ********
 * @date 2020-06-13 14:55:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("con_welfare")
public class ConWelfare implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 福利商品表的id
     */
    @TableId
    @ApiModelProperty(value = "福利商品表的id")
    private Long id;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    private String productPic;
    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Integer proSkuStockId;
    /**
     * 规格值
     */
    @ApiModelProperty(value = "规格值")
    private String skuValue;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;
    /**
     * 上架状态：0->下架；1->上架
     */
    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Integer publishStatus;
    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    private Date publishTime;
    /**
     * 福利价格
     */
    @ApiModelProperty(value = "福利价格")
    private BigDecimal welfarePrice;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer limitCount;

    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品分类id")
    private Long productCategoryId;

    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品货号")
    private String productCode;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格详情")
    private ProSkuStock proSkuStock;


}
