package com.keith.modules.entity.sms;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信发送表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-05-21 10:59:35
 */
@Data
@TableName("sys_send_sms")
public class SysSendSms implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value="id",type= IdType.AUTO)
	private long id;
	/**
	 * 短信类型（1/登录，2/注册,3/密码修改）
	 */
	@ApiModelProperty(value="短信类型（1/登录，2/注册）",name="type",required=true)
	private Integer type;
	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	private String mobile;
	/**
	 * 发送状态（1/发送成功，2/发送失败）
	 */
	private Integer sendStatus;
	/**
	 * 创建时间
	 */
	private Date sendTime;
	/**
	 * 短信发送回执
	 */
	private String sendData;
	/**
	 * 发送内容
	 */
	private String sendContent;

}
