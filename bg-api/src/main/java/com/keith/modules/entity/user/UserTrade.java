package com.keith.modules.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 充值记录 订单表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-09 14:40:04
 */
@Data
@TableName("user_trade")
public class UserTrade implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(value = "{}")
    @TableId
    private Long id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "{用户id}")
    private Long userId;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "{手机号}")
    private String userMobile;
    /**
     *
     */
    @ApiModelProperty(value = "{}")
    private BigDecimal tradeAmount;
    /**
     * 交易描述
     */
    @ApiModelProperty(value = "{交易描述}")
    private String tradeDesc;
    /**
     * 交易状态
     */
    @ApiModelProperty(value = "{交易状态}")
    private Integer tradeStatus;
    /**
     * 流水号
     */
    @ApiModelProperty(value = "{流水号}")
    private String flowTradeNo;
    /**
     * 第三放账号
     */
    @ApiModelProperty(value = "{第三放账号}")
    private String flowUserAcc;
    /**
     * 支付类型 1微信
     */
    @ApiModelProperty(value = "{支付类型 1微信}")
    private Integer payType;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "{创建时间}")
    private Date createDate;

}
