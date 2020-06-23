package com.keith.modules.entity.bank;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 17:09:51
 */
@Data
@TableName("bank_cat")
public class BankCat implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	/**
	 * 银行卡名
	 */
@ApiModelProperty(value = "{银行卡名}")
	private String bankName;
	/**
	 * 编号
	 */
@ApiModelProperty(value = "{编号}")
	private String bankSn;

}
