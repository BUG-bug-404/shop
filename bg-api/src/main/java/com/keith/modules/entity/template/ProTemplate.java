package com.keith.modules.entity.template;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 小运费模版
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:36
 */
@Data
@TableName("pro_template")
public class ProTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long feightTemplateId;
	/**
	 * 首单位kg
	 */
	@ApiModelProperty("首单位kg")
	private BigDecimal firstUnit;
	/**
	 * 首费（元）
	 */
	@ApiModelProperty("首费（元）")
	private BigDecimal firstFee;
	/**
	 * 
	 */
	@ApiModelProperty("续件")
	private BigDecimal continueUnit;
	/**
	 * 
	 */
	@ApiModelProperty("续费（元）")
	private BigDecimal continueFee;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String region;

	/**
	 * 供应商id
	 */
	private Integer userAdminId;

}
