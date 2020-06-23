package com.keith.modules.dto.order;

import com.keith.modules.dto.SplitPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单中心")
public class MyOrderDTO  extends SplitPageDTO {

    @ApiModelProperty(value = "订单状态：0->待付款；2->已付款,3->已发货；4->已收货；5->已完成 6->已关闭；7->已取消，9,退款")
    private Integer status;

    @ApiModelProperty(value = "名称")
    private String productName;
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

    @ApiModelProperty(value = "详细地址")
    private String detailTime;

}
