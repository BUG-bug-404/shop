package com.keith.modules.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.order.OrderCartItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 进货车表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:26:54
 */
@Mapper
@Repository
public interface OrderCartItemDao extends BaseMapper<OrderCartItem> {
	
}
