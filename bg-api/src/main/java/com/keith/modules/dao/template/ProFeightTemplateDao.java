package com.keith.modules.dao.template;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.template.ProFeightTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 大运费模版表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:36
 */
@Mapper
@Repository
public interface ProFeightTemplateDao extends BaseMapper<ProFeightTemplate> {
	
}
