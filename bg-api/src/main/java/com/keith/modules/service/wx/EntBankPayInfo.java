package com.keith.modules.service.wx;

import lombok.Data;

/**
 *
 */
@Data
public class EntBankPayInfo {
    /**
     * 商户企业付款单号.
     */
    private String partnerTradeNo;

    /**
     * 收款方银行卡号.
     */
    private String encBankNo;
    /**
     * 收款方用户名.
     */
    private String encTrueName;

    /**
     * 收款方开户行.
     */
    private String bankCode;

    /**
     * 付款金额.
     */
    private Integer amount;

    /**
     * 付款说明.
     */
    private String description;

}
