package com.keith.modules.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.product.ProAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 商品图集
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 11:05:20
 */
@Mapper
@Repository
public interface ProAlbumDao extends BaseMapper<ProAlbum> {

    @Select("select pic from pro_album where product_id=#{productId} and cover_status=0 and pic_owner=1")
    ProAlbum selectCoverStatus(Long productId);
}
