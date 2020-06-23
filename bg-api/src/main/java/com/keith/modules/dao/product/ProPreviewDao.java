package com.keith.modules.dao.product;

import com.keith.modules.entity.product.ProPreview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
/**
 * 预售设置表
 * 
 * @author lzy
 * @email ********
 * @date 2020-06-17 15:01:55
 */
@Mapper
@Repository
public interface ProPreviewDao extends BaseMapper<ProPreview> {
	
}
