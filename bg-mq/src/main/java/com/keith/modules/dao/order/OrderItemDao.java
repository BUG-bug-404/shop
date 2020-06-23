package com.keith.modules.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 订单中所包含的商品
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 15:55:22
 */
@Mapper
@Repository
public interface OrderItemDao extends BaseMapper<OrderItem> {
	
}
