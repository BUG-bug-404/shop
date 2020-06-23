package com.keith.modules.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户app信息
 *
 * @author gray
 * @version 1.0
 * @date 2020/6/13 15:29
 */
@Data
@TableName("user_member_app")
public class UserMemberApp implements Serializable {
    private static final long serialVersionUID = 902541036502392568L;

    @TableId(type = IdType.AUTO)
    private long id;
    @ApiModelProperty(value = "用户id")
    private long userMemberId;
    @ApiModelProperty(value = "用户申请的appId,自动生成")
    private String appId;
    @ApiModelProperty(value = "app名称，用户自己取")
    private String appName;
    @ApiModelProperty(value = "app秘钥,自动生成")
    private String appSecret;
    @ApiModelProperty(value = "app介绍")
    private String appDescription;
    @ApiModelProperty(value = "app状态")
    private int appStatus;
    @ApiModelProperty(value = "app类型")
    private int appType;
    @ApiModelProperty(value = "Ip限制")
    private String ipLimit;



}
