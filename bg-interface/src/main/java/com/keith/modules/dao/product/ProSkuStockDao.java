package com.keith.modules.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.product.ProSkuStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品sku库存表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 09:39:17
 */
@Mapper
@Repository
public interface ProSkuStockDao extends BaseMapper<ProSkuStock> {

    @Select("select sp1 from pro_sku_stock where product_id =#{productId}")
    List<String> selectSp1(@Param("productId") Long productId);

    @Select("select sp2 from pro_sku_stock where product_id =#{productId}")
    List<String> selectSp2(@Param("productId") Long productId);

}
