package com.keith.modules.entity.withdraw;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 提现申请表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 13:47:48
 */
@Data
@TableName("user_member_withdraw")
public class UserMemberWithdraw implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Long userMemberId;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Integer phone;
	/**
	 * 提现方式0/支付宝1/微信
	 */
@ApiModelProperty(value = "{提现方式0/支付宝1/微信 2银行卡}")
	private Integer style;
	/**
	 * 提现账号
	 */
@ApiModelProperty(value = "{提现账号}")
	private String acceptAccount;
	/**
	 * 提现金额
	 */
@ApiModelProperty(value = "{提现金额}")
	private BigDecimal price;
	/**
	 * 提现状态0审核中/1通过/2驳回
	 */
@ApiModelProperty(value = "{提现状态0审核中/1通过/2驳回}")
	private Integer status;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private String notice;
	/**
	 * 预计时间(天)
	 */
@ApiModelProperty(value = "{预计时间(天)}")
	private Integer presetTime;
	/**
	 * 申请时间
	 */
@ApiModelProperty(value = "{申请时间}")
	private Date createTime;
	/**
	 * 提现类型0->余额提现1->暂定
	 */
@ApiModelProperty(value = "{提现类型0->余额提现1->暂定}")
	private Integer type;

}
