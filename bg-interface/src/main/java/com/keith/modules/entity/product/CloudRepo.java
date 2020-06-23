package com.keith.modules.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 回收申请表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-12 09:21:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("cloud_repo")
public class CloudRepo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 店家id
	 */
	@ApiModelProperty(value = "店家id")
	private Long userMemberId;
	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Long productId;

	@ApiModelProperty(value = "商品图片")
	private String pic;

	@ApiModelProperty(value = "商品标题")
	private String productName;
	/**
	 * 回收单号
	 */
	@ApiModelProperty(value = "回收单号")
	private String repoSn;
	/**
	 * 规格id
	 */
	@ApiModelProperty(value = "规格id")
	private Long proSkuStockId;

	@ApiModelProperty(value = "规格值")
	private String proSkuValue;
	/**
	 * 此规格回收件数
	 */
	@ApiModelProperty(value = "此规格回收件数")
	private Integer count;
	/**
	 * 此单回收的件数
	 */
	@ApiModelProperty(value = "此单回收的件数")
	private Integer allCount;
	/**
	 * 此规格回收价格
	 */
	@ApiModelProperty(value = "此规格回收价格")
	private BigDecimal repoPrice;

	/**
	 * 此单总价格
	 */
	@ApiModelProperty(value = "此单总价格")
	private BigDecimal sumPrice;
	/**
	 * 此单回收的总价格
	 */
	@ApiModelProperty(value = "此规格原活动价格")
	private BigDecimal originalPrice;
	/**
	 * 此规格几折回收
	 */
	@ApiModelProperty(value = "此规格几折回收")
	private BigDecimal repoDiscount;
	/**
	 * 回收状态: 1->申请中,2->申请通过,3->拒绝通过
	 */
	@ApiModelProperty(value = "回收状态: 1->申请中,2->申请通过,3->拒绝通过")
	private Integer status;
	/**
	 * 回收申请时间
	 */
	@ApiModelProperty(value = "回收申请时间")
	private Date createTime;

}
