package com.keith.modules.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 * 
 * @author JohnSon
 * @email 380847163@qq.com
 * @date 2020-05-13 18:15:37
 */
@Data
@ApiModel
@TableName("user_member")
public class UserMember implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;
	/**
	 * */
	@ApiModelProperty(value = "")
	private Long parentId;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobile;
	/**
	 * 支付密码
	 */
	@ApiModelProperty(value = "支付密码")
	private String paymentCode;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;
	/**
	 * 性别：0->未知；1->男；2->女
	 */
	@ApiModelProperty(value = "性别：0->未知；1->男；2->女")
	private String gender;
	/**
	 * 生日
	 */
	@ApiModelProperty(value = "生日")
	private Date birthday;
	/**
	 * 所在城市[县级]
	 */
	@ApiModelProperty(value = "所在城市[县级]")
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
	 * 头像图片
	 */
	@ApiModelProperty(value = "头像图片")
	private String pic;
	/**
	 * 账号类型0->用户1->店家
	 */
	@ApiModelProperty(value = "账号类型0->用户1->店家")
	private Integer type;
	/**
	 * 是否为新人状态：0->是1->不是
	 */
	@ApiModelProperty(value = "是否为新人状态：0->是1->不是")
	private Integer registerBenrfits;
	/**
	 * 店家等级
	 */
	@ApiModelProperty(value = "店家等级：1:普通店家,2:铜,3:银,4:金")
	private Long levelId;
	/**
	 * 注册时间
	 */
	@ApiModelProperty(value = "注册时间")
	private Date createDate;
	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String identifierName;
	/**
	 * 行业id
	 */
	@ApiModelProperty(value = "行业id")
	private Long professionId;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String note;
	/**
	 * openid
	 */
	@ApiModelProperty(value = "openid")
	private String openId;
	/**
	 * unionid
	 */
	@ApiModelProperty(value = "unionid")
	private String unionId;

	/**
	 * 直邀A级店长人数
	 */
	@ApiModelProperty(value = "直邀A级店长人数")
	private Integer inviteCount;
	/**
	 * 最近佣金结算时间
	 */
	@ApiModelProperty(value = "最近佣金结算时间")
	private Date commissionTime;
	/**
	 * 下级用户
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "下级用户")
	private List<UserMember> child;

	@TableField(exist = false)
	private String token;

}
