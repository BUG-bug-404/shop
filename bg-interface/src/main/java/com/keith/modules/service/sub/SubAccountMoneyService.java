package com.keith.modules.service.sub;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.sub.SubAccountMoney;

import java.util.Map;

/**
 * 分账记录表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:10
 */
public interface SubAccountMoneyService extends IService<SubAccountMoney> {

    PageUtils queryPage(Map<String, Object> params);
}

