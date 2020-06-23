package com.keith.modules.entity.bank;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行卡表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-18 11:25:22
 */
@Data
@TableName("bank_admin")
@ApiModel
public class BankAdmin implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	/**
	 * 收款方银行卡号
	 */
@ApiModelProperty(value = "{收款方银行卡号}")
	private String bankNo;
	/**
	 * 收款方用户名
	 */
@ApiModelProperty(value = "{收款方用户名}")
	private String trueName;
	/**
	 * 收款方开户行
	 */
@ApiModelProperty(value = "{收款方开户行}")
	private String bankCode;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Date createDate;
	/**
	 * 用户id
	 */
@ApiModelProperty(value = "{用户id}")
	private Long userId;
	/**
	 * 银行卡名称
	 */
@ApiModelProperty(value = "{银行卡名称}")
	private String bankName;

}
