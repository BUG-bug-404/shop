package com.keith.modules.entity.withdraw;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商账户表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-18 11:21:07
 */
@Data
@TableName("user_admin_account")
public class UserAdminAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	/**
	 * 供应商id
	 */
@ApiModelProperty(value = "{供应商id}")
	private Long useAdminId;
	/**
	 * 总额
	 */
@ApiModelProperty(value = "{总额}")
	private BigDecimal totalMoeny;
	/**
	 * 提现额
	 */
@ApiModelProperty(value = "{提现额}")
	private BigDecimal withdrawalMoney;
	/**
	 * 余额
	 */
@ApiModelProperty(value = "{余额}")
	private BigDecimal balanceMoney;
	/**
	 * 待退回邮费余额
	 */
@ApiModelProperty(value = "{待退回邮费余额}")
	private BigDecimal returnMoney;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Date createTime;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Date update;

}
