package com.keith.modules.dto.user;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;


/**
 * 用户表
 * 
 * @author lius
 * @date 2020-05-13 18:15:37
 */
@Data
public class UpdateUserDTO   {

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobile;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "旧密码")
	private String password;

	@ApiModelProperty(value = "新密码")
	private String newPassword;
	/**
	 * 支付密码
	 */
	@ApiModelProperty(value = "旧支付密码")
	private String paymentCode;

	/**
	 * 支付密码
	 */
	@ApiModelProperty(value = "新支付密码")
	private String newPaymentCode;
	/**
	 * 短信类型（1/登录，2/注册,3/密码修改）
	 */
	@ApiModelProperty(value = "短信类型（1/登录，2/注册,3/密码修改）")
	private Integer smsType;

}
