package com.keith.modules.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品图集
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 11:05:20
 */
@Data
@TableName("pro_album")
public class ProAlbum implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	@ApiModelProperty(value = "id")
	private Long id;
	/**
	 * */
	@ApiModelProperty(value = "商品id")
	private Long productId;
	/**
	 * */
	@ApiModelProperty(value = "图片")
	private String pic;
	/**
	 * 是否为封面0为封面1不是
	 */
	@ApiModelProperty(value = "是否为封面0为封面1不是")
	private Integer coverStatus;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;
	/**
	 * 是否首页展示
	 */
	@ApiModelProperty(value = "是否首页展示")
	private Integer status;
	/**
	 * 图片状态
	 */
	@ApiModelProperty(value = "图片状态")
	private Integer deleteStatus;
	/**
	 * 0.供应商，1.平台，2.商家
	 */
	@ApiModelProperty(value = "0.供应商，1.平台，2.商家")
	private Integer picOwner;
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
	/**
	 * */
	@ApiModelProperty(value = "")
	private String description;

}
