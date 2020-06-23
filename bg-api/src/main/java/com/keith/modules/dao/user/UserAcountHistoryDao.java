package com.keith.modules.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.user.UserAcountHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账户历史表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-08 14:36:29
 */
@Mapper
public interface UserAcountHistoryDao extends BaseMapper<UserAcountHistory> {
	
}
