package com.keith.modules.entity.template;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:50
 */
@Data
@TableName("pro_feight_product")
public class ProFeightProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 商品id
	 */
	private Long productId;
	/**
	 * 模板id
	 */
	private Long feightId;

}
