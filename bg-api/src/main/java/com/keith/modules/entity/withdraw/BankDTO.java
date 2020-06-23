package com.keith.modules.entity.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("添加银行卡参数")
public class BankDTO {

    /**
     * 收款方银行卡号.
     */
    @ApiModelProperty("收款方银行卡号")
    private String encBankNo;
    /**
     * 收款方用户名.
     */
    @ApiModelProperty("收款方用户名")
    private String encTrueName;

    /**
     * 收款方开户行.
     */
    @ApiModelProperty("银行卡编号")
    private String bankCode;

    /**
     * 付款金额.
     */
    @ApiModelProperty("付款金额")
    private Integer amount;

    /**
     * 银行卡名称
     */
    @ApiModelProperty("银行卡名称")
    private String bankName;
}
