package com.keith.modules.entity.user;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户余额表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-02 15:36:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_banlance")
public class UserBanlance implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long userMemberId;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 余额
	 */
	private BigDecimal banlance;

}
