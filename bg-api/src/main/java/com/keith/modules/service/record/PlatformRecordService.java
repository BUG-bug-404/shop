package com.keith.modules.service.record;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.record.PlatformRecord;

import java.util.Map;

/**
 * 平台收入记录表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:31
 */
public interface PlatformRecordService extends IService<PlatformRecord> {

    PageUtils queryPage(Map<String, Object> params);
}

