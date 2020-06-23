package com.keith.modules.dao.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.modules.entity.product.ProProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 09:39:17
 */
@Mapper
@Repository
public interface ProProductDao extends BaseMapper<ProProduct> {

    @Select("SELECT id,parent_id, product_attribute_category_id, product_name, platform_price, sale_price ,create_time FROM pro_product  WHERE parent_id IS NOT NULL AND delete_status = 0 \n" +
            "AND verify_status = 1 " +
            " AND publish_status = 1 ORDER BY create_time LIMIT ${page},${pageSize}")
    List<ProProduct> selectAll(@Param("page") int page, @Param("pageSize") int pageSize);

    /*
    ,Page<ProProduct> page
     */
    List<ProProduct> getProList(@Param(value = "lowPrice") BigDecimal lowPrice, @Param(value = "highPrice") BigDecimal highPrice,
                                @Param(value = "categoryId") Long categoryId, @Param(value = "sortType") Long sortType
    );

    /**
     * 通过全文检索查询商品
     *
     * @param page
     * @param productName
     * @return
     */

    IPage<ProProduct> selectByName(Page<ProProduct> page, @Param("productName") String productName);

    /**
     * 查询商品库存为0的商品
     *
     * @param page
     * @param userId
     * @return
     */
    IPage<ProProduct> selectByUserId(Page<ProProduct> page, @Param("userId") long userId);


    @Update("update pro_product set ext_count = ext_count+1 where id=#{productId}")
    int updateByProductId(long productId);


    @Select("SELECT DATEDIFF(SYSDATE(),create_time) AS DiffDate FROM pro_product where id=#{proId}")
    int date(@Param("") Long proId);

}
