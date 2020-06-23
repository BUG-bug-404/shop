package com.keith.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.ProducerUtil;
import com.keith.modules.dao.address.UserReceiveAddressDao;
import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.dao.template.ProTemplateDao;
import com.keith.modules.dto.order.FeightDTO;
import com.keith.modules.dto.order.MyOrderDTO;
import com.keith.modules.dto.order.OrderDTO;
import com.keith.modules.dto.order.OrderTrackDTO;
import com.keith.modules.entity.address.UserReceiveAddress;
import com.keith.modules.entity.order.Order;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.template.ProTemplate;
import com.keith.modules.service.order.OrderOrderService;
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

@Slf4j
@Service("orderOrderService")
public class OrderOrderServiceImpl extends ServiceImpl<OrderOrderDao, OrderOrder> implements OrderOrderService {

    Map<String, Object> map = new HashMap<>();

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
    private StringRedisTemplate redisTemplate;
    //普通消息的Producer 已经注册到了spring容器中，后面需要使用时可以 直接注入到其它类中
    @Autowired
    private ProducerUtil producer;


    @Override
    public PageUtils queryPage(MyOrderDTO orderDTO, Long userMemberId) {
        IPage<OrderOrder> page = new Page<>(orderDTO.getPage(), orderDTO.getPageSize());
        page = this.page(page, new QueryWrapper<OrderOrder>().eq("user_member_id", userMemberId));

        return new PageUtils(page);
    }

    @Override
    public List<Order> findAllOrder(MyOrderDTO orderDTO, Long userMemberId) {
        int pa = (orderDTO.getPage() - 1) * orderDTO.getPageSize();

        List<Order> list = orderDao.selectAllOrder(userMemberId, orderDTO.getProductName(), orderDTO.getReceiveName(), orderDTO.getPhone(), orderDTO.getDetailTime(), pa, orderDTO.getPageSize());

        return list;
    }

    @Override
    public Order findByOne(String orderSn, Long userMemberId) {
        Order order = orderDao.selectByOrderSn(userMemberId, orderSn);

        return order;
    }

    private static Lock lock = new ReentrantLock(true);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createOrder(List<OrderDTO> orderDTOs,Integer type, Long userMemberId) {

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
                orderOrder.setThirdOrderSn(orderDTO.getThirdOrderSn());
                orderOrder.setReceiveAdress(orderDTO.getReceiveAddress());
                orderOrder.setReceivePhone(orderDTO.getReceivePhone());
                ProSkuStock skuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", orderDTO.getSkuId()));
//判断角色
                FeightDTO feightDTO = new FeightDTO();
                feightDTO.setAddressId(orderDTO.getAddressId().intValue());
                feightDTO.setNum(orderDTO.getNum());
                feightDTO.setProductId(orderDTO.getProductId().intValue());

                Map map = feightMoney(feightDTO,userMemberId);

            /*map=this.feightMoney(feightDTO,userMemberId);
            BigDecimal value=new BigDecimal(0);//运费
            for(String key : map.keySet()){
               value = (BigDecimal) map.get(key);
                System.out.println(key+"  "+value);
            }*/

                BigDecimal freightAmount =new BigDecimal(map.get("feightMoney").toString());

                //普通的用户购买
                orderOrder.setPayAmount(skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum())));
                orderOrder.setFreightAmount(freightAmount);
                orderOrder.setTotalAmount((skuStock.getPlatformSalePrice().multiply(new BigDecimal(orderDTO.getNum()))).add(freightAmount));

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
                itemDao.insert(orderItem);
                map.put("orderSn", orderOrder.getUnifyOrderSn());
                //map.put("msg", "创建订单成功！");

                //修改库存
                skuStock.setStock(skuStock.getStock() - orderDTO.getNum());
                skuStockDao.update(skuStock, new QueryWrapper<ProSkuStock>().eq("id", orderDTO.getSkuId()));

           /* JSONObject body = new JSONObject();
            body.put("orderNo", orderOrder.getOrderSn());
            body.put("notice", "定时/延时消息");

            producer.sendTimeMsg("time-01", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis() + 1000 * 60 * 15);*/
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

        List<ProTemplate> list = templateDao.selectList(new QueryWrapper<ProTemplate>().eq("user_admin_id", proProduct.getUserAdminId()));

        if (list == null) {
            throw new RRException("暂无运费信息无法配送");
        }
        UserReceiveAddress address = addressDao.selectOne(new QueryWrapper<UserReceiveAddress>().eq("user_member_id", userMemberId).eq("id", feightDTO.getAddressId()));
        if (list == null) {
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


}
