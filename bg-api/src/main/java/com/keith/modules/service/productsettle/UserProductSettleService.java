package com.keith.modules.service.productsettle;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.productsettle.UserProductSettle;

import java.util.Map;

/**
 * 供应商，商品结算表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 18:03:34
 */
public interface UserProductSettleService extends IService<UserProductSettle> {

    PageUtils queryPage(Map<String, Object> params);
}

