package com.keith.modules.service.wx;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.keith.modules.dto.wxpay.WeiXinEntPayDTO;
import com.keith.modules.dto.wxpay.WeiXinPayDTO;
import com.keith.modules.dto.wxpay.WirhDrawDTO;
import com.keith.modules.entity.bank.BankCat;
import com.keith.modules.entity.withdraw.BankDTO;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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


    /**
     * 提现到银行卡
     * @return
     */
    JSONObject getBank(BankDTO bankDTO) throws WxPayException;


    /**
     * 添加银行卡
     * @param bankDTO
     * @return
     */
    JSONObject addBank(long userId,BankDTO bankDTO);

    /**
     * 查询银行卡
     * @return
     */
    List<BankCat> findAll();


}
