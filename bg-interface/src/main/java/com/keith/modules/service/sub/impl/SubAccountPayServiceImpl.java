package com.keith.modules.service.sub.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.profitsharing.ProfitSharingReceiverRequest;
import com.github.binarywang.wxpay.bean.profitsharing.ProfitSharingRequest;
import com.github.binarywang.wxpay.bean.profitsharing.Receiver;
import com.github.binarywang.wxpay.bean.profitsharing.ReceiverList;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.sub.SubAccountPayDao;
import com.keith.modules.dao.sub.SubAccountUserDao;
import com.keith.modules.dto.sub.SubAccountPayDTO;
import com.keith.modules.dto.sub.SubShopDTO;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.sub.SubAccountPay;
import com.keith.modules.entity.sub.SubAccountUser;
import com.keith.modules.service.sub.SubAccountPayService;
import com.keith.modules.service.wx.CommonUtils;
import com.keith.modules.service.wx.WxPay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("subAccountPayService")
public class SubAccountPayServiceImpl extends ServiceImpl<SubAccountPayDao, SubAccountPay> implements SubAccountPayService {
    @Autowired
    private SubAccountUserDao subAccountUserDao;
    @Autowired
    private SubAccountPayDao subAccountPayDao;
    @Autowired
    private OrderOrderDao orderOrderDao;

    JSONObject obj = new JSONObject();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SubAccountPay> page = this.page(
                new Query<SubAccountPay>().getPage(params),
                new QueryWrapper<SubAccountPay>()
        );

        return new PageUtils(page);
    }

    @Override
    public JSONObject createOrder_jsapi(SubAccountPayDTO subAccountPayDTO, HttpServletRequest request) throws WxPayException, IOException {
        List<OrderOrder> orderOrders = orderOrderDao.selectList(new QueryWrapper<OrderOrder>().eq("unify_order_sn", subAccountPayDTO.getOrderSn()).eq("status", 0));
        if (orderOrders.size()==0) {
            obj.put("code", 1);
            obj.put("msg", "用户订单信息有误！");
            return obj;
        }
        OrderOrder orderOrder = new OrderOrder();
        orderOrder.setPaymentTime(new Date());
        orderOrder.setPayType(2);
        orderOrderDao.update(orderOrder, new QueryWrapper<OrderOrder>().eq("unify_order_sn", subAccountPayDTO.getOrderSn()));
        //微信支付
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        WxPayService wxPayService = WxPay.createOrderJSAPI();
        orderRequest.setBody("小程序");
        orderRequest.setOutTradeNo(subAccountPayDTO.getOrderSn());
        orderRequest.setProfitSharing("Y");
        orderRequest.setOpenid(subAccountPayDTO.getUserOpenid());

        //15分钟过期
        Date date = new Date();
        date.setTime(date.getTime() + 15 * 60 * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String createdate = sdf.format(date);
        orderRequest.setTimeExpire(createdate);
        //元转成分

        BigDecimal totalAmount = orderOrders.stream().map(OrderOrder::getTotalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(totalAmount.toString()));
        orderRequest.setSpbillCreateIp(CommonUtils.getRealIp(request));
        Object order = wxPayService.createOrder(orderRequest);


        obj.put("order", order);

        log.info("" + order);
        return obj;
    }


    @Override
    public JSONObject testProfitSharing(HttpServletRequest request) throws WxPayException, IOException, InterruptedException {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayService wxPayService = WxPay.createOrderJSAPI();
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
        log.info("微信支付回调1---------------result" + result.toString());
        String orderId = result.getOutTradeNo();
        log.info("orderId++" + orderId);
        String tradeNo = result.getTransactionId();
        log.info("tradeNo++" + tradeNo);
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
        log.info("totalFee++" + totalFee);

        SubAccountPay pay = subAccountPayDao.selectOne(new QueryWrapper<SubAccountPay>().eq("order_sn", orderId));
        if (pay!=null){
            SubAccountUser user=subAccountUserDao.selectOne(new QueryWrapper<SubAccountUser>().eq("shop_id",pay.getShopId()));
            if(user!=null){
                Receiver receiver = new Receiver(user.getType(),
                        user.getAccount(),
                        user.getName(),
                        user.getRelationType(),
                        user.getCustomRelation());
                ProfitSharingReceiverRequest profitSharingReceiverRequest = ProfitSharingReceiverRequest
                        .newBuilder()
                        .receiver(receiver.toJSONString())
                        .build();
                this.log.info(wxPayService.getProfitSharingService().addReceiver(profitSharingReceiverRequest).toString());


                Thread.sleep(60000);


                String outRefundNo = "f" + 9 + System.currentTimeMillis();
                ReceiverList instance = ReceiverList.getInstance();
                instance.add(new Receiver(user.getType(),
                        user.getAccount(),
                        1,
                        "分账说明"));
                //30000002922019102310811092093
                ProfitSharingRequest sharingRequest = ProfitSharingRequest
                        .newBuilder()
                        .outOrderNo(outRefundNo)
                        .transactionId(tradeNo)
                        .receivers(instance.toJSONString())
                        .build();
                this.log.info(wxPayService.getProfitSharingService().profitSharing(sharingRequest).toString());

            }

        }



        return obj;
    }

    @Override
    public JSONObject addSubShop(SubShopDTO user) {
            SubAccountUser subAccountUser = new SubAccountUser();
            subAccountUser.setShopId(user.getShopId());
            subAccountUser.setType(user.getType());
            subAccountUser.setAccount(user.getAccount());
            subAccountUser.setCreateTime(new Date());
            subAccountUser.setCustomRelation(user.getCustomRelation());
            subAccountUser.setName(user.getName());
            subAccountUser.setRelationType(user.getRelationType());
            subAccountUserDao.insert(subAccountUser);
            obj.put("msg", "添加成功！");
        return obj;
    }

}
