package com.keith.modules.dao.record;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.record.PlatformRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台收入记录表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:31
 */
@Mapper
public interface PlatformRecordDao extends BaseMapper<PlatformRecord> {
	
}
