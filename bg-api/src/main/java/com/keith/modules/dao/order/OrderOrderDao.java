package com.keith.modules.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.order.Order;
import com.keith.modules.entity.order.OrderOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
@Mapper
@Repository
public interface OrderOrderDao extends BaseMapper<OrderOrder> {

    /**
     * 订单中心
     * @param userMemberId
     * @param productName
     * @param receiveName
     * @param phone
     * @param detailTime
     * @param page
     * @param pageSize
     * @return
     */
    List<Order> selectAllOrder(@Param("userMemberId") Long userMemberId, @Param("productName")String productName,
                               @Param("receiveName")String receiveName, @Param("phone")String phone,@Param("detailTime")String detailTime,
                               @Param("status")Integer status,
                               @Param("page") int page, @Param("pageSize") int pageSize);

    /**
     * 订单跟踪
     * @param userMemberId
     * @param page
     * @param pageSize
     * @return
     */
    List<Order> selectAllOrderTrack(@Param("day") Integer day,@Param("userMemberId") Long userMemberId,@Param("status")Integer status,@Param("page") int page, @Param("pageSize") int pageSize);

    /**
     * 订单详情
     * @param userMemberId
     * @param orderSn
     * @return
     */
    Order selectByOrderSn(@Param("userMemberId") Long userMemberId,@Param("orderSn") String orderSn);

    /**
     * 查看是否满足升级A级店主
     * @param userMeberId
     * @return
     */
    List<Order> getCountForUser(@Param("userMemberId") Long userMeberId);


    @Select("SELECT DATEDIFF(SYSDATE(),payment_time) AS DiffDate FROM order_order WHERE status=3 AND order_sn=#{orderSn} ")
    int orderDate(@Param("orderSn")String orderSn);

    /**
     * 获取订单数
     * @param userAdminId
     * @return
     */
    OrderOrder getOrderNum(@Param("userAdminId") Long userAdminId);
}
