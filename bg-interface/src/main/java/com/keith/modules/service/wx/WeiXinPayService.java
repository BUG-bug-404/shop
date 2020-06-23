package com.keith.modules.service.wx;

import com.alibaba.fastjson.JSONObject;
import com.keith.modules.dto.wxpay.WeiXinEntPayDTO;
import com.keith.modules.dto.wxpay.WeiXinPayDTO;
import com.keith.modules.dto.wxpay.WirhDrawDTO;

import javax.servlet.http.HttpServletRequest;

public interface WeiXinPayService {

    /**
     * 统一下单
     * @param request
     * @param response
     * @return
     */
    JSONObject createUnifiedOrder(Long userId, WeiXinPayDTO weiXinPayDTO, HttpServletRequest request);

    /**
     * 支付回调地址
     * @param request
     * @return
     */
    String getWxpayNotifyInfo(HttpServletRequest request);

    /**
     * 退款业务
     * @param userId
     * @param orderSn
     * @param request
     * @return
     */
    JSONObject getWxpayRefund(Long userId, String orderSn, HttpServletRequest request);


    /**
     * 添加提现记录
     * @param userId
     * @param wirhDrawDTO
     * @return
     */
    JSONObject addWithDraw(Long userId, WirhDrawDTO wirhDrawDTO);


    /**
     * 提现
     * @param weiXinEntPayDTO
     * @param request
     * @return
     */
    JSONObject getEntPay(long userId,WeiXinEntPayDTO weiXinEntPayDTO, HttpServletRequest request);



}
