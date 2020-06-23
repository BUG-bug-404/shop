package com.keith.modules.service.sub;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dto.sub.SubAccountPayDTO;
import com.keith.modules.dto.sub.SubShopDTO;
import com.keith.modules.entity.sub.SubAccountPay;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 分账订单支付表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:11
 */
public interface SubAccountPayService extends IService<SubAccountPay> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分账
     * @return
     * @throws WxPayException
     */
    JSONObject createOrder_jsapi(SubAccountPayDTO subAccountPayDTO, HttpServletRequest request) throws WxPayException, IOException;


    /**
     * 分账回调
     * @param request
     * @return
     * @throws WxPayException
     * @throws IOException
     */
    JSONObject testProfitSharing(HttpServletRequest request) throws WxPayException, IOException, InterruptedException;

    /**
     * 添加分账用户
     * @param subShopDTOList
     * @return
     */
    JSONObject addSubShop(SubShopDTO subShopDTOList);
}

