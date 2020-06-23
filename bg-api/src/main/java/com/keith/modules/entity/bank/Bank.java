package com.keith.modules.entity.bank;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行卡表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 10:56:12
 */
@Data
@TableName("bank")
public class Bank implements Serializable {
	private static final long serialVersionUID = 1L;



	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	private  Long userId;

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
@ApiModelProperty(value = "{银行卡编号}")
	private String bankCode;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Date createDate;

	/**
	 * 银行卡名称
	 */
	private String bankName;

}
