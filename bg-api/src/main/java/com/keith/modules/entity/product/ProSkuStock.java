package com.keith.modules.entity.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品sku库存表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 09:39:17
 */
@Data
@TableName("pro_sku_stock")
public class ProSkuStock implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Long productId;
	/**
	 * sku编码
	 */
	@ApiModelProperty(value = "sku编码")
	private String skuCode;
	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	private BigDecimal price;
	/**
	 * 库存
	 */
	@ApiModelProperty(value = "库存")
	private Integer stock;
	/**
	 * 预警库存
	 */
	@ApiModelProperty(value = "预警库存")
	private Integer lowStock;
	/**
	 * 规格属性1
	 */
	@ApiModelProperty(value = "规格属性1")
	private String sp1;
	/**
	 * 规格属性2
	 */
	@ApiModelProperty(value = "规格属性2")
	private String sp2;
	/**
	 * 规格属性2
	 */
	@ApiModelProperty(value = "规格属性2")
	private String sp3;
	/**
	 * 展示图片
	 */
	@ApiModelProperty(value = "展示图片")
	private String pic;
	/**
	 * 销量
	 */
	@ApiModelProperty(value = "销量")
	private Integer sale;
	/**
	 * 单品促销价格
	 */
	@ApiModelProperty(value = "单品促销价格")
	private BigDecimal promotionPrice;
	/**
	 * 锁定库存
	 */
	@ApiModelProperty(value = "锁定库存")
	private Integer lockStock;
	/**
	 * 平台进货价格
	 */
	@ApiModelProperty(value = "平台进货价格")
	private BigDecimal platformPrice;
	/**
	 * 平台销售价格
	 */
	@ApiModelProperty(value = "平台销售价格")
	private BigDecimal platformSalePrice;
	/**
	 * 添加人
	 */
	@ApiModelProperty(value = "添加人")
	private String createPerson;
	/**
	 * 添加时间
	 */
	@ApiModelProperty(value = "添加时间")
	private Date createTime;
	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String changePerson;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date changeTime;




}
