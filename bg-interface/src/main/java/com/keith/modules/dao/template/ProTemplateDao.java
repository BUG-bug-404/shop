package com.keith.modules.dao.template;

import com.keith.modules.entity.template.ProTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 小运费模版
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:36
 */
@Mapper
@Repository
public interface ProTemplateDao extends BaseMapper<ProTemplate> {
	
}
