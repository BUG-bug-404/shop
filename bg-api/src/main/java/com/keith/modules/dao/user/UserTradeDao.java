package com.keith.modules.dao.user;

import com.keith.modules.entity.user.UserTrade;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充值记录 订单表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-09 14:40:04
 */
@Mapper
public interface UserTradeDao extends BaseMapper<UserTrade> {
	
}
