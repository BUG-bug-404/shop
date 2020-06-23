package com.keith.modules.service.wx;

import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WxPay {

    private static String appId= "wx2bcd532cf2aa1f38";

    private static String mchId="1535221551";

    private static String apiKey="59abfbba1984d38bb0de473fdf725e0d";

    private static String notifyUrl="http://crm.aissyun.com:8081/api/wxPay/getWxpayNotifyInfo";

    private static String congZhiNotifyUrl="http://crm.aissyun.com:8081/api/userbanlance/wxPayNotifyPay";

    private static String notifyProUrl="http://jnx9gw.natappfree.cc/api/wxPay/testProfitSharing";

    private static String keyPath;
    public static String getKeyPath() {
        return keyPath;
    }
    @Value("${pay.keyPath}")
    public void setKeyPath(String keyPath) {
        WxPay.keyPath = keyPath;


    }

    private static String payRefundRul="";


    /**
     * 统一下单支付
     * @return
     */
    public static WxPayService wxPayService(){
        System.out.println();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(appId));
        payConfig.setMchId(StringUtils.trimToNull(mchId));
        payConfig.setMchKey(StringUtils.trimToNull(apiKey));
        payConfig.setKeyPath(keyPath);
        payConfig.setNotifyUrl(notifyUrl);
        payConfig.setTradeType("JSAPI");
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }



    /**
     * 分账
     * @return
     * @throws WxPayException
     */
    public static WxPayService createOrderJSAPI(){
        System.out.println();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(appId));
        payConfig.setMchId(StringUtils.trimToNull(mchId));
        payConfig.setMchKey(StringUtils.trimToNull(apiKey));
        payConfig.setKeyPath(keyPath);
        payConfig.setNotifyUrl(notifyProUrl);
        payConfig.setTradeType("JSAPI");
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }



    /**
     * 充值
     * @return
     */
    public static WxPayService wxZhiPayService(){
        System.out.println();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(appId));
        payConfig.setMchId(StringUtils.trimToNull(mchId));
        payConfig.setMchKey(StringUtils.trimToNull(apiKey));
        //payConfig.setKeyPath(keyPath);
        payConfig.setNotifyUrl(congZhiNotifyUrl);
        payConfig.setTradeType("JSAPI");
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 微信退款
     * @param payInfo
     * @return
     */
    public static WxPayRefundRequest WxPayRefundResult(PayInfo payInfo){
        WxPayRefundRequest wxPayRefundRequest=new WxPayRefundRequest();
        //商户订单号	out_trade_no 或者 微信订单号	transaction_id
        //商户退款单号	out_refund_no
        //订单金额	total_fee
        //退款金额	refund_fee
       /* wxPayRefundRequest.setRefundFee(1);
        wxPayRefundRequest.setTotalFee(1);*/
        wxPayRefundRequest.setTransactionId(payInfo.getOrderno());
        wxPayRefundRequest.setOutRefundNo(payInfo.getRefundNo());
        wxPayRefundRequest.setTotalFee(BaseWxPayRequest.yuanToFen(payInfo.getAmount()));
        wxPayRefundRequest.setRefundFee(BaseWxPayRequest.yuanToFen(payInfo.getAmount()));
        wxPayRefundRequest.setNotifyUrl(payRefundRul);
        return wxPayRefundRequest;
    }


    /**
     *    提现
     *
     *  deviceInfo： 设备号(可选)
     *  nonceStr：随机字符串
     *  partnerTradeNo：商户订单号，商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其他字符)
     *  openid：用户openid
     *  checkName：校验用户姓名选项（NO_CHECK：不校验真实姓名，FORCE_CHECK：强校验真实姓名）
     *  reUserName：收款用户姓名 (可选) 根据上个来确认是否需要填写
     *  amount：金额。单位：分
     *  description：企业付款备注
     *  spbillCreateIp：Ip地址
     *
     */

    public static EntPayResult entPay(EntPayInfo entPayInfo) throws WxPayException {
        EntPayRequest request=new EntPayRequest();
        request.setDeviceInfo(entPayInfo.getDeviceInfo());
        request.setNonceStr(entPayInfo.getNonceStr());
        request.setPartnerTradeNo(entPayInfo.getPartnerTradeNo());
        request.setOpenid(entPayInfo.getOpenid());
        request.setCheckName(entPayInfo.getCheckName());
        request.setReUserName(entPayInfo.getReUserName());
        request.setAmount(entPayInfo.getAmount());
        request.setDescription(entPayInfo.getDescription());
        request.setSpbillCreateIp(entPayInfo.getSpbillCreateIp());
        return wxPayService().getEntPayService().entPay(request);
    }

}
