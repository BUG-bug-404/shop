package com.keith.modules.service.wx.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.util.SnowflakeIdWorker;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.keith.common.exception.RRException;
import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.order.OrderPaymentInfoDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.dao.user.UserBanlanceDao;
import com.keith.modules.dao.withdraw.SetWithdrawDao;
import com.keith.modules.dao.withdraw.UserMemberWithdrawDao;
import com.keith.modules.dto.wxpay.WeiXinEntPayDTO;
import com.keith.modules.dto.wxpay.WeiXinPayDTO;
import com.keith.modules.dto.wxpay.WirhDrawDTO;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.order.OrderPaymentInfo;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.user.UserBanlance;
import com.keith.modules.entity.withdraw.SetWithdraw;
import com.keith.modules.entity.withdraw.UserMemberWithdraw;
import com.keith.modules.service.wx.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("wxPayService")
public class WxPayServiceImpl implements WeiXinPayService {
    private static final Logger log = LoggerFactory.getLogger(WxPayServiceImpl.class);
    @Autowired
    private OrderOrderDao orderDao;
    @Autowired
    private OrderItemDao itemDao;

    @Autowired
    private OrderPaymentInfoDao paymentInfoDao;
    @Autowired
    private ProSkuStockDao skuStockDao;

    @Autowired
    private UserMemberWithdrawDao userMemberWithdrawDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SetWithdrawDao setWithdrawDao;
    @Autowired
    private UserBanlanceDao userBanlanceDao;

    JSONObject obj = new JSONObject();

    @Override
    public JSONObject createUnifiedOrder(Long userId, WeiXinPayDTO weiXinPayDTO, HttpServletRequest request) {
        //查询订单
        List<OrderOrder> orderOrders = orderDao.selectList(new QueryWrapper<OrderOrder>().eq("user_member_id", userId).eq("unify_order_sn", weiXinPayDTO.getOrderSn()).eq("status", 0));
        if (orderOrders.size()==0) {
            obj.put("code", 1);
            obj.put("msg", "用户订单信息有误！");
            return obj;
        }
        OrderOrder orderOrder = new OrderOrder();
        orderOrder.setPaymentTime(new Date());
        orderOrder.setPayType(2);
        orderDao.update(orderOrder, new QueryWrapper<OrderOrder>().eq("user_member_id", userId).eq("unify_order_sn", weiXinPayDTO.getOrderSn()));
        try {

            //微信支付
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            WxPayService wxPayService = WxPay.wxPayService();
            orderRequest.setBody("小程序");
            orderRequest.setOutTradeNo(weiXinPayDTO.getOrderSn());
            orderRequest.setOpenid(weiXinPayDTO.getOpenId());

            BigDecimal totalAmount = orderOrders.stream().map(OrderOrder::getTotalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            //15分钟过期
            Date date = new Date();
            date.setTime(date.getTime() + 15 * 60 * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String createdate = sdf.format(date);
            orderRequest.setTimeExpire(createdate);
            //元转成分
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen("0.01"));
            orderRequest.setSpbillCreateIp(CommonUtils.getRealIp(request));
            Object order = wxPayService.createOrder(orderRequest);
            log.info("微信支付，我掉起来了！");

            log.info("回调开始");
            // getWxpayNotifyInfo(request);
            log.info("回调结束");

            obj.put("data", order);
            obj.put("code", 0);
            return obj;


        } catch (Exception e) {
            log.error("微信支付失败！订单号：{},原因:{}", e.getMessage());
            // e.printStackTrace();
            obj.put("code", 1);
            obj.put("msg", "支付失败，请稍后重试！" + e.getMessage());
            return obj;
        }

    }


    public String getWxpayNotifyInfo(HttpServletRequest request) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayService wxPayService = WxPay.wxPayService();
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            log.info("微信支付回调1---------------result" + result.toString());
            String orderId = result.getOutTradeNo();
            log.info("orderId++" + orderId);
            String tradeNo = result.getTransactionId();
            log.info("tradeNo++" + tradeNo);
            String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
            log.info("totalFee++" + totalFee);
            //do something
            //SUCCESS结果
            String resultCode = result.getResultCode();
            if ("SUCCESS".equals(resultCode) || "success".equals(resultCode)) {
                Date date = new Date();
                //微信支付成功
                OrderPaymentInfo orderPaymentInfo = paymentInfoDao.selectOne(new QueryWrapper<OrderPaymentInfo>().eq("order_sn", orderId));

                OrderOrder order = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("order_sn", orderId));
                OrderItem item = itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", orderId));
                if (item != null) {
                    ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                    skuStock.setStock(skuStock.getStock() - item.getProductQuantity());
                    skuStock.setSale(skuStock.getSale() + item.getProductQuantity());
                    skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                }
                if (order.getStatus() == 0) {

                    order.setStatus(2);
                    orderDao.update(order, new QueryWrapper<OrderOrder>().eq("order_sn", orderId));
                    log.info("修改订单状态成功");
                }
                if (orderPaymentInfo == null) {
                    OrderPaymentInfo paymentInfo = new OrderPaymentInfo();
                    paymentInfo.setOrderSn(orderId);
                    paymentInfo.setPayTradeNo(tradeNo);
                    paymentInfo.setTotalAmount(new BigDecimal(totalFee));
                    paymentInfo.setPaymentStatus("0");
                    paymentInfo.setCreateTime(date);
                    paymentInfo.setCallbackTime(date);
                    paymentInfoDao.insert(paymentInfo);
                    log.info("插入支付信息");
                }


                log.info("支付回调成功！开始处理自己的业务");
                //微信支付成功
                //修改订单状态处理自己的业务
            } else {
                //支付失败
                log.info("微信支付失败回调-----------------------------------商品订单号" + orderId);
            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    @Override
    public JSONObject getWxpayRefund(Long userId, String orderSn, HttpServletRequest request) {
        //查询订单
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("user_member_id", userId).eq("order_sn", orderSn));
        OrderPaymentInfo paymentInfo = paymentInfoDao.selectOne(new QueryWrapper<OrderPaymentInfo>().eq("order_sn", orderSn));
        if (ObjectUtil.isNull(orderOrder)) {
            obj.put("code", 1);
            obj.put("msg", "用户订单信息有误！");
            return obj;
        }
        WxPayService wxPayService = WxPay.wxPayService();
        PayInfo payInfo = new PayInfo();
        /*
        //商户订单号	out_trade_no 或者 微信订单号	transaction_id
        //商户退款单号	out_refund_no
        //订单金额	total_fee
        //退款金额	refund_fee
         */
        String outRefundNo = "wxrefund" + orderOrder.getUserMemberId() + System.currentTimeMillis();
        //微信流水号
        payInfo.setOrderno(paymentInfo.getPayTradeNo());
        //微信退款订单号
        payInfo.setRefundNo(outRefundNo);
        //退款金额
        payInfo.setAmount(orderOrder.getTotalAmount().toString());
        //微信退款操作
        WxPayRefundRequest wxPayRefund = WxPay.WxPayRefundResult(payInfo);
        WxPayRefundResult wxPayRefundResult = null;
        try {
            wxPayRefundResult = wxPayService.refund(wxPayRefund);
        } catch (WxPayException e) {
            e.printStackTrace();
        }

        if (wxPayRefundResult == null) {
            wxPayRefundResult = new WxPayRefundResult();
            wxPayRefundResult.setResultCode("error");
            wxPayRefundResult.setOutTradeNo(orderSn);
        }
        String msg = wxPayRefund(wxPayRefundResult);
        if ("退款成功".equals(msg)) {



            obj.put("code", 0);
            log.info("退款成功！");
        } else {
            obj.put("code", 1);
            log.info("退款失败！");
        }
        obj.put("msg", msg);
        return obj;
    }

    @Override
    @Transactional
    public JSONObject addWithDraw(Long userId, WirhDrawDTO wirhDrawDTO) {

       int num= userMemberWithdrawDao.countNum(userId);
        SetWithdraw setWithdraw=setWithdrawDao.selectOne(new QueryWrapper<SetWithdraw>());
        if(setWithdraw==null){
            throw new RRException("提现设置异常无法提现！");
        }
       if(num>setWithdraw.getNum().intValue()){
           throw new RRException("本月已提现！");
       }
        UserBanlance userBanlance=userBanlanceDao.selectOne(new QueryWrapper<UserBanlance>().eq("user_member_id",userId));
       if(userBanlance==null){
           throw new RRException("无余额信息！");
       }
       userBanlance.setBanlance(userBanlance.getBanlance().subtract(wirhDrawDTO.getMoney()));
        userBanlanceDao.update(userBanlance,new QueryWrapper<UserBanlance>().eq("user_member_id",userId));

        UserMemberWithdraw userMemberWithdraw = new UserMemberWithdraw();
        userMemberWithdraw.setUserMemberId(userId);
        userMemberWithdraw.setCreateTime(new Date());
        userMemberWithdraw.setStyle(wirhDrawDTO.getType());
        userMemberWithdraw.setAcceptAccount(wirhDrawDTO.getOpenId());
        userMemberWithdraw.setPrice(wirhDrawDTO.getMoney());
        userMemberWithdraw.setType(0);
        userMemberWithdrawDao.insert(userMemberWithdraw);




        return obj;
    }


    /**
     * 提现
     *
     * @param userId
     * @param weiXinEntPayDTO
     * @param request
     * @return
     */
    @Override
    public JSONObject getEntPay(long userId, WeiXinEntPayDTO weiXinEntPayDTO, HttpServletRequest request) {

        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());

            EntPayInfo entPayInfo = new EntPayInfo();
            entPayInfo.setOpenid(weiXinEntPayDTO.getOpenId());
            entPayInfo.setAmount(weiXinEntPayDTO.getMoney());
            entPayInfo.setCheckName("NO_CHECK");
            entPayInfo.setDescription("提现");

            //用来生成32位随机字符
            SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
            String nonceStr = RandomStringUtils.randomAlphanumeric(32);
            String sn = String.valueOf(idWorker.nextId());
            entPayInfo.setNonceStr(nonceStr);
            entPayInfo.setPartnerTradeNo(sn);
            entPayInfo.setSpbillCreateIp("182.92.232.224");

            EntPayResult entPayRequest = WxPay.entPay(entPayInfo);

            /*UserMemberWithdraw userMemberWithdraw = new UserMemberWithdraw();
            userMemberWithdraw.setUserMemberId(userId);
            userMemberWithdraw.setCreateTime(new Date());
            userMemberWithdraw.setStyle(1);
            userMemberWithdraw.setAcceptAccount(weiXinEntPayDTO.getOpenId());
            userMemberWithdraw.setPrice(new BigDecimal(weiXinEntPayDTO.getMoney()));
            userMemberWithdraw.setType(0);
            userMemberWithdrawDao.insert(userMemberWithdraw);*/

            log.info("+++++++++++++" + entPayRequest);
        } catch (WxPayException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }


    /**
     * 退款业务
     *
     * @return
     */
    public String wxPayRefund(WxPayRefundResult wxPayRefundResult) {
        //状态
        String resultCode = wxPayRefundResult.getResultCode();
        //商品订单号
        String outTradeNo = wxPayRefundResult.getOutTradeNo();
        log.info("微信退款回调2----------------outTradeNo" + outTradeNo);
        //微信支付流水号
        String transactionId = wxPayRefundResult.getTransactionId();
        //微信退款单号
        String outRefundNo = wxPayRefundResult.getOutRefundNo();
        //微信退款流水号
        String refundId = wxPayRefundResult.getRefundId();

        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("order_sn", outTradeNo));
        OrderPaymentInfo paymentInfo = paymentInfoDao.selectOne(new QueryWrapper<OrderPaymentInfo>().eq("order_sn", outTradeNo));

        if ("SUCCESS".equals(resultCode) || "success".equals(resultCode)) {
            //退款成功
            log.info("退款回调成功");
            if (orderOrder != null) {
                orderOrder.setStatus(9);
                orderDao.update(orderOrder, new QueryWrapper<OrderOrder>().eq("order_sn", outTradeNo));
                OrderItem item = itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", outTradeNo));
                ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                skuStock.setStock(skuStock.getStock() + item.getProductQuantity());
                skuStock.setSale(skuStock.getSale() - item.getProductQuantity());
                skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
            }

            OrderOrder order = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("order_sn", outTradeNo));
            OrderItem item = itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", outTradeNo));

            if (order == null || item == null) {
                throw new RRException("订单信息有误！");
            }
            ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
            if (order.getStatus() == 0) {
                order.setStatus(7);
                orderDao.update(order, new QueryWrapper<OrderOrder>().eq("order_sn", outTradeNo));
                if (skuStock != null) {
                    skuStock.setStock(skuStock.getStock() + item.getProductQuantity());
                    skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                    int stock = Integer.parseInt(redisTemplate.opsForValue().get(item.getProductSkuId() + ""));
                    redisTemplate.opsForValue().set(skuStock.getId() + "", skuStock.getStock() + "");
                }
            }


            return "退款成功";
        } else {
            //退款失败
            log.info("退款回调失败");
            return "退款失败";
        }
    }


}
