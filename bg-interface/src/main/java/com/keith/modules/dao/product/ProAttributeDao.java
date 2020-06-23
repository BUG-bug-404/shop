package com.keith.modules.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.product.ProAttribute;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 14:05:23
 */
@Mapper
public interface ProAttributeDao extends BaseMapper<ProAttribute> {
	
}
