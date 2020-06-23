package com.keith.modules.service.wx;

import lombok.Data;

@Data
public class EntPayInfo {

    //设备号(可选)
    private String deviceInfo;
    //随机字符串
    private String nonceStr;
    //商户订单号，商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其他字符)
    private String partnerTradeNo;
    //用户openid
    private String openid;
    //校验用户姓名选项（NO_CHECK：不校验真实姓名，FORCE_CHECK：强校验真实姓名）
    private String checkName;
    //收款用户姓名 (可选) 根据上个来确认是否需要填写
    private String reUserName;
    //金额。单位：分
    private int amount;
    //企业付款备注
    private String description;
    //Ip地址
    private String spbillCreateIp;
}
