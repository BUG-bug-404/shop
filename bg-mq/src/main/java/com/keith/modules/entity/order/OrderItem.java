package com.keith.modules.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单中所包含的商品
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
@Data
@TableName("order_item")
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private Long orderId;
	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;
	/**
	 * */
	@ApiModelProperty(value = "")
	private Long productId;
	/**
	 * */
	@ApiModelProperty(value = "")
	private Long userAdminId;
	/**
	 * */
	@ApiModelProperty(value = "")
	private String productPic;
	/**
	 * */
	@ApiModelProperty(value = "")
	private String productName;
	/**
	 * 货号
	 */
	@ApiModelProperty(value = "货号")
	private String productSn;
	/**
	 * 运费金额
	 */
	private BigDecimal freightAmount;
	/**
	 * 物流单号
	 */
	@ApiModelProperty(value = "物流单号")
	private String deliverySn;
	/**
	 * 销售价格
	 */
	@ApiModelProperty(value = "销售价格")
	private BigDecimal productPrice;
	/**
	 * 购买数量
	 */
	@ApiModelProperty(value = "购买数量")
	private Integer productQuantity;
	/**
	 * 商品sku编号
	 */
	@ApiModelProperty(value = "商品sku编号")
	private Long productSkuId;
	/**
	 * 商品sku条码
	 */
	@ApiModelProperty(value = "商品sku条码")
	private String productSkuCode;
	/**
	 * 商品分类id
	 */
	@ApiModelProperty(value = "商品分类id")
	private Long productCategoryId;
	/**
	 * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
	 */
	@ApiModelProperty(value = "商品销售属性，啥啊]")
	private String productAttr;
	/**
	 * 
	 */
	//@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 商品进货价
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "商品进货价")
	private BigDecimal platformPrice;
	/**
	 * 单个商品利润
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "单个商品利润(用于计算分佣)")
	private BigDecimal profits;

}
