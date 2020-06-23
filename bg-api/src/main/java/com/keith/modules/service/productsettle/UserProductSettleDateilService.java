package com.keith.modules.service.productsettle;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.productsettle.UserProductSettleDateil;

import java.util.Map;

/**
 * 商品结算详情表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 18:03:34
 */
public interface UserProductSettleDateilService extends IService<UserProductSettleDateil> {

    PageUtils queryPage(Map<String, Object> params);
}

