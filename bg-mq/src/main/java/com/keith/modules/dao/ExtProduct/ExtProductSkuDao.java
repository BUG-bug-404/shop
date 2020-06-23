package com.keith.modules.dao.ExtProduct;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.ExtProduct.ExtProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 铺货商品跟的规格表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-05 16:49:32
 */
@Mapper
@Repository
public interface ExtProductSkuDao extends BaseMapper<ExtProductSku> {


}
