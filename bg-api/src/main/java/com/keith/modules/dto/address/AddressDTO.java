package com.keith.modules.dto.address;

import com.keith.modules.dto.SplitPageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
public class AddressDTO extends SplitPageDTO {


	private  Long id;
	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String receiveName;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "收货人手机号码")
	private String phone;
	/**
	 * 是否为默认
	 */

	@ApiModelProperty(value = "是否为默认")
	private Integer defaultStatus;
	/**
	 * 省份
	 */
	@ApiModelProperty(value = "省份")
	private String province;
	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String city;
	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String region;
	/**
	 * 详细地址
	 */

	@ApiModelProperty(value = "详细地址")
	private String detailTime;

}
