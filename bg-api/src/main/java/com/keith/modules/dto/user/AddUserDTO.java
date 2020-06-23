package com.keith.modules.dto.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * 
 * @author JohnSon
 * @email 380847163@qq.com
 * @date 2020-05-13 18:15:37
 */
@Data
public class AddUserDTO  {

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
	 * 性别：0->未知；1->男；2->女
	 */
	@ApiModelProperty(value = "性别：0->未知；1->男；2->女")
	private String gender;
	/**
	 * 所在城市
	 */
	@ApiModelProperty(value = "所在城市")
	private String city;
	/**
	 * 名字
	 */
	@ApiModelProperty(value = "名字")
	private String fullname;
	/**
	 * 账号状态0->异常1->正常
	 */
	@ApiModelProperty(value = "账号状态0->异常1->正常")
	private Integer idStatus;
	/**
	 * 邀请码
	 */
	@ApiModelProperty(value = "邀请码")
	private String inviteCode;
	/**
	 * 图片
	 */
	@ApiModelProperty(value = "图片")
	private String pic;
	/**
	 * 是否为新人状态
	 */
	@ApiModelProperty(value = "是否为新人状态")
	private Integer registerBenrfits;
	/**
	 * 注册时间
	 */
	@TableField(value = "create_date",fill = FieldFill.INSERT)
	private Date createDate;

	@ApiModelProperty(value = "解法向量")
	private String iv;

	@ApiModelProperty(value = "用户信息的加密数据")
	private String encryptedData;


}
