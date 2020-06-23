package com.keith.modules.entity.template;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 大运费模版表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:36
 */
@Data
@TableName("pro_feight_template")
public class ProFeightTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long userAdminId;
	/**
	 * 
	 */
	private Long productId;
	/**
	 * 计费类型:0->按重量；1->按件数;2->按体积
	 */
	private Integer chargeType;
	/**
	 * 运费模板名称
	 */
	private String name;
	/**
	 * 是否为新商品默认模板
	 */
	private Integer defaultStatus;
	/**
	 * 0买家包邮1卖家包邮
	 */
	private Integer temTypt;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
