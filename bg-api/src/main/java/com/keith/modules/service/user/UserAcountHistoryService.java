package com.keith.modules.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dto.PageDTO;
import com.keith.modules.entity.user.UserAcountHistory;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 用户账户历史表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-08 14:36:29
 */
public interface UserAcountHistoryService extends IService<UserAcountHistory> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 计算该订单的收益佣金保存到数据库
     */
    void orderEarnings(Long orderId);

    /**
     * 分页获取
     * @return
     */
    PageUtils getPage(PageDTO pageDTO,Long userId);
}

