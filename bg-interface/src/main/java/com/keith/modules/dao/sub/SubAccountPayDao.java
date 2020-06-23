package com.keith.modules.dao.sub;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.sub.SubAccountPay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 分账订单支付表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:11
 */
@Mapper
@Repository
public interface SubAccountPayDao extends BaseMapper<SubAccountPay> {
	
}
