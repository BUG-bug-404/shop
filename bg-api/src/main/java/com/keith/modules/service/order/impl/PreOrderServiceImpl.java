package com.keith.modules.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.common.utils.ProducerUtil;
import com.keith.modules.dao.address.UserReceiveAddressDao;
import com.keith.modules.dao.order.OrderCartItemDao;
import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.product.ProPreviewDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.dao.productsettle.UserProductSettleDao;
import com.keith.modules.dao.productsettle.UserProductSettleDateilDao;
import com.keith.modules.dao.template.ProFeightProductDao;
import com.keith.modules.dao.template.ProTemplateDao;
import com.keith.modules.dao.yf.UserAdminYfDao;
import com.keith.modules.dto.order.FeightDTO;
import com.keith.modules.dto.order.OrderDTO;
import com.keith.modules.entity.address.UserReceiveAddress;
import com.keith.modules.entity.order.OrderCartItem;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.ProPreview;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.template.ProFeightProduct;
import com.keith.modules.entity.template.ProTemplate;
import com.keith.modules.service.order.PreOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Lzy
 * @date 2020/6/17 16:18
 */
@Slf4j
@Service("PreOrderServiceImpl")
public class PreOrderServiceImpl extends ServiceImpl<OrderOrderDao, OrderOrder> implements PreOrderService {

    Map<String, Object> map = new HashMap<>();
    private static Lock lock = new ReentrantLock(true);

    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private UserReceiveAddressDao addressDao;
    @Autowired
    private OrderItemDao itemDao;
    @Autowired
    private ProTemplateDao templateDao;
    @Autowired
    private OrderOrderDao orderDao;
    @Autowired
    private ProSkuStockDao skuStockDao;
    @Autowired
    private UserProductSettleDao productSettleDao;
    @Autowired
    private UserProductSettleDateilDao productSettleDateilDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    //普通消息的Producer 已经注册到了spring容器中，后面需要使用时可以 直接注入到其它类中
    @Autowired
    private ProducerUtil producer;
    @Autowired
    UserAdminYfDao userAdminYfDao;
    @Autowired
    private ProPreviewDao proPreviewDao;
    @Autowired
    private OrderCartItemDao orderCartItemDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private UserReceiveAddressDao userReceiveAddressDao;
    @Autowired
    private ProFeightProductDao proFeightProductDao;

    /**
     * 创建预售订单
     *
     * @param orderDTO
     * @param userMemberId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createPreOrder(OrderDTO orderDTO, Long userMemberId) {
        lock.lock();
        ProProduct productEntity = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("id", orderDTO.getProductId()).
                eq("publish_status", 1).eq("verify_status", 1).eq("delete_status", 0)
                .isNotNull("parent_id").eq("preview_status", 1));
        if (productEntity == null) {
            throw new RRException("商品信息有误！");
        }
        if (productEntity.getTotalNum() < 1) {
            throw new RRException("库存不足！");
        }
        int stock = Integer.parseInt(redisTemplate.opsForValue().get(orderDTO.getSkuId() + ""));
        if (stock < 0) {
            log.info("" + stock);
            throw new RRException("库存不足！");
        }

        if (stock >= orderDTO.getSkuId().intValue()) {
            int totalStock = stock - orderDTO.getNum().intValue();
            redisTemplate.opsForValue().set(orderDTO.getSkuId().intValue() + "", totalStock + "");
        }


        try {
            Date date = new Date();

            String orderSn = getOrderIdByUUId();

            //创建订单
            OrderOrder orderOrder = new OrderOrder();
            orderOrder.setProductId(orderDTO.getProductId());
            orderOrder.setOrderSn(orderSn);
            orderOrder.setUnifyOrderSn("ys" + orderSn);
            orderOrder.setUserAdminId(productEntity.getUserAdminId());
            orderOrder.setUserMemberId(userMemberId);
            orderOrder.setCreateTime(date);
            orderOrder.setStatus(0);
            orderOrder.setOrderType(1);
            /*收货人信息*/
            UserReceiveAddress userReceiveAddress = userReceiveAddressDao.selectById(orderDTO.getAddressId());
            orderOrder.setReceiveAdress(userReceiveAddress.getProvince()+"-"+userReceiveAddress.getCity()+"-"
                    +userReceiveAddress.getRegion()+"-"+userReceiveAddress.getDetailTime());
            orderOrder.setReceivePerson(userReceiveAddress.getReceiveName());
            orderOrder.setReceivePhone(userReceiveAddress.getPhone());
            orderOrder.setReceiveAddressId(orderDTO.getAddressId());

            ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", orderDTO.getSkuId()));
            //预售不判断角色
            FeightDTO feightDTO = new FeightDTO();
            feightDTO.setAddressId(orderDTO.getAddressId().intValue());
            feightDTO.setNum(orderDTO.getNum());
            feightDTO.setProductId(orderDTO.getProductId().intValue());

            Map maps = feightMoney(feightDTO, userMemberId);

            BigDecimal freightAmount = new BigDecimal("0");
            if(maps== null || maps.size()<= 0 ||maps.get("msg").toString().equals("msg")){
                freightAmount = new BigDecimal("0");

            }else{
                freightAmount = new BigDecimal(maps.get("feightMoney").toString());

            }



            /*计算定金*/
            ProPreview proPreview = proPreviewDao.selectOne(new QueryWrapper<ProPreview>().eq("product_id", productEntity.getId()));
            Integer style = proPreview.getStyle();
            /*0全款预售/1比例定金预售2/固定金定金预售*/
            if (style != null && style == 0) {
                /*全款预售需要付邮费*/
                orderOrder.setRepayAmount(skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum())));//定金
                orderOrder.setPayAmount(new BigDecimal("0"));
                orderOrder.setTotalAmount((skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum()))).add(freightAmount));
            } else if (style == 1) {
                BigDecimal bigDecimal1 = new BigDecimal("0.01");
                BigDecimal bigDecimal = bigDecimal1.multiply(new BigDecimal(proPreview.getPreRatio()));//比例
                BigDecimal multiply = bigDecimal.multiply(skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum())));//定金
                orderOrder.setRepayAmount(multiply);
                orderOrder.setPayAmount(new BigDecimal("0"));
//                orderOrder.setFreightAmount(orderDTO.getFreightAmount());
                orderOrder.setTotalAmount(multiply.add(freightAmount));
            } else if (style == 3) {
                BigDecimal preMoney = proPreview.getPreMoney();
                orderOrder.setRepayAmount(preMoney);
                orderOrder.setPayAmount(new BigDecimal("0"));
//                orderOrder.setFreightAmount(orderDTO.getFreightAmount());
                orderOrder.setTotalAmount(preMoney.add(freightAmount));
            }

            //会员购买
            this.save(orderOrder);

            //创建订单详情
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderOrder.getId());
            orderItem.setOrderSn(orderSn);
            orderItem.setCreateTime(date);
            orderItem.setProductId(orderDTO.getProductId());
            orderItem.setProductSkuId(orderDTO.getSkuId());
            orderItem.setUserAdminId(productEntity.getUserAdminId());
            orderItem.setProductName(productEntity.getProductName());
            ProSkuStock proSkuStock = skuStockDao.selectById(orderDTO.getSkuId());
            orderItem.setProductPrice(proSkuStock.getPlatformSalePrice());
            orderItem.setProductQuantity(orderDTO.getNum());
            itemDao.insert(orderItem);
            map.put("orderSn", orderOrder.getUnifyOrderSn());

            //修改库存
            skuStock.setStock(skuStock.getStock() - orderDTO.getNum());
            skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", orderDTO.getSkuId()));

            JSONObject body = new JSONObject();
            body.put("orderNo", orderOrder.getOrderSn());
            body.put("notice", "定时/延时消息");

            producer.sendTimeMsg("time-06", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis() + 1000 * 60 * 15);
            map.put("msg", "请在15分钟内完成支付，超时未付订单将自动取消");
            map.put("code", "0");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return map;
    }

    /**
     * 支付尾款
     *
     * @param orderSn
     * @param userMemberId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> payAmount(String orderSn, Long userMemberId) {
        lock.lock();
        Map<String, Object> map = payOff(orderSn);
        Integer stauts = (Integer) map.get("status");
        if (stauts == 0) {
            throw new RRException("未到支付尾款时间！");
        }
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().
                eq("unify_order_sn", orderSn).
                eq("user_member_id", userMemberId));

        Integer preStatus = orderOrder.getPreStatus();
        if (preStatus == null || preStatus == 2) {
            throw new RRException("该订单已支付！");
        }
        if (orderOrder == null) {
            throw new RRException("有误！");
        }

        try {
            /*计算---尾款*/
            String orderIdByUUId = getOrderIdByUUId();
            String newOrderSn = orderSn.replace("ys", "sy");
            orderOrder.setUnifyOrderSn(newOrderSn);
            orderOrder.setOrderSn(orderIdByUUId);
//            BigDecimal totalAmount = orderOrder.getTotalAmount();//已付钱，定金加尾款
            BigDecimal repayAmount = orderOrder.getRepayAmount();//已付定金
            BigDecimal freightAmount = orderOrder.getFreightAmount();//已付邮费

            BigDecimal bigDecimal = needPay(orderSn);//尾款
            orderOrder.setTotalAmount(bigDecimal);
            orderOrder.setPayAmount(bigDecimal.add(freightAmount).add(repayAmount));
            orderDao.updateById(orderOrder);

            this.map.put("orderSn", orderOrder.getUnifyOrderSn());

            JSONObject body = new JSONObject();
            body.put("orderNo", orderOrder.getOrderSn());
            body.put("notice", "定时/延时消息");

            producer.sendTimeMsg("time-07", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis() + 1000 * 60 * 15);
            this.map.put("msg", "请在15分钟内完成支付，超时未付订单将自动取消");
            this.map.put("code", "0");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.map;
    }

    /**
     * 查看订单详情
     *
     * @param orderSn
     * @param userMemberId
     * @return
     */
    @Override
    public OrderOrder findByOrdersn(String orderSn, Long userMemberId) {
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn).eq("user_member_id", userMemberId));
        if (orderOrder == null) {
            throw new RRException("订单不存在！");
        }
        Map<String, Object> map1 = OrderOff(orderSn);
        Integer status1 = (Integer) map1.get("status");
        if (status1 == 0) {
            orderOrder.setStatus(6);
            orderOrder.setPreStatus(2);
            orderDao.updateById(orderOrder);
            throw new RRException("订单已关闭！");
        }

        Integer status = orderOrder.getStatus();//2
        Integer preStatus = orderOrder.getPreStatus();//6

        if ((status != null && status == 6) || (preStatus != null && preStatus == 2)) {
            throw new RRException("订单已关闭！");
        } else {
//            OrderOrder order = new OrderOrder();
            Map<String, Object> map = payOff(orderSn);//true:已到支付时间
            /*0已到支付时间1未到支付时间*/
            Integer stauts = (Integer) map.get("status");
            orderOrder.setStartPayStatus(stauts);

            Date time = (Date) map.get("time");
            orderOrder.setStartTime(time.toString());

            BigDecimal bigDecimal = needPay(orderSn);
            orderOrder.setNeedPay(bigDecimal.toString());

            Map<String, Object> result = OrderOff(orderSn);
            Integer results = (Integer) result.get("status");
            orderOrder.setEndPayStatus(results);

            Date endTime = (Date) result.get("time");
            orderOrder.setEndTime(endTime.toString());
            List<OrderItem> orderItem = findOrderItem(orderSn, userMemberId);
            orderOrder.setOrderItems(orderItem);

            return orderOrder;

        }

    }

    /**
     * 判断是否过了支付时间
     *
     * @param orderSn
     * @return
     */
    public Map<String, Object> OrderOff(String orderSn) {
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn));
        Integer preStatus = orderOrder.getPreStatus();
//        if(preStatus != null && preStatus == 0 && orderOrder.getStatus() != null && orderOrder.getStatus() == 0){
        //已付定金,判断是否已过支付时间

        Long productId = orderOrder.getProductId();
        ProProduct proProduct = proProductDao.selectById(productId);
        ProPreview proPreview = proPreviewDao.selectOne(new QueryWrapper<ProPreview>().eq("product_id", productId));
        String defaultPayTime = proPreview.getDefaultPayTime();//支付定金后- 1天;1天,1小时 -支付尾款
        String str[] = defaultPayTime.split(";|\\,");
        String dayOff = str[0];//多少天开始支付尾款
        String day = str[1];
        String hour = str[2];
        int days = Integer.parseInt(dayOff) + Integer.parseInt(day);
        Date paymentTime = orderOrder.getPaymentTime();//定金支付的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(paymentTime);
        calendar.add(calendar.DATE, days);//把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.HOUR, Integer.parseInt(hour));//时
//            //进行时间格式化
//            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            String format = s.format(calendar.getTime());
        Date time = calendar.getTime();//支付截止时间
        /*判断*/
        Date nowTime = new Date();

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

//            Calendar begin = Calendar.getInstance();
//            begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(time);
        Map<String, Object> map = new HashMap<>();
//        map.put("status",0);
        //*0-已过1-未过*/
        map.put("time", time);
        if (date.before(end)) {
            map.put("status", 0);
//                return true;//已过支付时间
        } else {
            map.put("status", 1);
//                return false;
        }
        return map;
    }


    /**
     * 判断是否到了支付时间
     *
     * @param orderSn
     * @return
     */
    public Map<String, Object> payOff(String orderSn) {
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn));
        Integer preStatus = orderOrder.getPreStatus();
//        if(preStatus != null && preStatus == 0 && orderOrder.getStatus() != null && orderOrder.getStatus() == 0){
        //已付定金,判断是否已过支付时间

        Long productId = orderOrder.getProductId();
        ProProduct proProduct = proProductDao.selectById(productId);
        ProPreview proPreview = proPreviewDao.selectOne(new QueryWrapper<ProPreview>().eq("product_id", productId));
        String defaultPayTime = proPreview.getDefaultPayTime();//支付定金后- 1天;1天,1小时 -支付尾款
        String str[] = defaultPayTime.split(";|\\,");
        String dayOff = str[0];//多少天开始支付尾款
        String day = str[1];
        String hour = str[2];
        int days = Integer.parseInt(dayOff) + Integer.parseInt(day);
        Date paymentTime = orderOrder.getPaymentTime();//定金支付的时间

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(paymentTime);
        calendar.add(calendar.DATE, Integer.parseInt(dayOff));//把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.HOUR, Integer.parseInt(hour));//时
//            //进行时间格式化
//            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            String format = s.format(calendar.getTime());
        Date time = calendar.getTime();//支付开始时间
        /*判断*/
        Date nowTime = new Date();

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

//            Calendar begin = Calendar.getInstance();
//            begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(time);

        Map<String, Object> map = new HashMap<>();
//        map.put("status",0);
        map.put("time", time);
        if (date.after(end)) {
            map.put("status", 0);
//            return true;//已到支付时间
        } else {
            map.put("status", 1);
//            return false;
        }
        return map;
    }

    /**
     * 计算订单还需付多少尾款
     *
     * @param orderSn
     * @return
     */
    public BigDecimal needPay(String orderSn) {
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("unify_order_sn", orderSn));

        String orderSn1 = orderOrder.getOrderSn();

        OrderItem orderItems = orderItemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", orderSn1));
        Integer productQuantity = orderItems.getProductQuantity();
        Long productSkuId = orderItems.getProductSkuId();

        BigDecimal totalAmount = orderOrder.getTotalAmount();//已付钱，定金加尾款
        BigDecimal platformSalePrice = skuStockDao.selectById(productSkuId).getPlatformSalePrice();

        BigDecimal bigDecimal = new BigDecimal("0");

        bigDecimal = platformSalePrice.multiply(new BigDecimal(productQuantity));//没减去定金的金额

        BigDecimal needPay = new BigDecimal("0");

        needPay = bigDecimal.subtract(totalAmount);//尾款待支付

        return needPay;

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
     * 查询订单里面的商品信息
     *
     * @param
     * @param
     * @return
     */
    public List<OrderItem> findOrderItem(String unifyOrderSn, Long userMemberId) {
        /*一个商品一个订单*/
        OrderOrder orderOrder = orderDao.selectOne(new QueryWrapper<OrderOrder>().
                eq("unify_order_sn", unifyOrderSn).eq("user_member_id", userMemberId));
        List<OrderItem> orderItems = orderItemDao.selectList(new QueryWrapper<OrderItem>().
                eq("order_sn", orderOrder.getOrderSn()).eq("product_id", orderOrder.getProductId()));

        if (orderItems == null || orderItems.size() <= 0 || orderOrder == null) {
            throw new RRException("订单信息有误！");
        }
        List<OrderItem> list = new ArrayList<>();
        orderItems.stream().forEach(orderItem -> {
            list.add(orderItem);
        });
        return list;
    }


    /**
     * 算运费
     *
     * @param feightDTO
     * @param userMemberId
     * @return
     */
    public Map<String, Object> feightMoney(FeightDTO feightDTO, Long userMemberId) {

        ProProduct proProduct = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("id", feightDTO.getProductId()));
        ProFeightProduct proFeightProduct=proFeightProductDao.selectOne(new QueryWrapper<ProFeightProduct>().eq("product_id",proProduct.getParentId()));
        List<ProTemplate> list = templateDao.selectList(new QueryWrapper<ProTemplate>().eq("feight_template_id", proFeightProduct.getFeightId()));

        if (list == null) {
            throw new RRException("暂无运费信息无法配送");
        }
        UserReceiveAddress address = addressDao.selectOne(new QueryWrapper<UserReceiveAddress>().eq("user_member_id", userMemberId).eq("id", feightDTO.getAddressId()));
        if (address == null) {
            throw new RRException("地址信息有误");
        }
        for (ProTemplate template : list) {
            String[] key = template.getProvince().split(",");
            String she = "";
            for (int i = 0; i < key.length; i++) {
                if (address.getProvince().equals(key[i])) {
                    she = key[i];
                    break;
                }
            }


            if (she.equals(address.getProvince()) || template.getRegion().equals("true")) {//
                if (feightDTO.getNum() <= template.getFirstUnit().setScale(0, BigDecimal.ROUND_UP).intValue()) {//购买数量小于首数
                    map.put("feight", template.getFirstFee());
                    return map;
                }
                if (feightDTO.getNum() > template.getFirstUnit().intValue()) {//购买数量大于首数
                    int num = -(template.getFirstUnit().setScale(0, BigDecimal.ROUND_UP).intValue() - feightDTO.getNum());

                    int a = num / template.getContinueUnit().setScale(0, BigDecimal.ROUND_UP).intValue();
                    int b = num % template.getContinueUnit().setScale(0, BigDecimal.ROUND_UP).intValue();

                    if (b > 0) {
                        b = 1;
                        BigDecimal feight = template.getContinueFee().multiply(new BigDecimal((a + b))).add(template.getFirstFee());
                        map.put("feightMoney", feight);
                    } else {
                        BigDecimal feight = template.getContinueFee().multiply(new BigDecimal(a)).add(template.getFirstFee());
                        map.put("feightMoney", feight);
                    }
                }
            }


        }

        return map;
    }
}
