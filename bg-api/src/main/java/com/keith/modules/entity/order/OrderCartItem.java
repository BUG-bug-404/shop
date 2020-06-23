package com.keith.modules.entity.order;

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
 * 进货车表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:26:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("order_cart_item")
public class OrderCartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "进货车id")
    private Long id;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     * 规格，统一规格这里为空
     */
    @ApiModelProperty(value = "规格，统一规格这里为空")
    private Long productSkuId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userMemberId;
    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer quantity;
    /**
     * 销售价格
     */
    @ApiModelProperty(value = "批发价格")
    private BigDecimal price;
    /**
     * 批发价格
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
     * 修改时间s
     */
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "该商品规格是否失效0->未失效1->已失效")
    private Integer validStatus;
    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品分类id")
    private Long productCategoryId;
    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    private String productSn;
    /**
     * 商品销售属性:[{"key":"颜色","value":"红色"},{"key":"容量","value":"4G"}]
     */
    @ApiModelProperty(value = "商品销售属性]")
    private String productAttr;

    @ApiModelProperty(value = "预售待支付尾款的订单0，是1，不是")
    private Integer preWaitpay;

    @ApiModelProperty(value = "1:预售 2:云仓 3:现货")
    private Integer type;

    @ApiModelProperty(value = "单号-预售待支付的")
    private String orderSn;

    @ApiModelProperty(value = "规格值")
    @TableField(exist = false)
    private String sp2;



}
