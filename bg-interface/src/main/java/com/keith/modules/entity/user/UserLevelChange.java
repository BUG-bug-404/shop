package com.keith.modules.entity.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店家等级变化表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-04 09:53:52
 */
@Data
@TableName("user_level_change")
@ApiModel(value = "店家等级变化表")
public class UserLevelChange implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
		@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 
	 */
		@ApiModelProperty(value = "")
	private Long userMemberId;
	/**
	 * 当前等级
	 */
		@ApiModelProperty(value = "当前等级")
	private Long currentLevelId;
	/**
	 * 等级福利状态
	 */
		@ApiModelProperty(value = "等级福利状态")
	private Integer welfareStatus;
	/**
	 * 
	 */
		@ApiModelProperty(value = "")
	private Long levelId;
	/**
	 * 备注
	 */
		@ApiModelProperty(value = "备注")
	private String note;
	/**
	 * 变化时间
	 */
		@TableField(value = "create_time",fill = FieldFill.INSERT)
		@ApiModelProperty(value = "变化时间")
	private Date createTime;

}
