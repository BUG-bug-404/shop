package com.keith.modules.dto.order;


import com.keith.modules.dto.SplitPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class OrderTrackDTO extends SplitPageDTO {

    @ApiModelProperty("时间")
    private Integer time;

    @ApiModelProperty("1->待发货；,3->已发货；")
    Integer status;
}
