package com.keith.modules.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dto.order.*;
import com.keith.modules.entity.order.Order;
import com.keith.modules.entity.order.OrderOrder;
import org.apache.ibatis.annotations.Param;

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


    Map<String,Object> findOrderByExt(Integer current, Integer size, Integer type, Integer date, Long userMemberId);

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
    Map<String ,Object>  createOrder(List<OrderDTO> orderDTO, Long userMemberId);

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

    /**
     * 订单完成
     * @param endOrderDTO
     * @param userMemberId
     * @return
     */
    Map<String,Object>  endOrder(EndOrderDTO endOrderDTO, Long userMemberId);


    /**
     * 查看是否满足升级A级店主
     * @param userMeberId
     * @return
     */
    List<Order> getCountForUser(@Param("userMemberId") Long userMeberId);

    /**
     * 获取订单数
     * @param userAdminId
     * @return
     */
    OrderOrder getOrderNum(@Param("userAdminId")Long userAdminId);


    /**
     * 创建购买会员等级订单
     * @param
     * @param
     * @return
     */
    Map<String ,Object>  createOrderLevelA(Long userMemberId);
    /**
     * 创建购买会员等级订单
     * @param
     * @param
     * @return
     */
    Map<String ,Object>  createOrderLevelB(Long userMemberId);
    /**
     * 创建购买会员等级订单
     * @param
     * @param
     * @return
     */
    Map<String ,Object>  createOrderLevelC(Long userMemberId);

}

