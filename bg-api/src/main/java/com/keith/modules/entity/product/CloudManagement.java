package com.keith.modules.entity.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 云仓SKU数据表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-04 19:25:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("cloud_management")
public class CloudManagement implements Serializable {
	private static final long serialVersionUID = 1L;


	@TableId
	@ApiModelProperty(value = "id")
	private Long id;
	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Long productId;
	/**
	 * 商品sku库存表id
	 */
	@ApiModelProperty(value = "商品sku库存表id")
	private Long proSkuStockId;
	/**
	 * 云仓商品ID
	 */
	@ApiModelProperty(value = "云仓商品ID--->不用")
	private Long cloudStorageId;
	/**
	 * 活动价
	 */
	@ApiModelProperty(value = "活动价")
	private BigDecimal acitivityPrice;
	/**
	 * 库存
	 */
	@ApiModelProperty(value = "库存")
	private Integer stock;
	/**
	 * 活动数量
	 */
	@ApiModelProperty(value = "活动数量")
	private Integer activityStock;
	/**
	 * 平台价格
	 */
	@ApiModelProperty(value = "平台价格")
	private BigDecimal platformPrice;
	/**
	 * 回购价格
	 */
	@ApiModelProperty(value = "回购价格----->不用")
	private BigDecimal repoPrice;
	/**
	 * 回购截至时间
	 */
	@ApiModelProperty(value = "回购截至时间-->不用")
	private Date repoTime;
	/**
	 * 上架状态：0->下架；1->上架
	 */
	@ApiModelProperty(value = "上架状态：0->下架；1->上架")
	private Integer publishStatus;
	/**
	 * 上架时间
	 */
	@ApiModelProperty(value = "上架时间")
	private Date publishTime;
	/**
	 * 回购区间1开始时间
	 */
	@ApiModelProperty(value = "回购区间1开始时间")
	private Date range1StartTime;
	/**
	 * 回购区间2开始时间
	 */
	@ApiModelProperty(value = "回购区间2开始时间")
	private Date range2StartTime;
	/**
	 * 回购区间1结束时间
	 */
	@ApiModelProperty(value = "回购区间1结束时间")
	private Date range1EndTime;
	/**
	 * 回购区间2结束时间
	 */
	@ApiModelProperty(value = "回购区间2结束时间")
	private Date range2EndTime;
	/**
	 * 回购区间1的回购折扣
	 */
	@ApiModelProperty(value = "回购区间1的回购折扣")
	private BigDecimal range1Discount;
	/**
	 * 回购区间2的回购折扣
	 */
	@ApiModelProperty(value = "回购区间2的回购折扣")
	private BigDecimal range2Discount;


	@ApiModelProperty(value = "商品名字")
	@TableField(exist = false)
	private String productName;

	@ApiModelProperty(value = "图片")
	@TableField(exist = false)
	private String pic;

	@ApiModelProperty(value = "规格")
	@TableField(exist = false)
	private String skuValue;

	@ApiModelProperty(value = "规格库存")
	@TableField(exist = false)
	private Integer skuStock;

	@ApiModelProperty(value = "平台进货价")
	@TableField(exist = false)
	private BigDecimal platPrice;

	@ApiModelProperty(value = "平台销售价")
	@TableField(exist = false)
	private BigDecimal platSalePrice;

	@ApiModelProperty(value = "关键字【自营】")
	@TableField(exist = false)
	private String keywords;

}
