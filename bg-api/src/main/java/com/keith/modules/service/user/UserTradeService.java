package com.keith.modules.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.user.UserTrade;

import java.util.Map;

/**
 * 充值记录 订单表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-09 14:40:04
 */
public interface UserTradeService extends IService<UserTrade> {

    PageUtils queryPage(Map<String, Object> params);
}

