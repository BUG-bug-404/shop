package com.keith.modules.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.order.OrderItem;

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
}

