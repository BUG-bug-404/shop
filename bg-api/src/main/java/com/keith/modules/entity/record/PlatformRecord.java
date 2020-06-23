package com.keith.modules.entity.record;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 平台收入记录表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:31
 */
@Data
@TableName("platform_record")
@ApiModel(value = "平台收入记录表")
public class PlatformRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
		@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 订单id
	 */
		@ApiModelProperty(value = "订单id")
	private Long orderId;
	/**
	 * 用户id
	 */
		@ApiModelProperty(value = "用户id")
	private Long userId;
	/**
	 * 名称
	 */
		@ApiModelProperty(value = "名称")
	private String userName;
	/**
	 * 类型名称 0：平台收入，1：运费
	 */
	@ApiModelProperty(value = "类型名称 0：平台收入，1：运费")
	private Integer name;
	/**
	 * 佣金额
	 */
		@ApiModelProperty(value = "佣金额")
	private BigDecimal costFee;
	/**
	 * 用户类型0：店家，1：供应商
	 */
		@ApiModelProperty(value = "用户类型0：店家，1：供应商")
	private Integer userType;
	/**
	 * 记录类型0：支出，1：收入
	 */
		@ApiModelProperty(value = "记录类型0：支出，1：收入")
	private Integer recordType;
	/**
	 * 添加时间
	 */
		@ApiModelProperty(value = "添加时间")
	private Date createTime;

}
