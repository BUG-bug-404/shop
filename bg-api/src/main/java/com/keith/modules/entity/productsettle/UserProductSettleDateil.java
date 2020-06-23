package com.keith.modules.entity.productsettle;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品结算详情表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 18:03:34
 */
@Data
@TableName("user_product_settle_dateil")
public class UserProductSettleDateil implements Serializable {
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
	private Long userAdminId;
	/**
	 * 结算id
	 */
@ApiModelProperty(value = "{结算id}")
	private Long settleId;
	/**
	 * 规格id
	 */
@ApiModelProperty(value = "{规格id}")
	private Long skuId;
	/**
	 * 售出数量
	 */
@ApiModelProperty(value = "{售出数量}")
	private Integer num;
	/**
	 * 单价
	 */
@ApiModelProperty(value = "{单价}")
	private BigDecimal price;
	/**
	 * 总价
	 */
@ApiModelProperty(value = "{总价}")
	private BigDecimal totalPrice;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Date createTime;

}
