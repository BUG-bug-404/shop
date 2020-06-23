package com.keith.modules.service.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lzy
 * @date 2020/6/11 18:44
 */
@Data
public class WxUserInfo {
    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "头像")
    private String pic;

    @ApiModelProperty(value = "地址")
    private String address;
}
