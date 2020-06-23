package com.keith.modules.entity.withdraw;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 提现设置
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 15:44:42
 */
@Data
@TableName("set_withdraw")
public class SetWithdraw implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	/**
	 * 提现类型 1微信，2银行卡
	 */
@ApiModelProperty(value = "{提现类型 1微信，2银行卡}")
	private String type;
	/**
	 * 是否开启最低金额1开启，2关闭
	 */
@ApiModelProperty(value = "{是否开启最低金额1开启，2关闭}")
	private Integer moneyType;
	/**
	 * 最低金额
	 */
@ApiModelProperty(value = "{最低金额}")
	private BigDecimal minMoney;
	/**
	 * 是否开启提现手续，1开启，2关闭
	 */
@ApiModelProperty(value = "{是否开启提现手续，1开启，2关闭}")
	private Integer withType;
	/**
	 * 提现手续费
	 */
@ApiModelProperty(value = "{提现手续费}")
	private BigDecimal withMoney;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Date createTime;

	/**
	 * 提现次数
	 */
	private Integer num;

}
