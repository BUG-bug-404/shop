package com.keith.modules.dao.product;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.product.ProCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品分类表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-02 13:38:48
 */
@Mapper
@Repository
public interface ProCategoryDao extends BaseMapper<ProCategory> {
	
}
