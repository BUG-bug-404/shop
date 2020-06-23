package com.keith.modules.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店家等级表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-04 09:53:52
 */
@Data
@TableName("user_level")
@ApiModel(value = "店家等级表")
public class UserLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
		@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 等级名称
	 */
		@ApiModelProperty(value = "等级名称")
	private String levelName;
	/**
	 * 铺货折扣
	 */
		@ApiModelProperty(value = "铺货折扣")
	private BigDecimal levelDiscount;
	/**
	 * 钱的奖励
	 */
		@ApiModelProperty(value = "钱的奖励")
	private BigDecimal levelAwardMoney;
	/**
	 * 社群直邀佣金0.08比
	 */
		@ApiModelProperty(value = "社群直邀佣金0.08比")
	private BigDecimal subRate;
	/**
	 * 社群间邀佣金0.08比
	 */
		@ApiModelProperty(value = "社群间邀佣金0.08比")
	private BigDecimal resubRate;
	/**
	 * 同级邀佣金0.08比
	 */
		@ApiModelProperty(value = "同级邀佣金0.08比")
	private BigDecimal rate;
	/**
	 * 默认返佣比，AB两等级专用
	 */
		@ApiModelProperty(value = "默认返佣比，AB两等级专用")
	private BigDecimal defaultRate;
	/**
	 * 自购返利
	 */
		@ApiModelProperty(value = "自购返利")
	private BigDecimal selfBuyWelfare;
	/**
	 * 等级权限说明
	 */
		@ApiModelProperty(value = "等级权限说明")
	private String levelAuthority;

}
