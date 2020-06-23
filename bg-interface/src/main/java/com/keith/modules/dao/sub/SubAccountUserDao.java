package com.keith.modules.dao.sub;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.sub.SubAccountUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 添加分账方表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:10
 */
@Mapper
@Repository
public interface SubAccountUserDao extends BaseMapper<SubAccountUser> {
	
}
