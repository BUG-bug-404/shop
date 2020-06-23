package com.keith.modules.dao.record;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.record.StoreRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分销记录表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:30
 */
@Mapper
public interface StoreRecordDao extends BaseMapper<StoreRecord> {
	StoreRecord getMonth(Long userId);
	StoreRecord getAll(Long userId);
}
