package com.keith.modules.entity.user;

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
 * 收藏表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:41:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_collect_item")
public class UserCollectItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Long id;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Long productId;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Long userMemberId;
    /**
     * 批发价格
     */
    @ApiModelProperty(value = "批发价格")
    private BigDecimal price;
    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    private BigDecimal salePrice;
    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    private String productPic;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;
    /**
     * 商品副标题（卖点）
     */
    @ApiModelProperty(value = "商品副标题（卖点）")
    private String productSubTitle;
    /**
     * 商品sku条码
     */
    @ApiModelProperty(value = "商品sku条码")
    private String productSkuCode;
    /**
     * 会员昵称
     */
    @ApiModelProperty(value = "会员昵称")
    private String memberUsername;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT )
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer deleteStatus;
    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类")
    private Long productCategoryId;
    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    private String productSn;
    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    @ApiModelProperty(value = "商品销售属性]")
    private String productAttr;

}
