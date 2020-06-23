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
 * 订单表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
@Data
@TableName("order_order")
public class OrderOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
	@TableId
	@ApiModelProperty(value = "订单id")
	private Long id;
	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Long productId;
	/**
	 * */
	@ApiModelProperty(value = "")
	private Long userAdminId;
	/**
	 * */
	@ApiModelProperty(value = "")
	private Long userMemberId;
	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

	/**
	 * 提交时间
	 */
	//@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "提交时间")
	private Date createTime;
	/**
	 * 用户帐号名
	 */
	@ApiModelProperty(value = "用户帐号名")
	private String memberUsername;
	/**
	 * 订单总金额
	 */
	@ApiModelProperty(value = "订单总金额")
	private BigDecimal totalAmount;
	/**
	 * 应付金额（实际支付金额）
	 */
	@ApiModelProperty(value = "应付金额（实际支付金额）")
	private BigDecimal payAmount;
	/**
	 * 运费金额
	 */
	@ApiModelProperty(value = "运费金额")
	private BigDecimal freightAmount;
	/**
	 * 支付方式：0->未支付；1->支付宝；2->微信
	 */
	@ApiModelProperty(value = "支付方式：0->未支付；1->支付宝；2->微信")
	private Integer payType;
	/**
	 * 订单状态：0->待付款；1->待发货；2->已付款,3->已发货；4->已收货，5->已完成；6->已关闭；7->已取消，
	 */
	@ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已付款,3->已发货；4->已收货，5->已完成；6->已关闭；7->已取消，9,退款")
	private Integer status;
	/**
	 * 订单类型：0->正常订单；1->预售订单2->云仓订单
	 */
	@ApiModelProperty(value = "订单类型：0->正常订单；1->预售订单2->云仓订单")
	private Integer orderType;
	/**
	 * 物流单号
	 */
	@ApiModelProperty(value = "物流单号")
	private String deliverySn;
	/**
	 * 自动确认时间（天）
	 */
	@ApiModelProperty(value = "自动确认时间（天）")
	private Integer autoConfirmDay;
	/**
	 * 活动信息
	 */
	@ApiModelProperty(value = "活动信息")
	private String promotionInfo;
	/**
	 * 发票类型：0->不开发票；1->电子发票；2->纸质发票
	 */
	@ApiModelProperty(value = "发票类型：0->不开发票；1->电子发票；2->纸质发票")
	private Integer billType;
	/**
	 * 预售订单状态
	 */
	@ApiModelProperty(value = "预售订单状态")
	private Integer preStatus;
	/**
	 * 收获地址id
	 */
	@ApiModelProperty(value = "收获地址id")
	private Long receiveAddressId;
	/**
	 * 发票id
	 */
	@ApiModelProperty(value = "发票id")
	private Long billId;
	/**
	 * 订单备注/买家留言
	 */
	@ApiModelProperty(value = "订单备注/买家留言")
	private String note;
	/**
	 * 确认收货状态：0->未确认；1->已确认
	 */
	@ApiModelProperty(value = "确认收货状态：0->未确认；1->已确认")
	private Integer confirmStatus;
	/**
	 * 删除状态：0->未删除；1->已删除
	 */
	@ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
	private Integer deleteStatus;
	/**
	 * 支付时间
	 */
	@ApiModelProperty(value = "支付时间")
	private Date paymentTime;
	/**
	 * 发货时间
	 */
	@ApiModelProperty(value = "发货时间")
	private Date deliveryTime;
	/**
	 * 确认收货时间
	 */
	@ApiModelProperty(value = "确认收货时间")
	private Date receiveTime;
	/**
	 * 评价时间
	 */
	@ApiModelProperty(value = "评价时间")
	private Date commentTime;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date modifyTime;

	/**
	 * 每个订单的纯收益
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "每个订单的纯收益((用于计算分佣))")
	private BigDecimal profits;
	/**
	 * 是否已结算佣金：0：否/1：是
	 */
	@ApiModelProperty(value = "是否已结算佣金：0：否/1：是")
	private  Integer closeAccount;

	@ApiModelProperty(value = "第三方订单号")
	private  String thirdOrderSn;

	@ApiModelProperty(value = "收货人")
	private  String receivePerson;

	@ApiModelProperty(value = "收货人地址")
	private  String receiveAdress;

	@ApiModelProperty(value = "收货人手机号")
	private  String receivePhone;

	@ApiModelProperty(value = "已付预定金额")
	private BigDecimal repayAmount;

	@ApiModelProperty(value = "订单统一单号")
	private String unifyOrderSn;

}
