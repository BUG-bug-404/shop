package com.keith.modules.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.modules.dto.order.OrderDTO;
import com.keith.modules.entity.order.OrderOrder;

import java.util.Map;

/**
 * @author Lzy
 * @date 2020/6/17 16:18
 */
public interface PreOrderService extends IService<OrderOrder> {

    /**
     * 生成预售订单
     */

    Map<String ,Object>  createPreOrder(OrderDTO orderDTO,Long userMemberId);

    /**
     * 支付尾款*/
    Map<String ,Object>  payAmount(String orderSn,Long userMemberId);

    /*查询订单*/
    OrderOrder findByOrdersn(String orderSn,Long userMemberId);

    /*已支付预售订单列表*/


}
