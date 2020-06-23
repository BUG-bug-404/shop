package com.keith.modules.dao.address;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.address.UserReceiveAddress;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 收货地址表
 * 
 * @author lijinxinag
 * @email @qq.com
 * @date 2020-06-02 20:08:46
 */
@Mapper
@Repository
public interface UserReceiveAddressDao extends BaseMapper<UserReceiveAddress> {
	
}
