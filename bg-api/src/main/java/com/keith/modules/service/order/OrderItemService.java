package com.keith.modules.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.form.CountOrderPrice;

import java.util.List;
import java.util.Map;

/**
 * 订单中所包含的商品
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
public interface OrderItemService extends IService<OrderItem> {

    PageUtils queryPage(Map<String, Object> params);

    /*算钱---一个商品多个规格*/
    Map<String, Object> CountOrderPrice(List<CountOrderPrice> countOrderPriceList,Long userMemberId);

    /*List<List<CountOrderPrice>> countOrderPriceList*/
    Map<String, Object> countCartPrice(List<Long> id,Long userMemberId,Long addressId);

    /*计算商品单个邮费*/
    String countFeight(Long productId);
}

