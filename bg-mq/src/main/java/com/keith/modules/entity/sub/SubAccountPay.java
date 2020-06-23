package com.keith.modules.entity.sub;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分账订单支付表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:11
 */
@Data
@TableName("sub_account_pay")
public class SubAccountPay implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(value = "{}")
    @TableId
    private Long id;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "{订单编号}")
    private String orderSn;
    /**
     * 用户openid
     */
    @ApiModelProperty(value = "{用户openid}")
    private String userOpenid;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "{用户id}")
    private String userId;
    /**
     * 支付类型 1微信，2支付宝
     */
    @ApiModelProperty(value = "{支付类型 1微信，2支付宝}")
    private Integer payType;
    /**
     * 支付状态 1未支付，2已支付
     */
    @ApiModelProperty(value = "{支付状态 1未支付，2已支付}")
    private Integer payStatus;
    /**
     * 第三方流水编号
     */
    @ApiModelProperty(value = "{第三方流水编号}")
    private String paySn;
    /**
     * 支付金额
     */
    @ApiModelProperty(value = "{支付金额}")
    private BigDecimal payMoney;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "{创建时间}")
    private Date creatTime;
    /**
     * 支付时间
     */
    @ApiModelProperty(value = "{支付时间}")
    private Date payTime;
    /**
     * 规格id
     */
    private Integer skuId;
    /**
     * 商家id
     */
    private Integer shopId;
    /**
     * 供应上id
     */
    private Long adminId;

}
