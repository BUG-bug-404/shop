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
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.collect.Maps;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.ProducerUtil;
import com.keith.common.utils.Query;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.sub.SubAccountMoneyDao;
import com.keith.modules.dao.sub.SubAccountPayDao;
import com.keith.modules.dao.sub.SubAccountUserDao;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.sub.SubAccountMoney;
import com.keith.modules.entity.sub.SubAccountPay;
import com.keith.modules.entity.sub.SubAccountUser;
import com.keith.modules.service.sub.SubAccountPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service("subAccountPayService")
public class SubAccountPayServiceImpl extends ServiceImpl<SubAccountPayDao, SubAccountPay> implements SubAccountPayService {
    @Autowired
    private SubAccountUserDao subAccountUserDao;
    @Autowired
    private SubAccountPayDao subAccountPayDao;
    @Autowired
    private SubAccountMoneyDao subAccountMoneyDao;

    Map<String, Object> map = Maps.newHashMap();

    @Override
    public Map<String, Object> subMoney(String orderSn) throws WxPayException {
        SubAccountPay pay = subAccountPayDao.selectOne(new QueryWrapper<SubAccountPay>().eq("order_sn", orderSn));
        if (pay != null) {
            SubAccountUser user = subAccountUserDao.selectOne(new QueryWrapper<SubAccountUser>().eq("shop_id", pay.getShopId()));
            if (user != null) {
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
                        .transactionId(orderSn)
                        .receivers(instance.toJSONString())
                        .build();
                SubAccountMoney subAccountMoney = new SubAccountMoney();
                subAccountMoney.setAccount(null);
                subAccountMoney.setAmount(null);
                subAccountMoney.setDescription(null);
                subAccountMoney.setType(null);
                subAccountMoney.setCreateTime(new Date());
                subAccountMoneyDao.insert(subAccountMoney);


                WxPayService wxPayService = new WxPayServiceImpl();
                log.info(wxPayService.getProfitSharingService().profitSharing(sharingRequest).toString());
            }

        }

        return map;
    }
}
