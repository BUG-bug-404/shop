package com.keith.modules.service.wx;

import lombok.Data;

@Data
public class PayInfo {

    //商户系统订单号
    private String orderno;

    //支付金额
    private String amount;

    //客户端ip
    private String clientip;

    //商户退款单号
    private String  refundNo;

    private String userId;

    private String totalFee;

    private String openId;

    private String description;

    //支付场景 APP 微信app支付 JSAPI 公众号支付  NATIVE 扫码支付
    private String tradeType;

    //商品ID
    private Integer id;

}
