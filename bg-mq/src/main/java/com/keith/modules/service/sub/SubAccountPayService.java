package com.keith.modules.service.sub;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.keith.modules.entity.sub.SubAccountPay;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 分账订单支付表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:11
 */
public interface SubAccountPayService extends IService<SubAccountPay> {

    /**
     * 分账
     *
     * @return
     */
    Map<String, Object> subMoney(String orderSn) throws WxPayException;

}

