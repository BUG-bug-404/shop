package com.keith.modules.service.record;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.record.StoreRecord;

import java.util.Map;

/**
 * 分销记录表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:30
 */
public interface StoreRecordService extends IService<StoreRecord> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分销记录
     */
    void saveRecord(Long orderId);

    /**
     * 获取本周订单收益
     * @return
     */
    StoreRecord getMonth(Long userId);

    /**
     * 获取所有订单收益
     * @return
     */
    StoreRecord getAll(Long userId);
}

