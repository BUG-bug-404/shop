package com.keith.modules.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.ProducerUtil;
import com.keith.modules.dao.ExtProduct.ExtProductDao;
import com.keith.modules.dao.address.UserReceiveAddressDao;
import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.product.ProAlbumDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.dao.productsettle.UserProductSettleDao;
import com.keith.modules.dao.productsettle.UserProductSettleDateilDao;
import com.keith.modules.dao.template.ProFeightProductDao;
import com.keith.modules.dao.template.ProFeightTemplateDao;
import com.keith.modules.dao.template.ProTemplateDao;
import com.keith.modules.dao.yf.UserAdminYfDao;
import com.keith.modules.dto.order.*;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.entity.address.UserReceiveAddress;
import com.keith.modules.entity.order.Order;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.ProAlbum;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.productsettle.UserProductSettle;
import com.keith.modules.entity.productsettle.UserProductSettleDateil;
import com.keith.modules.entity.template.ProFeightProduct;
import com.keith.modules.entity.template.ProFeightTemplate;
import com.keith.modules.entity.template.ProTemplate;
import com.keith.modules.entity.yf.UserAdminYf;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.record.StoreRecordService;
import com.keith.modules.service.user.UserLevelChangeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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
import java.util.stream.Collectors;

@Slf4j
@Service("orderOrderService")
public class OrderOrderServiceImpl extends ServiceImpl<OrderOrderDao, OrderOrder> implements OrderOrderService {

    Map<String, Object> map = new HashMap<>();

    @Autowired
    private ExtProductDao extProductDao;
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
    private StoreRecordService storeRecordService;
    @Autowired
    private UserLevelChangeService userLevelChangeService;
    @Autowired
    private UserReceiveAddressDao userReceiveAddressDao;
    @Autowired
    private ProAlbumDao proAlbumDao;
    @Autowired
    private ProFeightProductDao proFeightProductDao;
    @Autowired
    private ProFeightTemplateDao feightTemplateDao;


    @Override
    public PageUtils queryPage(MyOrderDTO orderDTO, Long userMemberId) {
        IPage<OrderOrder> page = new Page<>(orderDTO.getPage(), orderDTO.getPageSize());
        page = this.page(page, new QueryWrapper<OrderOrder>().eq("user_member_id", userMemberId));

        return new PageUtils(page);
    }

    @Override
    public List<Order> findAllOrder(MyOrderDTO orderDTO, Long userMemberId) {
        int pa = (orderDTO.getPage() - 1) * orderDTO.getPageSize();

        List<Order> list = orderDao.selectAllOrder(userMemberId, orderDTO.getProductName(), orderDTO.getReceiveName(), orderDTO.getPhone(), orderDTO.getDetailTime(), orderDTO.getStatus(),pa, orderDTO.getPageSize());
        list.stream().forEach(orderList->{
//            String orderSn = orderList.getOrderSn();
//            List<OrderItem> orderItems = itemDao.selectList(new QueryWrapper<OrderItem>().eq("order_sn", orderSn));
//            orderItems.stream().forEach(orderItem -> {
//                ProSkuStock proSkuStock = skuStockDao.selectById(orderItem.getProductSkuId());
//            });
            ProAlbum proAlbum = proAlbumDao.selectOne(new QueryWrapper<ProAlbum>().
                    eq("product_id", orderList.getProductId()).eq("cover_status", 0).eq("pic_owner", 1));
            orderList.setPic(proAlbum.getPic());
            ProSkuStock proSkuStock = skuStockDao.selectById(orderList.getProductSkuId());
            orderList.setSp2(proSkuStock.getSp2());
        });

        return list;
    }

    @Override
    public Order findByOne(String orderSn, Long userMemberId) {
        Order order = orderDao.selectByOrderSn(userMemberId, orderSn);

        return order;
    }

    @Override
    public Map<String,Object> findOrderByExt(Integer current, Integer size,Integer type, Integer date, Long userMemberId) {
        String startTime;
        String endTime;
        switch (date){
            case 1:startTime = getPastDate(0)+" 0:00:00";
                    endTime = getPastDate(0)+" 23:59:59";
                    break;
            case 3:
                startTime = getPastDate(3)+" 0:00:00";
                endTime = getPastDate(0)+" 23:59:59";
                break;
            case 7:
                startTime = getPastDate(7)+" 0:00:00";
                endTime = getPastDate(0)+" 23:59:59";
                break;
            default:throw new RRException("上传日期错误");
        }

        List<OrderOrder> orderOrders = orderDao.selectList(new LambdaQueryWrapper<OrderOrder>().isNotNull(OrderOrder::getThirdOrderSn).eq(OrderOrder::getUserMemberId,userMemberId));

        Integer goumai = 0;
        Integer fahuo  = 0;
        Integer weifa  = 0;

        if (orderOrders.size()>0){
            Map<Integer, List<OrderOrder>> singleMap = orderOrders.stream().collect(Collectors.groupingBy(OrderOrder::getStatus));
             goumai = singleMap.get(5).size();
             fahuo = singleMap.get(3).size();
             weifa = singleMap.get(2).size();
        }


        if (size ==null||size==0){
            size = 10;
        }
        Page page = new Page(current,size);

        IPage<OrderOrder> orderPage = orderDao.selectPage(page,new LambdaQueryWrapper<OrderOrder>().eq(OrderOrder::getStatus,type==0?2:3).eq(OrderOrder::getUserMemberId,userMemberId).ge(OrderOrder::getCreateTime,startTime).le(OrderOrder::getCreateTime,endTime));

        List<Order> list = new LinkedList<>();
        if (orderPage.getRecords().size()>0){
            orderPage.getRecords().stream().forEach(t->{
                ExtProduct p = extProductDao.selectOne(new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getProductId,t.getProductId()).eq(ExtProduct::getUserMemberId,userMemberId));
                Order order = new Order();
                order.setProductName(p.getProductName());
                order.setProductPic(Arrays.asList(p.getPic()).get(0).replace("]","").replace("[",""));
                order.setOrderSn(t.getOrderSn());
                order.setDeliverySn(t.getDeliverySn());
                order.setTotalAmount(orderPage.getRecords().stream().map(OrderOrder::getTotalAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
                order.setFreightAmount(t.getFreightAmount());
                order.setProductAmount(t.getPayAmount());
                order.setStatus(t.getStatus());
                list.add(order);
            });
        }
        Map<String,Object> map = new HashMap<>();
        map.put("fahuo",fahuo);
        map.put("goumai",goumai);
        map.put("weifa",weifa);
        page.setRecords(list);
        page.setTotal(orderPage.getTotal());
        page.setPages(orderPage.getPages());
        map.put("page",page);

        return map;
    }


    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    private static Lock lock = new ReentrantLock(true);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createOrder(List<OrderDTO> orderDTOs, Long userMemberId) {

        String unifyOrderSn = getOrderIdByUUId();

        orderDTOs.stream().forEach(orderDTO -> {
            lock.lock();
            ProProduct productEntity = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("id", orderDTO.getProductId()));
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
            //ProSkuStock skuStock=skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id",orderDTO.getSkuId()));
            //UserReceiveAddress address= addressDao.selectOne(new QueryWrapper<UserReceiveAddress>().eq("",orderDTO.getAddressId()));

            try {
                Date date = new Date();

                String orderSn = getOrderIdByUUId();
                //创建订单
                OrderOrder orderOrder = new OrderOrder();
                orderOrder.setProductId(orderDTO.getProductId());
                orderOrder.setOrderSn(orderSn);
                orderOrder.setUnifyOrderSn(unifyOrderSn);
                orderOrder.setUserAdminId(productEntity.getUserAdminId());
                orderOrder.setUserMemberId(userMemberId);
                orderOrder.setCreateTime(date);
                orderOrder.setStatus(0);
                orderOrder.setOrderType(orderDTO.getIsCloud());
                orderOrder.setOrderType(productEntity.getPreviewStatus());
                orderOrder.setReceiveAddressId(orderDTO.getAddressId());

                /*收货人信息*/
                UserReceiveAddress userReceiveAddress = userReceiveAddressDao.selectById(orderDTO.getAddressId());
                orderOrder.setReceiveAdress(userReceiveAddress.getProvince() + "-" + userReceiveAddress.getCity() + "-"
                        + userReceiveAddress.getRegion() + "-" + userReceiveAddress.getDetailTime());
                orderOrder.setReceivePerson(userReceiveAddress.getReceiveName());
                orderOrder.setReceivePhone(userReceiveAddress.getPhone());
                ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", orderDTO.getSkuId()));
//判断角色
                FeightDTO feightDTO = new FeightDTO();
                feightDTO.setAddressId(orderDTO.getAddressId().intValue());
                feightDTO.setNum(orderDTO.getNum());
                feightDTO.setProductId(orderDTO.getProductId().intValue());

                Map<String,Object> maps = feightMoney(feightDTO, userMemberId);

            /*map=this.feightMoney(feightDTO,userMemberId);
            BigDecimal value=new BigDecimal(0);//运费
            for(String key : map.keySet()){
               value = (BigDecimal) map.get(key);
                System.out.println(key+"  "+value);
            }*/


                //普通的用户购买
                orderOrder.setPayAmount(skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum())));

                if(maps== null || maps.size()<= 0 ||maps.get("msg").toString().equals("msg")){
                    BigDecimal freightAmount = new BigDecimal("0");
                    orderOrder.setFreightAmount(freightAmount);
                    orderOrder.setTotalAmount((skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum()))).add(freightAmount));
                }else{
                    BigDecimal freightAmount = new BigDecimal(maps.get("feightMoney")==null?"0":maps.get("feightMoney").toString());
                    orderOrder.setFreightAmount(freightAmount);
                    orderOrder.setTotalAmount((skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum()))).add(freightAmount));
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
                orderItem.setProductQuantity(orderDTO.getNum());
                ProSkuStock proSkuStock = skuStockDao.selectById(orderDTO.getSkuId());
                orderItem.setProductPrice(proSkuStock.getPlatformSalePrice());
                itemDao.insert(orderItem);
                map.put("orderSn", orderOrder.getUnifyOrderSn());
                //map.put("msg", "创建订单成功！");

                //修改库存
                skuStock.setStock(skuStock.getStock() - orderDTO.getNum());
                skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", orderDTO.getSkuId()));

                JSONObject body = new JSONObject();
                body.put("orderNo", orderOrder.getOrderSn());
                body.put("notice", "定时/延时消息");

                producer.sendTimeMsg("time-01", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis() + 1000 * 60 * 15);
                map.put("msg", "请在15分钟内完成支付，超时未付订单将自动取消");
                map.put("code", "0");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        return map;
    }



    @Override
    public Map<String, Object> feightMoney(FeightDTO feightDTO, Long userMemberId) {

        ProProduct proProduct = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("id", feightDTO.getProductId()));
        ProFeightProduct proFeightProduct = proFeightProductDao.selectOne(new QueryWrapper<ProFeightProduct>().eq("product_id", proProduct.getParentId()));
        //ProTemplate template=templateDao.selectOne(new QueryWrapper<ProTemplate>().eq("user_admin_id", proProduct.getUserAdminId()));
        ProFeightTemplate feightTemplate = feightTemplateDao.selectOne(new QueryWrapper<ProFeightTemplate>().eq("feight_template_id", proFeightProduct.getFeightId()));

        if (feightTemplate != null) {
            if (feightTemplate.getTemTypt() == 1) {
                map.put("feightMoney", 0);
            }

        } else {

            List<ProTemplate> list = templateDao.selectList(new QueryWrapper<ProTemplate>().eq("feight_template_id", proFeightProduct.getFeightId()));

            if (list == null || list.size() <= 0) {
                throw new RRException("暂无运费信息无法配送");
            }
            UserReceiveAddress address = addressDao.selectOne(new QueryWrapper<UserReceiveAddress>().eq("user_member_id", userMemberId).eq("id", feightDTO.getAddressId()));
            if (address == null) {
                throw new RRException("地址信息有误");
            }
            for (ProTemplate template : list) {

                if (template.getProvince() != null && !template.getRegion().equals("true")) {
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
                            map.put("feightMoney", template.getFirstFee());
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
                                return map;
                            } else {
                                BigDecimal feight = template.getContinueFee().multiply(new BigDecimal(a)).add(template.getFirstFee());
                                map.put("feightMoney", feight);
                                return map;
                            }
                        }
                    } else {
                        map.put("msg", "msg");
                    }
                }
            }

        }

        return map;
    }

    @Override
    public Map<String, Object> orderTrack(OrderTrackDTO orderTrackDTO, Long userMemberId) {
        int pa = (orderTrackDTO.getPage() - 1) * orderTrackDTO.getPageSize();
        List<Order> list = orderDao.selectAllOrderTrack(orderTrackDTO.getTime(), userMemberId, orderTrackDTO.getStatus(), pa, orderTrackDTO.getPageSize());
        int total = 0;
        int dai = 0;
        for (Order order : list) {
            if (order.getTotal() == null) {
                total = total + 0;
            } else {
                total = total + order.getTotal();
            }

            if (order.getDai() == null) {
                dai = dai + 0;
            } else {
                dai = dai + order.getDai();
            }
        }
        map.put("list", list);
        map.put("totalOrder", total);
        map.put("daiOrder", dai);
        map.put("weiOrder", total - dai);


        return map;
    }


    @Override
    @Transactional
    public Map<String, Object> endOrder(EndOrderDTO endOrderDTO, Long userMemberId) {
        OrderOrder order = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("order_sn", endOrderDTO.getOrderSn()));
        OrderItem item = itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", endOrderDTO.getOrderSn()));
        if (order == null || item == null) {
            throw new RRException("订单有误！");
        }
        order.setStatus(5);
        orderDao.update(order, new QueryWrapper<OrderOrder>().eq("order_sn", endOrderDTO.getOrderSn()));

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
            //统计订单所属人员总额
            userLevelChangeService.updateLevelByOrder(order.getId());
            //计算该订单分佣保存到分销记录表
            storeRecordService.saveRecord(order.getId());


        }


        map.put("msg", "订单已完成！");
        return map;
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
     * 查看是否满足升级A级店主
     *
     * @param userMeberId
     * @return
     */
    @Override
    public List<Order> getCountForUser(@Param("userMemberId") Long userMeberId) {
        List<Order> list = orderDao.getCountForUser(userMeberId);
        return list;
    }

    /**
     * 获取订单数
     * @param userAdminId
     * @return
     */
    @Override
    public OrderOrder getOrderNum(@Param("userAdminId") Long userAdminId){
        return orderDao.getOrderNum(userAdminId);
    }


    /**
     * 创建购买会员等级订单A
     * @param
     * @param
     * @return
     */
    @Override
    public Map<String ,Object>  createOrderLevelA(Long userMemberId){
        String unifyOrderSn = getOrderIdByUUId();
        lock.lock();
        try {
            Date date = new Date();
            String orderSn = getOrderIdByUUId();
            //创建订单
            OrderOrder orderOrder = new OrderOrder();
//            orderOrder.setProductId(orderDTO.getProductId());
            orderOrder.setOrderSn(orderSn);
            orderOrder.setUnifyOrderSn(unifyOrderSn);
//            orderOrder.setUserAdminId(userMemberId);
            orderOrder.setUserMemberId(userMemberId);
            orderOrder.setCreateTime(date);
            orderOrder.setStatus(0);
//            orderOrder.setOrderType(orderDTO.getIsCloud());
            orderOrder.setOrderType(0);
            orderOrder.setNote("用户等级提升自购");
            orderOrder.setTotalAmount(new BigDecimal(199));
            orderOrder.setPayAmount(new BigDecimal(199));
//            orderOrder.setReceiveAddressId(orderDTO.getAddressId());


            this.save(orderOrder);

            //创建订单详情
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderOrder.getId());
            orderItem.setOrderSn(orderSn);
            orderItem.setCreateTime(date);
//            orderItem.setUserAdminId(productEntity.getUserAdminId());
            itemDao.insert(orderItem);
            map.put("orderSn", orderOrder.getUnifyOrderSn());
            //map.put("msg", "创建订单成功！");


            JSONObject body = new JSONObject();
            body.put("orderNo", orderOrder.getOrderSn());
            body.put("notice", "定时/延时消息");

            producer.sendTimeMsg(" time-08", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis() + 1000 * 60 * 15);
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
     * 创建购买会员等级订单B
     * @param
     * @param
     * @return
     */
    @Override
    public Map<String ,Object>  createOrderLevelB(Long userMemberId){
        String unifyOrderSn = getOrderIdByUUId();
        lock.lock();
        try {
            Date date = new Date();
            String orderSn = getOrderIdByUUId();
            //创建订单
            OrderOrder orderOrder = new OrderOrder();
//            orderOrder.setProductId(orderDTO.getProductId());
            orderOrder.setOrderSn(orderSn);
            orderOrder.setUnifyOrderSn(unifyOrderSn);
//            orderOrder.setUserAdminId(userMemberId);
            orderOrder.setUserMemberId(userMemberId);
            orderOrder.setCreateTime(date);
            orderOrder.setStatus(0);
//            orderOrder.setOrderType(orderDTO.getIsCloud());
            orderOrder.setOrderType(0);
            orderOrder.setNote("用户等级提升自购");
            orderOrder.setTotalAmount(new BigDecimal(388));
            orderOrder.setPayAmount(new BigDecimal(388));
//            orderOrder.setReceiveAddressId(orderDTO.getAddressId());


            this.save(orderOrder);

            //创建订单详情
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderOrder.getId());
            orderItem.setOrderSn(orderSn);
            orderItem.setCreateTime(date);
//            orderItem.setUserAdminId(productEntity.getUserAdminId());
            itemDao.insert(orderItem);
            map.put("orderSn", orderOrder.getUnifyOrderSn());
            //map.put("msg", "创建订单成功！");


            JSONObject body = new JSONObject();
            body.put("orderNo", orderOrder.getOrderSn());
            body.put("notice", "定时/延时消息");

            producer.sendTimeMsg("time-09", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis() + 1000 * 60 * 15);
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
     * 创建购买会员等级订单C
     * @param
     * @param
     * @return
     */
    @Override
    public Map<String ,Object>  createOrderLevelC(Long userMemberId){
        String unifyOrderSn = getOrderIdByUUId();
        lock.lock();
        try {
            Date date = new Date();
            String orderSn = getOrderIdByUUId();
            //创建订单
            OrderOrder orderOrder = new OrderOrder();
//            orderOrder.setProductId(orderDTO.getProductId());
            orderOrder.setOrderSn(orderSn);
            orderOrder.setUnifyOrderSn(unifyOrderSn);
//            orderOrder.setUserAdminId(userMemberId);
            orderOrder.setUserMemberId(userMemberId);
            orderOrder.setCreateTime(date);
            orderOrder.setStatus(0);
//            orderOrder.setOrderType(orderDTO.getIsCloud());
            orderOrder.setOrderType(0);
            orderOrder.setNote("用户等级提升自购");
            orderOrder.setTotalAmount(new BigDecimal(588));
            orderOrder.setPayAmount(new BigDecimal(588));
//            orderOrder.setReceiveAddressId(orderDTO.getAddressId());


            this.save(orderOrder);

            //创建订单详情
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderOrder.getId());
            orderItem.setOrderSn(orderSn);
            orderItem.setCreateTime(date);
//            orderItem.setUserAdminId(productEntity.getUserAdminId());
            itemDao.insert(orderItem);
            map.put("orderSn", orderOrder.getUnifyOrderSn());
            //map.put("msg", "创建订单成功！");


            JSONObject body = new JSONObject();
            body.put("orderNo", orderOrder.getOrderSn());
            body.put("notice", "定时/延时消息");

            producer.sendTimeMsg("time-10", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis() + 1000 * 60 * 15);
            map.put("msg", "请在15分钟内完成支付，超时未付订单将自动取消");
            map.put("code", "0");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return map;
    }

}
