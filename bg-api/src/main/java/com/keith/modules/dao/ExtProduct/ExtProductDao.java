package com.keith.modules.dao.ExtProduct;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
/**
 * 铺货商品表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-05 14:52:58
 */
@Mapper
@Repository
public interface ExtProductDao extends BaseMapper<ExtProduct> {
	
}
