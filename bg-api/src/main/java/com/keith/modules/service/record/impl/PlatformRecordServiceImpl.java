package com.keith.modules.service.record.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.Query;
import com.keith.modules.dao.record.PlatformRecordDao;
import com.keith.modules.entity.record.PlatformRecord;
import com.keith.modules.service.record.PlatformRecordService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.keith.common.utils.PageUtils;



@Service("platformRecordService")
public class PlatformRecordServiceImpl extends ServiceImpl<PlatformRecordDao, PlatformRecord> implements PlatformRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PlatformRecord> page = this.page(
                new Query<PlatformRecord>().getPage(params),
                new QueryWrapper<PlatformRecord>()
        );

        return new PageUtils(page);
    }

}
