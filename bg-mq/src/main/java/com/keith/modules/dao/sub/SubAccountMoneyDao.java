package com.keith.modules.dao.sub;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.sub.SubAccountMoney;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分账记录表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:10
 */
@Mapper
public interface SubAccountMoneyDao extends BaseMapper<SubAccountMoney> {
	
}
