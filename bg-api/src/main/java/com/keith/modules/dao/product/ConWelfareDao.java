package com.keith.modules.dao.product;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.product.ConWelfare;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 福利商品
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-13 14:55:11
 */
@Mapper
@Repository
public interface ConWelfareDao extends BaseMapper<ConWelfare> {

	
}
