package com.keith.modules.entity.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.keith.modules.entity.template.ProTemplate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 09:39:17
 */
@Data
@TableName("pro_product")
public class ProProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	@ApiModelProperty("父id")
	private Long parentId;
	/**
	 * 供货商id
	 */
	@ApiModelProperty("商品的所属用户id")
	private Long userAdminId;
	/**
	 * 商品属性分类id
	 */
	@ApiModelProperty("分类id")
	private Long productAttributeCategoryId;
	/**
	 * 商品名称
	 */
	@ApiModelProperty("产品名称")
	private String productName;
	/**
	 * 货号
	 */
	@ApiModelProperty("货号，产品编号，原型不存在编号")
	private String productCode;
	/**
	 * 删除状态：0->未删除；1->已删除
	 */
	@ApiModelProperty("删除状态：0->未删除；1->已删除")
	private Integer deleteStatus;
	/**
	 * 审核状态：0->未审核；1->审核通过
	 */
	@ApiModelProperty("审核状态：0->未审核；1->审核通过")
	private Integer verifyStatus;
	/**
	 * 上架状态：0->下架；1->上架
	 */
	@ApiModelProperty("上架状态：0->下架；1->上架")
	private Integer publishStatus;
	/**
	 * 销量[指所有规格销量]
	 */
	@ApiModelProperty("销量[指所有规格销量]")
	private Integer sale;
	/**
	 * 供货商填入价格
	 */
	@ApiModelProperty("供货商填入价格")
	private BigDecimal supplierPrice;
	/**
	 * 副标题
	 */
	@ApiModelProperty("副标题")
	private String subTitle;
	/**
	 * 商品描述
	 */
	@ApiModelProperty("商品描述")
	private String description;
	/**
	 * 是否为预告商品：0->不是；1->是：0为现货1为预售
	 */
	@ApiModelProperty("是否为预告商品：0->不是；1->是：0为现货1为预售")
	private Integer previewStatus;
	/**
	 * 是否为统一规格：0->不是；1->是
	 */
	@ApiModelProperty("是否为统一规格：0->不是；1->是")
	private Integer unifyStatus;
	/**
	 * 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
	 */
	@ApiModelProperty("以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
	private String serviceIds;
	/**
	 * 标签
	 */
	@ApiModelProperty("标签")
	private String keywords;
	/**
	 * 关键字
	 */
	@ApiModelProperty("关键字")
	private String tag;
	/**
	 * 卖家留言
	 */
	@ApiModelProperty("卖家留言")
	private String note;
	/**
	 * 详情标题
	 */
	@ApiModelProperty("详情标题，原型没有的东西")
	private String detailTitle;
	/**
	 * 详情描述
	 */
	@ApiModelProperty("详情描述")
	private String detailDesc;
	/**
	 * 产品详情网页内容
	 */
	@ApiModelProperty("产品详情网页内容")
	private String detailHtml;
	/**
	 * 移动端网页详情
	 */
	@ApiModelProperty("移动端网页详情")
	private String detailMobileHtml;
	/**
	 * 产品分类名称
	 */
	@ApiModelProperty("产品分类名称")
	private String productCategoryName;
	/**
	 * 产品发货地址
	 */
	@ApiModelProperty("产品发货地址")
	private String productAddress;
	/**
	 * 运费模板
	 */
	@ApiModelProperty(" 运费模板")
	private Long templateId;

	/**
	 * 库存
	 */
	private Integer totalNum;

	/**
	 * 是否包邮
	 */
	@ApiModelProperty("是否包邮")
	private Integer postageStatus;
	/**
	 * 几日一结算
	 */
	@ApiModelProperty("几日一结算")
	private Integer finalPay;
	/**
	 * 平台销售价格
	 */
	@ApiModelProperty("平台销售价格")
	private BigDecimal platformPrice;
	/**
	 * 平台批发铺货价格/进价价格
	 */
	@ApiModelProperty("平台批发铺货价格/进价价格")
	private BigDecimal salePrice;
	/**
	 * 添加人
	 */
	@ApiModelProperty("添加人")
	private String createPerson;
	/**
	 * 添加时间
	 */
	@ApiModelProperty("添加时间")
	private Date createTime;
	/**
	 * 修改人
	 */
	@ApiModelProperty("修改人")
	private String changePerson;
	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date changeTime;
	/**
	 * 上架时间
	 */
	@ApiModelProperty("上架时间")
	private Date publishTime;
	/**
	 * 是否添加到云仓：0->不是；1->是
	 */
	@ApiModelProperty("是否添加到云仓：0->不是；1->是")
	private Integer isCloud;
	/**
	 * 云仓上架状态：0->下架；1->上架
	 */
	@ApiModelProperty("云仓上架状态：0->下架；1->上架")
	private Integer cloudPublishStatus;
	/**
	 * 云仓上架时间
	 */
	@ApiModelProperty("云仓上架时间")
	private Date cloudPublishTime;
	/**
	 * 铺货量
	 */
	@ApiModelProperty("铺货量")
	private Integer extCount;

	/**
	 * 图片
	 */
	@ApiModelProperty("图片")
	@TableField(exist = false)
	private String pic;

	/**
	 *  浏览量*/

	@ApiModelProperty("浏览量")
	private Integer pvCount;
	/**
	 * 订单量
	 * */
	@ApiModelProperty("订单量")
	private Integer orderCount;

	/**
	 * 图片集
	 */
	@ApiModelProperty("图片集")
	@TableField(exist = false)
	private List<ProAlbum> albumEntityList;

	/**
	 * 规格属性
	 */
	@ApiModelProperty("规格属性")
	@TableField(exist = false)
	private List<ProAttribute> attributeEntityList;

	/**
	 * 规格
	 */
	@ApiModelProperty(" 规格")
	@TableField(exist = false)
	private List<ProSkuStock> skuStockEntityList;


	/**
	 *规格参数
	 */
	@ApiModelProperty("规格参数")
	@TableField(exist = false)
	private List sku;


	@ApiModelProperty("运费模板")
	@TableField(exist = false)
	private List<ProTemplate> template;
	@ApiModelProperty("总库存")
	@TableField(exist = false)
	private Integer  totalInventory;

}
