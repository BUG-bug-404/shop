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
 * 分销记录表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:30
 */
@Data
@TableName("store_record")
@ApiModel(value = "分销记录表")
public class StoreRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
		@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 店家Id
	 */
	@ApiModelProperty(value = "店家名称")
	private Long userId;
	/**
	 * 店家名称
	 */
		@ApiModelProperty(value = "店家名称")
	private String userName;
	/**
	 * 订单ID
	 */
		@ApiModelProperty(value = "订单ID")
	private Long orderId;
	/**
	 * 佣金额
	 */
		@ApiModelProperty(value = "佣金额")
	private BigDecimal costFee;
	/**
	 * 商品名称
	 */
		@ApiModelProperty(value = "商品名称")
	private String productName;
	/**
	 * 商品id
	 */
		@ApiModelProperty(value = "商品id")
	private Long productId;
	/**
	 * 添加时间
	 */
		@ApiModelProperty(value = "添加时间")
	private Date createTime;

}
