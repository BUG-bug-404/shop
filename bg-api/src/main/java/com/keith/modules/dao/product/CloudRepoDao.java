package com.keith.modules.dao.product;

import com.keith.modules.entity.product.CloudRepo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
/**
 * 回收申请表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-12 09:21:42
 */
@Mapper
@Repository
public interface CloudRepoDao extends BaseMapper<CloudRepo> {
	
}
