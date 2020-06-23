package com.keith.modules.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品属性表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 14:05:23
 */
@Data
@TableName("pro_attribute")
public class ProAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	@ApiModelProperty(value = "")
	private Long id;
	/**
	 * */
	@ApiModelProperty(value = "")
	private Long productId;
	/**
	 * 商品属性分类id
	 */
	@ApiModelProperty(value = "商品属性分类id")
	private Long productAttributeCategoryId;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 属性选择类型：0->唯一；1->单选；2->多选；对应属性和参数意义不同；
	 */
	@ApiModelProperty(value = "属性选择类型：0->唯一；1->单选；2->多选；对应属性和参数意义不同；")
	private Integer selectType;
	/**
	 * 属性录入方式：0->手工录入；1->从列表中选取
	 */
	@ApiModelProperty(value = "属性录入方式：0->手工录入；1->从列表中选取")
	private Integer inputType;
	/**
	 * 可选值列表，以逗号隔开
	 */
	@ApiModelProperty(value = "可选值列表，以逗号隔开")
	private String inputList;
	/**
	 * 排序字段：最高的可以单独上传图片
	 */
	@ApiModelProperty(value = "排序字段：最高的可以单独上传图片")
	private Integer sort;
	/**
	 * 分类筛选样式：1->普通；1->颜色
	 */
	@ApiModelProperty(value = "分类筛选样式：1->普通；1->颜色")
	private Integer filterType;
	/**
	 * 检索类型；0->不需要进行检索；1->关键字检索；2->范围检索
	 */
	@ApiModelProperty(value = "检索类型；0->不需要进行检索；1->关键字检索；2->范围检索")
	private Integer searchType;
	/**
	 * 相同属性产品是否关联；0->不关联；1->关联
	 */
	@ApiModelProperty(value = "相同属性产品是否关联；0->不关联；1->关联")
	private Integer relatedStatus;
	/**
	 * 是否支持手动新增；0->不支持；1->支持
	 */
	@ApiModelProperty(value = "是否支持手动新增；0->不支持；1->支持")
	private Integer handAddStatus;
	/**
	 * 属性的类型；0->规格；1->参数
	 */
	@ApiModelProperty(value = "属性的类型；0->规格；1->参数")
	private Integer type;
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
