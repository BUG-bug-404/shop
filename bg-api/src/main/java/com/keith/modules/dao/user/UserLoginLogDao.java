package com.keith.modules.dao.user;

import com.keith.modules.entity.user.UserLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户登录日志
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-01 16:00:09
 */
@Mapper
public interface UserLoginLogDao extends BaseMapper<UserLoginLog> {
	
}
