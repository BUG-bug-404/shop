package com.keith.modules.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.order.OrderOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
@Mapper
public interface OrderOrderDao extends BaseMapper<OrderOrder> {

    OrderOrder getOrderNum(Long userAdminId);

}
