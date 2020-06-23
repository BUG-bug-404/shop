package com.keith.modules.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 用户账户历史表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-08 14:36:29
 */
@Data
@TableName("user_acount_history")
@ApiModel(value = "用户账户历史表")
public class UserAcountHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
		@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 
	 */
		@ApiModelProperty(value = "")
	private Long userMemberId;
	/**
	 * 0:佣金收入/1:表示收入/2:支出/3:充值/4:单笔订单的收
	 */
		@ApiModelProperty(value = "0:佣金收入/1:表示收入/2:支出/3:充值/4:单笔订单的收")
	private Integer balanceType;
	/**
	 * 当前余额
	 */
		@ApiModelProperty(value = "当前余额")
	private BigDecimal currentBanlace;
	/**
	 * 历史
	 */
		@ApiModelProperty(value = "历史")
	private BigDecimal balanceHistory;
	/**
	 * 备注
	 */
		@ApiModelProperty(value = "备注")
	private String note;
	/**
	 * 进出时间
	 */
		@ApiModelProperty(value = "进出时间")
	private Date createTime;
	/**
	 * 订单ID
	 */
		@ApiModelProperty(value = "订单ID")
	private Long orderId;
	/**
	 * 金额
	 */
	@ApiModelProperty(value = "金额")
	private BigDecimal amount;
	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private  String orderSn;

}
