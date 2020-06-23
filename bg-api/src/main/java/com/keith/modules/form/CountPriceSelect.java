package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Lzy
 * @date 2020/6/23 12:21
 */
@Data
public class CountPriceSelect {

    @ApiModelProperty(value = "购物车id")
    private List<Long> id;

    @ApiModelProperty(value = "地址id")
    private Long addressId;

}
