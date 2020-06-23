package com.keith.modules.entity.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录日志
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-01 16:00:09
 */
@Data
@TableName("user_login_log")
public class UserLoginLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 登录时间
	 */
	@TableField(value = "login_time",fill = FieldFill.INSERT_UPDATE)
	private Date loginTime;

}
