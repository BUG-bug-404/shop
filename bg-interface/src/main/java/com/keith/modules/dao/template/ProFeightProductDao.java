package com.keith.modules.dao.template;

import com.keith.modules.entity.template.ProFeightProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:50
 */
@Mapper
@Repository
public interface ProFeightProductDao extends BaseMapper<ProFeightProduct> {
	
}
