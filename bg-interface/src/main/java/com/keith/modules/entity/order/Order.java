package com.keith.modules.entity.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {

    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     * */
    @ApiModelProperty(value = "用户id")
    private Long userMemberId;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderSn;
    @ApiModelProperty(value = "提交时间")
    private Date createTime;
    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;
    /**
     * 支付方式：0->未支付；1->支付宝；2->微信
     */
    @ApiModelProperty(value = "支付方式：0->未支付；1->支付宝；2->微信")
    private Integer payType;
    /**
     * 订单状态：0->待付款；1->待发货；2->已付款,3->已发货；4->已收货，5->已完成；6->已关闭；7->已取消，
     */
    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已付款,3->已发货；4->已收货，5->已完成；6->已关闭；7->已取消，")
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
     * 收获地址id
     */
    @ApiModelProperty(value = "收获地址id")
    private Long receiveAddressId;

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
     * */
    @ApiModelProperty(value = "图片")
    private String productPic;
    /**
     * */
    @ApiModelProperty(value = "名称")
    private String productName;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;
    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    @ApiModelProperty(value = "商品销售属性，啥啊]")
    private String productAttr;
    /**
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号")
    private Long productSkuId;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String receiveName;
    /**
     * 手机号
     */

    @ApiModelProperty(value = "收货人手机号码")
    private String phone;

    @ApiModelProperty(value = "详细地址")
    private String detailTime;

   // @ApiModelProperty(value = "下单总数")
    private Integer total;
   // @ApiModelProperty(value = "已发数量")
    private Integer dai;



}



