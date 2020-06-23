package com.keith.modules.entity.ExtProduct;

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
import java.time.LocalDateTime;

/**
 * 铺货商品跟的规格表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-05 16:49:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("ext_product_sku")
public class ExtProductSku implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店家铺货过来的生成的规格id
     */
    @TableId
    @ApiModelProperty(value = "店家铺货过来的生成的规格id")
    private Long id;
    /**
     * 商品规格id
     */
    @ApiModelProperty(value = "商品规格id")
    private Long skuStockId;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     * 删除状态：0->未删除；1->已删除【供应商或者平台将商品下架啊删除状态为1】铺货要下架
     */
    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除【供应商或者平台将商品下架啊删除状态为1】铺货要下架")
    private Integer deleteStatus = 0;
    /**
     * 该规格铺货填入价格
     */
    @ApiModelProperty(value = "该规格铺货填入价格")
    private BigDecimal supplierPrice;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT )
    private LocalDateTime createTime;

    @ApiModelProperty(value = "铺货表那个id")
    private Long extProductId;

    @ApiModelProperty(value = "从云仓过来的，锁定的那个库存")
    private Long stock;

    @ApiModelProperty(value = "关联查询表的特定字段，查询时使用")
    @TableField(exist = false,value = "skuInfo")
    private String skuInfo;

}
