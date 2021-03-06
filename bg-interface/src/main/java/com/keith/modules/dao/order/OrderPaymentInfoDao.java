package com.keith.modules.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.order.OrderPaymentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 支付信息表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-04 13:44:50
 */
@Mapper
@Repository
public interface OrderPaymentInfoDao extends BaseMapper<OrderPaymentInfo> {
	
}
