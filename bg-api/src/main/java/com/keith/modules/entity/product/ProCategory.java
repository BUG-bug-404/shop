package com.keith.modules.entity.product;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品分类表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-02 13:38:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("pro_category")
public class ProCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 上级分类的编号：0表示一级分类
	 */
	@ApiModelProperty(value = "上级分类的编号：0表示一级分类")
	private Long parentId;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 分类级别：0->1级；1->2级
	 */
	@ApiModelProperty(value = "分类级别：0->1级；1->2级")
	private Integer level;
	/**
	 * 商品数量
	 */
	@ApiModelProperty(value = "商品数量")
	private Integer productCount;
	/**
	 * 商品单位
	 */
	@ApiModelProperty(value = "商品单位")
	private String productUnit;
	/**
	 * 是否显示在导航栏：0->不显示；1->显示
	 */
	@ApiModelProperty(value = "是否显示在导航栏：0->不显示；1->显示")
	private Integer navStatus;
	/**
	 * 显示状态：0->不显示；1->显示
	 */
	@ApiModelProperty(value = "显示状态：0->不显示；1->显示")
	private Integer showStatus;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;
	/**
	 * 图标
	 */
	@ApiModelProperty(value = "图标")
	private String icon;
	/**
	 * 关键字
	 */
	@ApiModelProperty(value = "关键字")
	private String keywords;
	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;
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

	@TableField(exist = false)
	private List<ProCategory> proCategories;

}
