package com.keith.modules.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.user.UserLevel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店家等级表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-04 09:53:52
 */
@Mapper
public interface UserLevelDao extends BaseMapper<UserLevel> {
	
}
