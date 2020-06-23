package com.keith.modules.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.order.OrderOrder;

import java.util.List;
import java.util.Map;

/**
 * 订单表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
public interface OrderOrderService extends IService<OrderOrder> {

    /**
     * 修改订单状态
     * @param orderSn
     * @return
     */
    Map<String ,Object> updateOrderStatus(String orderSn);

    /**
     * 修改getGoods订单状态，库存
     */

    Map<String ,Object> updateGetStatus(String orderSn);

    /**
     * 修改付尾款订单装
     */
    Map<String ,Object> updatePsyStatus(String orderSn);





}

