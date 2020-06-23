package com.keith.modules.entity.sub;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分账记录表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:10
 */
@Data
@TableName("sub_account_money")
public class SubAccountMoney implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	/**
	 * MERCHANT_ID：商户ID  PERSONAL_WECHATID：个人微信号PERSONAL_OPENID：个人openid（由父商户APPID转换得到）PERSONAL_SUB_OPENID: 个人sub_openid（由子商户APPID转换得到）
	 */
@ApiModelProperty(value = "{MERCHANT_ID：商户ID  PERSONAL_WECHATID：个人微信号PERSONAL_OPENID：个人openid（由父商户APPID转换得到）PERSONAL_SUB_OPENID: 个人sub_openid（由子商户APPID转换得到）}")
	private String type;
	/**
	 * 类型是MERCHANT_ID时，是商户ID
   *                    类型是PERSONAL_WECHATID时，是个人微信号
   *                    类型是PERSONAL_OPENID时，是个人openid
   *                    类型是PERSONAL_SUB_OPENID时，是个人sub_openid
	 */
@ApiModelProperty(value = "{类型是MERCHANT_ID时，是商户ID\n" +
		"   *                    类型是PERSONAL_WECHATID时，是个人微信号\n" +
		"   *                    类型是PERSONAL_OPENID时，是个人openid\n" +
		"   *                    类型是PERSONAL_SUB_OPENID时，是个人sub_openid}")
	private String account;
	/**
	 *  分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额
	 */
@ApiModelProperty(value = "{ 分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额}")
	private Integer amount;
	/**
	 *  分账的原因描述，分账账单中需要体现
	 */
@ApiModelProperty(value = "{ 分账的原因描述，分账账单中需要体现}")
	private String description;
	/**
	 * 创建时间
	 */
@ApiModelProperty(value = "{创建时间}")
	private Date createTime;

}
