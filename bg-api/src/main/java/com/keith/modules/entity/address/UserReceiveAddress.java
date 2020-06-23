package com.keith.modules.entity.address;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 收货地址表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-02 20:08:46
 */
@Data
@TableName("user_receive_address")
public class UserReceiveAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	@ApiModelProperty(value = "用户Id")
	private Long userMemberId;
	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String receiveName;
	/**
	 * 手机号
	 */

	@ApiModelProperty(value = "收货人手机号码")
	private String phone;
	/**
	 * 是否为默认：0为不默认/1为默认
	 */

	@ApiModelProperty(value = "是否为默认：0为不默认/1为默认")
	private Integer defaultStatus;
	/**
	 * 省份
	 */
	@ApiModelProperty(value = "省份")
	private String province;
	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String city;
	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String region;
	/**
	 * 详细地址
	 */

	@ApiModelProperty(value = "详细地址")
	private String detailTime;
	/**
	 * 注册时间
	 */

	@TableField(value = "create_time",fill = FieldFill.INSERT)
	@ApiModelProperty(value = "注册时间")
	private Date createTime;

}
