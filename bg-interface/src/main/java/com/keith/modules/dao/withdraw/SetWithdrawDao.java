package com.keith.modules.dao.withdraw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.withdraw.SetWithdraw;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 提现设置
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 15:44:42
 */
@Mapper
@Repository
public interface SetWithdrawDao extends BaseMapper<SetWithdraw> {
	
}
