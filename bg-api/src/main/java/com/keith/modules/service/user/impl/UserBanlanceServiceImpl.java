package com.keith.modules.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.common.collect.Maps;
import com.keith.common.exception.RRException;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.user.UserAcountHistoryDao;
import com.keith.modules.dao.user.UserBanlanceDao;
import com.keith.modules.dao.user.UserTradeDao;
import com.keith.modules.dto.user.UserBanlanceDTO;
import com.keith.modules.entity.user.UserAcountHistory;
import com.keith.modules.entity.user.UserBanlance;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.entity.user.UserTrade;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProSkuStockService;
import com.keith.modules.service.user.UserBanlanceService;
import com.keith.modules.service.user.UserMemberService;
import com.keith.modules.service.wx.CommonUtils;
import com.keith.modules.service.wx.WxPay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("userBanlanceService")
public class UserBanlanceServiceImpl extends ServiceImpl<UserBanlanceDao, UserBanlance> implements UserBanlanceService {

//    @Autowired
//    private OrderItemService orderItemService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ProSkuStockService proSkuStockService;

    @Autowired
    private OrderOrderService orderOrderService;

    @Autowired
    private UserBanlanceDao banlanceDao;
    @Autowired
    private UserAcountHistoryDao acountHistoryDao;

    @Autowired
    private UserMemberService userMemberService;
    @Autowired
    private UserTradeDao tradeDao;

    Map<String, Object> map = Maps.newHashMap();
    JSONObject jsonObject = new JSONObject();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserBanlance> page = this.page(
                new Query<UserBanlance>().getPage(params),
                new QueryWrapper<UserBanlance>()
        );
        return new PageUtils(page);
    }


    @Transactional
    public Map<String, Object> recharge(BigDecimal money, Long userMemberId) {

        UserBanlance balance = banlanceDao.selectOne(new QueryWrapper<UserBanlance>().eq("user_member_id", userMemberId));
        UserAcountHistory accountHistory = new UserAcountHistory();
        accountHistory.setUserMemberId(userMemberId);
        accountHistory.setBalanceType(3);
        accountHistory.setCurrentBanlace(money);
        accountHistory.setCreateTime(new Date());
        if (balance != null) {
            accountHistory.setBalanceHistory(balance.getBanlance());
            acountHistoryDao.insert(accountHistory);
            balance.setBanlance(balance.getBanlance().add(money));
            balance.setUserMemberId(userMemberId);
            banlanceDao.update(balance, new QueryWrapper<UserBanlance>().eq("user_member_id", userMemberId));
            map.put("msg", "成功充值" + money + "元");
        } else {
            accountHistory.setBalanceHistory(new BigDecimal(0));
            acountHistoryDao.insert(accountHistory);
            balance.setBanlance(money);
            balance.setUserMemberId(userMemberId);
            banlanceDao.insert(balance);
            map.put("msg", "成功充值" + money + "元");
        }


        return map;
    }

    @Override
    public JSONObject rechargeUser(UserBanlanceDTO banlanceDTO, Long userMemberId, HttpServletRequest request) {
        String orderNo=getOrderIdByUUId();
        //充值生成订单
        addTrad(userMemberId, banlanceDTO.getMoney(), orderNo);
        try {

            //微信支付
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            WxPayService wxPayService = WxPay.wxPayService();
            orderRequest.setBody("充值");
            orderRequest.setOutTradeNo(orderNo);
           // orderRequest.setOpenid(weiXinPayDTO.getOpenId());

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

            //log.info("回调开始");
            // getWxpayNotifyInfo(request);


            jsonObject.put("data", order);
            jsonObject.put("code", 0);
            return jsonObject;


        } catch (Exception e) {
            log.error("微信支付失败！订单号：{},原因:{}", e.getMessage());
            // e.printStackTrace();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "支付失败，请稍后重试！" + e.getMessage());
            return jsonObject;
        }


    }

    @Override
    public String getWxpayNotifyInfo(HttpServletRequest request) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayService wxPayService = WxPay.wxZhiPayService();
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
                log.info("支付回调成功！开始处理自己的业务");
                UserTrade trade=tradeDao.selectOne(new QueryWrapper<UserTrade>().eq("flow_trade_no",orderId));

                Map<String, Object> map=recharge(trade.getTradeAmount(),trade.getUserId());
                log.info("map="+map);

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

    /**
     * 创建订单编号
     *
     * @return
     */
    public static String getOrderIdByUUId() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(date);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return time + String.format("%011d", hashCodeV);
    }

    /**
     * 创建订单
     *
     * @param userId
     * @param tradeAmount
     * @param flowTradeNo
     */
    public void addTrad(Long userId, Integer tradeAmount, String flowTradeNo) {
        UserTrade trade = new UserTrade();
        trade.setCreateDate(new Date());
        trade.setUserId(userId);
        trade.setUserMobile("");
        trade.setTradeAmount(new BigDecimal(tradeAmount));
        trade.setTradeDesc("用户充值");
        trade.setTradeStatus(1);
        trade.setFlowTradeNo(flowTradeNo);
        tradeDao.insert(trade);
    }


    /**
     * 更新金额到钱包
     */
    @Override
    public void updateBanlance(Long userId,BigDecimal bigDecimal){
        UserBanlance userBanlance = this.baseMapper.selectOne(new QueryWrapper<UserBanlance>().eq("user_member_id",userId));

        try {
            //如果不存用户钱包信息则插入，存在则进行钱包余额的修改
            if(userBanlance == null){
                userBanlance = new UserBanlance();
                userBanlance.setBanlance(bigDecimal);
                userBanlance.setUserMemberId(userId);
                UserMember user = userMemberService.getOne(new QueryWrapper<UserMember>().eq("id",userId));
                userBanlance.setPhone(user.getMobile());
                this.baseMapper.insert(userBanlance);
            }else{
                BigDecimal banlance = userBanlance.getBanlance();
                userBanlance.setBanlance(banlance.add(bigDecimal));
                this.updateById(userBanlance);
            }
        } catch (Exception e) {
            throw new RRException("更新钱包失败！",e);
        }
    }


    /**
     * 查询账户余额
     */
    @Override
    public Map<String,BigDecimal> getBanlance(Long userId){
        Map<String,BigDecimal> map = new HashMap<>();
        UserBanlance userBanlance = this.baseMapper.selectOne(new QueryWrapper<UserBanlance>().eq("user_member_id",userId));
       if(userBanlance==null){
           map.put("balance",new BigDecimal(0));
           return  map;
       }
        map.put("balance",userBanlance.getBanlance());
        return map;
    }


}
