package com.keith.modules.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dto.order.FeightDTO;
import com.keith.modules.dto.order.MyOrderDTO;
import com.keith.modules.dto.order.OrderDTO;
import com.keith.modules.dto.order.OrderTrackDTO;
import com.keith.modules.entity.order.Order;
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

    PageUtils queryPage(MyOrderDTO orderDTO, Long userMemberId);

    /**
     * 订单中心
     * @param orderDTO
     * @param userMemberId
     * @return
     */
    List<Order> findAllOrder(MyOrderDTO orderDTO, Long userMemberId);

    /**
     * 订单详情
     * @param orderSn
     * @param userMemberId
     * @return
     */
    Order findByOne(String orderSn,Long userMemberId);

    /**
     * 创建订单
     * @param skuId
     * @param productId
     * @return
     */
    Map<String ,Object>  createOrder(List<OrderDTO> orderDTO, Integer type, Long userMemberId);

    /**
     * 计算运费
     * @param feightDTO
     * @param userMemberId
     * @return
     */
   Map<String,Object> feightMoney(FeightDTO feightDTO, Long userMemberId);

    /**
     * 订单跟踪
     * @param userMemberId
     * @return
     */
    Map<String,Object> orderTrack(OrderTrackDTO orderTrackDTO, Long userMemberId);

}

