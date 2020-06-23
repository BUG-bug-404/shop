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


    @Select("select eps.*,pss.sp2 as skuInfo from ext_product_sku eps left join pro_sku_stock pss on eps.sku_stock_id =pss.id  where eps.ext_product_id = #{id} ")
    List<ExtProductSku> findSkuByName(@Param("id")long id);
}
