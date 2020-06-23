package com.keith.modules.entity.yf;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-17 14:39:03
 */
@Data
@TableName("user_admin_yf")
public class UserAdminYf implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	@TableId
	private Long id;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Long adminId;
	/**
	 * 运费金额
	 */
@ApiModelProperty(value = "{运费金额}")
	private BigDecimal moeny;
	/**
	 * 
	 */
@ApiModelProperty(value = "{}")
	private Date createTime;

}
