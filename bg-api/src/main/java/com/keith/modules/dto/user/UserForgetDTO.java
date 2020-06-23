package com.keith.modules.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户表
 * 
 * @author lius
 * @date 2020-05-13 18:15:37
 */
@Data
public class UserForgetDTO {



	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobile;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String newPassword;


	private String openId;

	private String unionId;
	/**
     * 短信类型（1/登录，2/注册,3/密码修改）
	 */
	@ApiModelProperty(value = "短信类型（1/登录，2/注册,3/密码修改）")
	private Integer smsType;

	@ApiModelProperty(value = "短信验证码")
	private String smsCode;

}
