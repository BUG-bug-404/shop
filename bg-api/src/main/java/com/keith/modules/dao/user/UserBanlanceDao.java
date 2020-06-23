package com.keith.modules.dao.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.user.UserBanlance;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
/**
 * 用户余额表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-02 15:36:25
 */
@Mapper
@Repository
public interface UserBanlanceDao extends BaseMapper<UserBanlance> {
	
}
