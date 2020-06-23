package com.keith.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * @author licoy.cn
 * @version 2018/1/24
 */
@Data
@ApiModel("登录信息")
public class SignInDTO {

    @NotBlank(message = "手机号！")
    private String mobile;

    @NotBlank(message = "验证码！")
    private String code;


}
