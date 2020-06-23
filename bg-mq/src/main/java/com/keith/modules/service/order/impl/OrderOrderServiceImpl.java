package com.keith.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dao.ExtProduct.ExtProductDao;
import com.keith.modules.dao.ExtProduct.ExtProductSkuDao;
import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.entity.ExtProduct.ExtProductSku;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.service.order.OrderOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Service("orderOrderService")
public class OrderOrderServiceImpl extends ServiceImpl<OrderOrderDao, OrderOrder> implements OrderOrderService {
    @Autowired
    private OrderOrderDao orderDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OrderItemDao itemDao;
    @Autowired
    private ProSkuStockDao skuStockDao;
    @Autowired
            private ExtProductDao extProductDao;
    @Autowired
            private ExtProductSkuDao extProductSkuDao;


    Map<String, Object> map = new HashMap<>();

    @Override
    @Transactional
    public Map<String, Object> updateOrderStatus(String orderSn) {
        OrderOrder order = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("order_sn", orderSn));
        OrderItem item=itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", orderSn));

        if (order == null || item==null) {
            throw new RRException("订单信息有误！");
        }
        ProSkuStock  skuStock=skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id",item.getProductSkuId()));
        if (order.getStatus() == 0) {
            order.setStatus(7);
            orderDao.update(order, new QueryWrapper<OrderOrder>().eq("order_sn", orderSn));
            if (skuStock!=null){
                skuStock.setStock(skuStock.getStock()+item.getProductQuantity());
                skuStockDao.update(skuStock,new QueryWrapper<ProSkuStock>().eq("id",item.getProductSkuId()));
                int stock = Integer.parseInt(redisTemplate.opsForValue().get(item.getProductSkuId() + ""));
                redisTemplate.opsForValue().set(skuStock.getId() + "", skuStock.getStock() + "");
            }
        }
        return map;
    }


    /**
     * 拿自己的货
     * @param orderSn
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> updateGetStatus(String orderSn) {

        OrderOrder order = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("order_sn", orderSn));
        OrderItem item=itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", orderSn));
        ExtProduct extProduct = extProductDao.selectOne(new QueryWrapper<ExtProduct>().
                eq("product_id", order.getProductId()).eq("user_member_id", order.getUserMemberId()));
        if (order == null || extProduct == null) {
            throw new RRException("订单信息有误！");
        }
        ExtProductSku extProductSku = extProductSkuDao.selectOne(new QueryWrapper<ExtProductSku>().
                eq("ext_product_id", extProduct.getId()).eq("sku_stock_id", item.getProductSkuId()));
        if (order.getStatus() == 0) {
            order.setStatus(6);
            orderDao.update(order, new QueryWrapper<OrderOrder>().eq("order_sn", orderSn));
            if(extProductSku == null){
                extProductSku.setStock(extProductSku.getSkuStockId()+item.getProductQuantity());
                extProductSkuDao.updateById(extProductSku);
            }
        }
        return map;
    }

    /**
     * 付尾款状态
     * @param orderSn
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> updatePsyStatus(String orderSn) {

        OrderOrder order = orderDao.selectOne(new QueryWrapper<OrderOrder>().eq("order_sn", orderSn));
//        OrderItem item=itemDao.selectOne(new QueryWrapper<OrderItem>().eq("order_sn", orderSn));

        if (order == null) {
            throw new RRException("订单信息有误！");
        }
        if (order.getStatus() == 0) {
            order.setStatus(6);
            orderDao.update(order, new QueryWrapper<OrderOrder>().eq("order_sn", orderSn));
        }
        return map;
    }
}
