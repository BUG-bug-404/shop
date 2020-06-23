package com.keith.modules.dao.product;

import com.keith.modules.entity.product.CloudManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
/**
 * 云仓SKU数据表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-04 19:25:40
 */
@Mapper
@Repository
public interface CloudManagementDao extends BaseMapper<CloudManagement> {
	
}
