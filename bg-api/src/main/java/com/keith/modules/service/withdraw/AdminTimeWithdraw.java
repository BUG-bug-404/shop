package com.keith.modules.service.withdraw;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.keith.common.utils.ProducerUtil;
import com.keith.modules.dao.bank.BankAdminDao;
import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.productsettle.UserProductSettleDao;
import com.keith.modules.dao.productsettle.UserProductSettleDateilDao;
import com.keith.modules.dao.withdraw.UserAdminAccountDao;
import com.keith.modules.dao.withdraw.UserAdminWithdrawDao;
import com.keith.modules.dao.yf.UserAdminYfDao;
import com.keith.modules.entity.bank.BankAdmin;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.productsettle.UserProductSettle;
import com.keith.modules.entity.productsettle.UserProductSettleDateil;
import com.keith.modules.entity.withdraw.BankDTO;
import com.keith.modules.entity.withdraw.UserAdminAccount;
import com.keith.modules.entity.withdraw.UserAdminWithdraw;
import com.keith.modules.entity.yf.UserAdminYf;
import com.keith.modules.service.wx.WeiXinPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 */
@Component
@Slf4j
@Service
public class AdminTimeWithdraw {
    @Autowired
    private UserProductSettleDao userProductSettleDao;
    //普通消息的Producer 已经注册到了spring容器中，后面需要使用时可以 直接注入到其它类中
    @Autowired
    private ProducerUtil producer;
    @Autowired
    ProProductDao proProductDao;
    @Autowired
    BankAdminDao bankAdminDao;
    @Autowired
    WeiXinPayService weiXinPayService;
    @Autowired
    UserAdminWithdrawDao userAdminWithdrawDao;
    @Autowired
    UserAdminAccountDao userAdminAccountDao;
    @Autowired
    OrderOrderDao orderDao;
    @Autowired
    UserAdminYfDao userAdminYfDao;
    @Autowired
    private UserProductSettleDao productSettleDao;
    @Autowired
    private UserProductSettleDateilDao productSettleDateilDao;
    @Autowired
    private OrderItemDao itemDao;
    Map<String, Object> map = new HashMap<>();

    /**
     * 供应上商定时提现
     *
     * @throws WxPayException
     */
    @Scheduled(cron = "0 */15 * * * ? ")
    @Transactional
    public void list() throws WxPayException {
        List<UserProductSettle> list = userProductSettleDao.selectList(new QueryWrapper<UserProductSettle>());
        if (list != null) {
            for (UserProductSettle userProductSettle : list) {
                if (userProductSettle.getSetStatus() == 2) {
                    int date = proProductDao.date(userProductSettle.getProductId());
                    if (date > userProductSettle.getSetDate()) {
                        BankAdmin bankAdmin = bankAdminDao.selectOne(new QueryWrapper<BankAdmin>().eq("user_id", userProductSettle.getUserAdminId()));
                        if (bankAdmin != null) {


                            UserAdminAccount userAdminAccount = userAdminAccountDao.selectOne(new QueryWrapper<UserAdminAccount>().eq("", userProductSettle.getUserAdminId()));
                            if (userAdminAccount != null) {
                                if (userAdminAccount.getTotalMoeny().compareTo(userProductSettle.getAccount()) > -1) {

                                    BankDTO bankDTO = new BankDTO();
                                    bankDTO.setAmount(BaseWxPayRequest.yuanToFen(userProductSettle.getAccount().toString()));
                                    bankDTO.setBankCode(bankAdmin.getBankCode());
                                    bankDTO.setBankName(bankAdmin.getBankName());
                                    bankDTO.setEncBankNo(bankAdmin.getBankNo());
                                    bankDTO.setEncTrueName(bankAdmin.getTrueName());
                                    weiXinPayService.getBank(bankDTO);

                                    UserAdminWithdraw userAdminWithdraw = new UserAdminWithdraw();
                                    userAdminWithdraw.setPrice(userProductSettle.getAccount());
                                    userAdminWithdraw.setAcceptAccount(bankAdmin.getBankCode());
                                    userAdminWithdraw.setCreateTime(new Date());
                                    userAdminWithdraw.setUserAdminId(userProductSettle.getUserAdminId());
                                    userAdminWithdraw.setStyle(3);
                                    userAdminWithdraw.setStatus(1);
                                    userAdminWithdrawDao.insert(userAdminWithdraw);

                                    userAdminAccount.setBalanceMoney(userAdminAccount.getBalanceMoney().subtract(userProductSettle.getAccount()));
                                    userAdminAccount.setWithdrawalMoney(userAdminAccount.getWithdrawalMoney().subtract(userProductSettle.getAccount()));
                                    userAdminAccount.setTotalMoeny(userAdminAccount.getTotalMoeny().subtract(userProductSettle.getAccount()));
                                    userAdminAccount.setUpdate(new Date());
                                    userAdminAccountDao.updateById(userAdminAccount);

                                    userProductSettle.setSetStatus(2);
                                    userProductSettle.setSetTime(new Date());
                                    userProductSettleDao.updateById(userProductSettle);
                                }

                            }


                        }
                    }

                }
            }

        }

    }


    /**
     * 修改订单状态
     */
    @Scheduled(cron = "0 */15 * * * ? ")
    public void order() {
        List<OrderOrder> lists = orderDao.selectList(new QueryWrapper<OrderOrder>().eq("status", 3));
        if(lists!=null){
            for (OrderOrder order:lists){
                OrderItem item = itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", order.getOrderSn()));
                int date=orderDao.orderDate(order.getOrderSn());
                if(date>=14){
                    order.setStatus(5);
                    orderDao.updateById(order);
                    List<UserProductSettle> list = productSettleDao.selectList(new QueryWrapper<UserProductSettle>().eq("user_admin_id", order.getUserAdminId()));
                    ProProduct proProduct = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("", order.getProductId()));
                    UserAdminYf userAdminYf = userAdminYfDao.selectOne(new QueryWrapper<UserAdminYf>().eq("user_admin_id", order.getUserAdminId()));
                    if (userAdminYf == null) {
                        userAdminYf.setAdminId(order.getUserAdminId());
                        userAdminYf.setMoeny(order.getFreightAmount());
                        userAdminYf.setCreateTime(new Date());
                        userAdminYfDao.insert(userAdminYf);

                    } else {
                        userAdminYf.setAdminId(order.getUserAdminId());
                        userAdminYf.setMoeny(order.getFreightAmount());
                        userAdminYfDao.update(userAdminYf, new QueryWrapper<UserAdminYf>().eq("user_admin_id", order.getUserAdminId()));
                    }

                    if (list == null) {
                        UserProductSettle productSettle = new UserProductSettle();
                        productSettle.setCreateTime(new Date());
                        productSettle.setSetStatus(1);
                        productSettle.setAccount(order == null ? new BigDecimal("0") : order.getTotalAmount());
                        productSettle.setProductId(proProduct.getId());
                        productSettle.setUserAdminId(proProduct.getUserAdminId());
                        productSettle.setProductName(proProduct.getProductName());
                        productSettle.setProductSpce(item.getProductAttr());
                        productSettle.setProductImg(item.getProductPic());
                        productSettle.setProductSpceStatus(proProduct.getUnifyStatus());
                        productSettle.setSetDate(proProduct.getFinalPay());
                        productSettleDao.insert(productSettle);
                        UserProductSettleDateil dateil = new UserProductSettleDateil();
                        dateil.setCreateTime(new Date());
                        dateil.setSettleId(productSettle.getId());
                        dateil.setUserAdminId(proProduct.getUserAdminId());
                        dateil.setNum(item.getProductQuantity());
                        dateil.setPrice(item.getProductPrice());
                        dateil.setTotalPrice(item.getProductPrice().subtract(new BigDecimal(item.getProductQuantity())));
                        dateil.setSkuId(item.getProductSkuId());
                        productSettleDateilDao.insert(dateil);
                    } else if (list != null) {
                        for (UserProductSettle userProductSettle : list) {
                            if (userProductSettle.getSetStatus() == 1) {
                                UserProductSettle productSettle = new UserProductSettle();
                                productSettle.setCreateTime(new Date());
                                //productSettle.setSetStatus(1);
                                productSettle.setAccount(order == null ? new BigDecimal("0") : order.getTotalAmount());
                                productSettle.setProductId(proProduct.getId());
                                productSettle.setUserAdminId(proProduct.getUserAdminId());
                                productSettle.setProductName(proProduct.getProductName());
                                productSettle.setProductSpce(item.getProductAttr());
                                productSettle.setProductImg(item.getProductPic());
                                productSettle.setProductSpceStatus(proProduct.getUnifyStatus());
                                productSettle.setSetDate(proProduct.getFinalPay());
                                productSettleDao.insert(productSettle);
                                UserProductSettleDateil dateil = new UserProductSettleDateil();
                                dateil.setCreateTime(new Date());
                                dateil.setSettleId(productSettle.getId());
                                dateil.setUserAdminId(proProduct.getUserAdminId());
                                dateil.setNum(item.getProductQuantity());
                                dateil.setPrice(item.getProductPrice());
                                dateil.setTotalPrice(item.getProductPrice().subtract(new BigDecimal(item.getProductQuantity())));
                                dateil.setSkuId(item.getProductSkuId());
                                productSettleDateilDao.insert(dateil);
                            } else {

                                UserProductSettle productSettle = new UserProductSettle();
                                productSettle.setCreateTime(new Date());
                                // productSettle.setSetStatus(1);
                                productSettle.setAccount((order == null ? new BigDecimal("0") : order.getTotalAmount()).add(userProductSettle.getAccount()));
                                //productSettle.setProductId(proProduct.getId());
                                // productSettle.setUserAdminId(proProduct.getUserAdminId());
                                // productSettle.setProductName(proProduct.getProductName());
                                // productSettle.setProductSpce(item.getProductAttr());
                                //productSettle.setProductImg(item.getProductPic());
                                // productSettle.setProductSpceStatus(proProduct.getUnifyStatus());
                                //productSettle.setSetDate(proProduct.getFinalPay());
                                productSettleDao.update(productSettle, new QueryWrapper<UserProductSettle>().eq("product_id", userProductSettle.getProductId()));
                                UserProductSettleDateil dateil = new UserProductSettleDateil();
                                dateil.setCreateTime(new Date());
                                dateil.setSettleId(userProductSettle.getId());
                                dateil.setUserAdminId(proProduct.getUserAdminId());
                                dateil.setNum(item.getProductQuantity());
                                dateil.setPrice(item.getProductPrice());
                                dateil.setTotalPrice(item.getProductPrice().subtract(new BigDecimal(item.getProductQuantity())));
                                dateil.setSkuId(item.getProductSkuId());
                                productSettleDateilDao.insert(dateil);
                            }

                        }

                    }
                    map.put("msg", "订单已完成！");



                }

            }

        }


    }


}
