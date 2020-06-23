package com.keith.modules.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.user.UserCollectItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
/**
 * 收藏表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:41:56
 */
@Mapper
@Repository
public interface UserCollectItemDao extends BaseMapper<UserCollectItem> {
	
}
