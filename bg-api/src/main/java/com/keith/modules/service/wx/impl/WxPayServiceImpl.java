package com.keith.modules.service.wx.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.util.SnowflakeIdWorker;
import com.github.binarywang.wxpay.bean.entpay.EntPayBankResult;
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
import com.keith.modules.dao.ExtProduct.ExtProductDao;
import com.keith.modules.dao.ExtProduct.ExtProductSkuDao;
import com.keith.modules.dao.bank.BankCatDao;
import com.keith.modules.dao.bank.BankDao;
import com.keith.modules.dao.order.OrderCartItemDao;
import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.order.OrderPaymentInfoDao;
import com.keith.modules.dao.product.CloudManagementDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.dao.user.UserBanlanceDao;
import com.keith.modules.dao.withdraw.SetWithdrawDao;
import com.keith.modules.dao.withdraw.UserMemberWithdrawDao;
import com.keith.modules.dto.wxpay.WeiXinEntPayDTO;
import com.keith.modules.dto.wxpay.WeiXinPayDTO;
import com.keith.modules.dto.wxpay.WirhDrawDTO;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.entity.ExtProduct.ExtProductSku;
import com.keith.modules.entity.bank.Bank;
import com.keith.modules.entity.bank.BankCat;
import com.keith.modules.entity.order.OrderCartItem;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.order.OrderPaymentInfo;
import com.keith.modules.entity.product.CloudManagement;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.user.UserBanlance;
import com.keith.modules.entity.withdraw.BankDTO;
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
import java.time.LocalDateTime;
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
    @Autowired
    private BankDao bankDao;
    @Autowired
    private BankCatDao bankCatDao;

    @Autowired
    private ExtProductDao extProductDao;
    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private CloudManagementDao cloudManagementDao;
    @Autowired
    private ExtProductSkuDao extProductSkuDao;
    @Autowired
    private OrderCartItemDao orderCartItemDao;


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

            //15分钟过期
            Date date = new Date();
            date.setTime(date.getTime() + 15 * 60 * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String createdate = sdf.format(date);
            orderRequest.setTimeExpire(createdate);

            /**
             * 订单总金额
             */
            BigDecimal totalAmount = orderOrders.stream().map(OrderOrder::getTotalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            //元转成分
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(totalAmount.toString()));
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
            log.info(resultCode);
            if ("SUCCESS".equals(resultCode) || "success".equals(resultCode)) {
                Date date = new Date();
                //微信支付成功
                OrderPaymentInfo orderPaymentInfo = paymentInfoDao.selectOne(new QueryWrapper<OrderPaymentInfo>().eq("order_sn", orderId));
                /**
                 * ys预售-yc云仓-sy付尾款的预售订单-yg代下单减云仓库存
                 */
                String str = orderId.substring(0,2);
                if("ys".equals(str)){
                    addCartItem(orderId);
                    devide(orderId,"");
                    updateStatus(orderId,"ys");
                }else if("sy".equals(str)){
                    delete(orderId);
                    updateStatus(orderId,"sy");
                }else if("yg".equals(str)){
                    updateStatus(orderId,"yg");
                    /*目前单规格*/
                    devide(orderId,"yg");

                }else {
                    List<OrderOrder> orderOrders = orderDao.selectList(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderId));
                    orderOrders.stream().forEach(order -> {
                        List<OrderItem> orderItems = itemDao.selectList(new QueryWrapper<OrderItem>().eq("order_sn", order.getOrderSn()));

                        orderItems.stream().forEach(item ->{ if (item != null) {
                            ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                            skuStock.setStock(skuStock.getStock() - item.getProductQuantity());
                            skuStock.setSale(skuStock.getSale() + item.getProductQuantity());
                            skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                            if("yc".equals(str)){
                                /*铺货到云仓*/
                                andExtCloud(order.getUserMemberId(),order.getProductId(),item.getProductSkuId(),item.getProductQuantity());
                            }
                        }} );

                        if (order.getStatus() == 0) {
                            order.setStatus(2);
                            orderDao.update(order, new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderId));
                            log.info("修改订单状态成功");
                        }

                    });
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
            e.printStackTrace();
            log.error("微信回调结果异常,异常原因{}", e);
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

        int num = userMemberWithdrawDao.countNum(userId);
        SetWithdraw setWithdraw = setWithdrawDao.selectOne(new QueryWrapper<SetWithdraw>());
        if (setWithdraw == null) {
            throw new RRException("提现设置异常无法提现！");
        }
        if (num > setWithdraw.getNum().intValue()) {
            throw new RRException("本月已提现！");
        }
        if (setWithdraw.getMoneyType() == 1) {
            if (wirhDrawDTO.getMoney().compareTo(setWithdraw.getMinMoney()) == -1) {
                throw new RRException("提现金额不能小于" + setWithdraw.getMinMoney());
            }
        }
        UserBanlance userBanlance = userBanlanceDao.selectOne(new QueryWrapper<UserBanlance>().eq("user_member_id", userId));
        if (userBanlance == null) {
            throw new RRException("暂无余额信息！");
        }
        userBanlance.setBanlance(userBanlance.getBanlance().subtract(wirhDrawDTO.getMoney()));
        userBanlanceDao.update(userBanlance, new QueryWrapper<UserBanlance>().eq("user_member_id", userId));

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

    @Override
    public JSONObject getBank(BankDTO bankDTO) throws WxPayException {

        EntBankPayInfo bankPayInfo = new EntBankPayInfo();
        bankPayInfo.setAmount(bankDTO.getAmount());
        bankPayInfo.setBankCode(bankDTO.getBankCode());
        bankPayInfo.setDescription("银行卡提现");
        bankPayInfo.setEncBankNo(bankDTO.getEncBankNo());
        bankPayInfo.setEncTrueName(bankDTO.getEncTrueName());

        //用来生成32位随机字符
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        String nonceStr = RandomStringUtils.randomAlphanumeric(32);
        String sn = String.valueOf(idWorker.nextId());
        bankPayInfo.setPartnerTradeNo(sn);
        EntPayBankResult entPayBankResult = WxPay.entBankPay(bankPayInfo);
        log.info(entPayBankResult.toString());


        return obj;
    }

    @Override
    public JSONObject addBank(long userId,BankDTO bankDTO) {
        Bank bank = new Bank();
        bank.setBankCode(bankDTO.getBankCode());
        bank.setUserId(userId);
        bank.setBankNo(bankDTO.getEncBankNo());
        bank.setCreateDate(new Date());
        bank.setTrueName(bankDTO.getEncTrueName());
        bank.setBankName(bankDTO.getBankName());
        bankDao.insert(bank);
        obj.put("msg","添加银行卡成功！");

        return obj;
    }



    @Override
    public List<BankCat> findAll() {
        List<BankCat> list=bankCatDao.selectList(new QueryWrapper<BankCat>());
        return list;
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


    /**铺货到云仓*/
    public void andExtCloud(Long memberId,Long productId,Long skuId,Integer count){
        ExtProduct selectOne = extProductDao.selectOne(new QueryWrapper<ExtProduct>().
                eq("product_id", productId).
                eq("user_member_id", memberId));
        ProProduct product = proProductDao.selectById(productId);
        if(selectOne == null) {
            ExtProduct extProduct = new ExtProduct();
            extProduct.setUserMemberId(memberId);
            extProduct.setProductId(productId);
            extProduct.setDeleteStatus(0);
            extProduct.setPublishStatus(0);
            extProduct.setSale(0);
            extProduct.setCloudStatus(1);
            extProduct.setProductName(product.getProductName());
            extProduct.setCloudStock(count);
            ProProduct proProduct = proProductDao.selectById(productId);
            CloudManagement cloudManagement = cloudManagementDao.selectOne(new QueryWrapper<CloudManagement>().eq("pro_sku_stock_id", skuId).eq("product_id", productId));
            extProduct.setCloudId(cloudManagement.getId().intValue());

            extProduct.setDescription(proProduct.getDescription());
            extProductDao.insert(extProduct);

            ExtProductSku extProductSku = new ExtProductSku();
            extProductSku.setExtProductId(extProduct.getId());
            extProductSku.setProductId(productId);
            extProductSku.setDeleteStatus(0);
            extProductSku.setSupplierPrice(new BigDecimal("0"));
            extProductSku.setStock(count.longValue());
            extProductSkuDao.insert(extProductSku);

        }else {
            int num = 0;
            Integer cloudStock = selectOne.getCloudStock();
            num = cloudStock + count;
            ExtProductSku extProductSku = extProductSkuDao.selectOne(new QueryWrapper<ExtProductSku>().
                    eq("product_id", productId).eq("sku_stock_id", skuId).
                    eq("ext_product_id", selectOne.getId()));
            if(extProductSku == null){
                ExtProductSku extP = new ExtProductSku();
                extP.setExtProductId(selectOne.getId());
                extP.setProductId(productId);
                extP.setDeleteStatus(0);
                extP.setSupplierPrice(new BigDecimal("0"));
                extP.setStock(count.longValue());
                extProductSkuDao.insert(extProductSku);
                selectOne.setCloudStatus(num);
                extProductDao.updateById(selectOne);
            }
        }
    }

    /**预售订单加入到进货车*/
    public void addCartItem(String orderSn){
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn));
        if(orderOrder == null ){
            throw new RRException("订单有误！");
        }
        String orderSn1 = orderOrder.getOrderSn();
        OrderItem orderItem = itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn",orderSn1));
        if(orderItem == null ){
            throw new RRException("订单有误！");
        }
        ProProduct proProduct = proProductDao.selectById(orderOrder.getProductId());
        if(proProduct == null ){
            throw new RRException("订单有误！");
        }
        OrderCartItem orderCartItem = new OrderCartItem();
        orderCartItem.setProductId(proProduct.getId());
        orderCartItem.setQuantity(orderItem.getProductQuantity());
        orderCartItem.setUserMemberId(orderOrder.getUserMemberId());
//        orderCartItem.setProductAttr();
        orderCartItem.setProductSkuId(orderItem.getProductSkuId());
        orderCartItem.setProductName(proProduct.getProductName());
        orderCartItem.setPrice(orderOrder.getRepayAmount());//已付定金
        orderCartItem.setSalePrice(proProduct.getSalePrice());//销售价格
        orderCartItem.setPreWaitpay(0);
//        orderCartItem.setMemberUsername();
        orderCartItem.setValidStatus(0);
        orderCartItem.setProductCategoryId(proProduct.getProductAttributeCategoryId());
        orderCartItem.setOrderSn(orderSn);
        orderCartItem.setType(1);
        orderCartItemDao.insert(orderCartItem);
    }

    /**从进货车删除预售*/
    public void delete(String orderSn){
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn));
        if(orderOrder == null ){
            throw new RRException("订单有误！");
        }
        String orderSn1 = orderOrder.getOrderSn();
        OrderItem orderItem = itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn",orderSn1));
        if(orderItem == null ){
            throw new RRException("订单有误！");
        }
        ProProduct proProduct = proProductDao.selectById(orderOrder.getProductId());
        if(proProduct == null ){
            throw new RRException("订单有误！");
        }
        OrderCartItem orderCartItem = orderCartItemDao.selectOne(new QueryWrapper<OrderCartItem>().
                eq("product_id", proProduct.getId()).
                eq("product_sku_id", orderItem.getProductSkuId()).eq("user_member_id", orderOrder.getUserMemberId()));
        if(orderCartItem == null ){
            throw new RRException("订单有误！");
        }
        orderCartItemDao.deleteById(orderCartItem);
    }

    /**支付减库存*/
    public void devide(String orderSn,String str) {

        List<OrderOrder> orderOrders = orderDao.selectList(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn));
        orderOrders.stream().forEach(order -> {
                    if ("yg".equals(str)) {
                        List<OrderItem> orderItems = itemDao.selectList(new QueryWrapper<OrderItem>().eq("order_sn", order.getOrderSn()));
                        orderItems.stream().forEach(item -> {
                            if (item != null) {
                                /*商品减库存*/
                                ExtProduct extProduct = extProductDao.selectOne(new QueryWrapper<ExtProduct>().
                                        eq("user_member_id", order.getUserMemberId()).eq("product_id", order.getProductId()));
                                    ExtProductSku extProductSku = extProductSkuDao.selectOne(new QueryWrapper<ExtProductSku>().
                                            eq("ext_product_id",extProduct.getId()).
                                            eq("product_id",order.getProductId()).
                                            eq("sku_stock_id",item.getProductSkuId()));
                                    Long stock = extProductSku.getStock();
                                    Long productQuantity = item.getProductQuantity().longValue();
                                    Integer cloudStock = extProduct.getCloudStock();
                                    cloudStock = cloudStock.intValue() - productQuantity.intValue();
                                    extProduct.setCloudStock(cloudStock);
                                    extProductDao.updateById(extProduct);
                                    extProductSku.setStock(stock - productQuantity);
                                    extProductSkuDao.updateById(extProductSku);
                            }
                        });
                    }else {
                        List<OrderItem> orderItems = itemDao.selectList(new QueryWrapper<OrderItem>().eq("order_sn", order.getOrderSn()));
                        orderItems.stream().forEach(item -> {
                            if (item != null) {
                                /*商品减库存*/
                                    ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                                    skuStock.setStock(skuStock.getStock() - item.getProductQuantity());
                                    skuStock.setSale(skuStock.getSale() + item.getProductQuantity());
                                    skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", item.getProductSkuId()));
                                }
                        });
                    }
                });
    }

    /**
     * 修改订单状态
     */
    public void updateStatus(String orderSn, String str) {
        List<OrderOrder> orderOrders = orderDao.selectList(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn));
        orderOrders.stream().forEach(orderOrder -> {
//            ys预售-yc云仓-sy付尾款的预售订单-yg代下单减云仓库存
            switch(str){
//                case "yg" :
//                    if (orderOrder.getStatus() == 0) {
//                        orderOrder.setStatus(2);
//                        orderDao.update(orderOrder, new QueryWrapper<OrderOrder>().eq("order_sn", orderOrder));
//                        log.info("修改订单状态成功");
//                    }
//                    break;
                case "ys" :
                    if(orderOrder.getStatus() == 0){
                        orderOrder.setPreStatus(1);
                        orderDao.update(orderOrder, new QueryWrapper<OrderOrder>().eq("order_sn", orderOrder.getOrderSn()));
                        log.info("修改订单状态成功");
                    }
                    break;
                case "sy" :
                    if(orderOrder.getStatus() == 0){
                        orderOrder.setStatus(2);
                        orderOrder.setPreStatus(2);
                        orderDao.update(orderOrder, new QueryWrapper<OrderOrder>().eq("order_sn", orderOrder.getOrderSn()));
                        log.info("修改订单状态成功");
                    }
                    break;
                default :
                    if (orderOrder.getStatus() == 0) {
                        orderOrder.setStatus(2);
                        orderDao.update(orderOrder, new QueryWrapper<OrderOrder>().eq("order_sn", orderOrder.getOrderSn()));
                        log.info("修改订单状态成功");
                    }
            }

        });
    }



}
