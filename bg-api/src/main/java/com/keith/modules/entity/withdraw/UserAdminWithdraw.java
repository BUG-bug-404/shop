package com.keith.modules.entity.withdraw;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商提现记录表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-18 11:21:07
 */
@Data
@TableName("user_admin_withdraw")
public class UserAdminWithdraw implements Serializable {
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
	private Long userAdminId;
	/**
	 * 提现方式0->支付宝2->微信
	 */
@ApiModelProperty(value = "{提现方式0->支付宝2->微信}")
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
	 * 申请时间
	 */
@ApiModelProperty(value = "{申请时间}")
	private Date createTime;
	/**
	 * 打款流水
	 */
@ApiModelProperty(value = "{打款流水}")
	private String water;
	/**
	 * 打款id
	 */
@ApiModelProperty(value = "{打款id}")
	private Long waterId;
	/**
	 * 0审核，1通过，2驳回
	 */
@ApiModelProperty(value = "{0审核，1通过，2驳回}")
	private Integer status;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private String adminName;

}
