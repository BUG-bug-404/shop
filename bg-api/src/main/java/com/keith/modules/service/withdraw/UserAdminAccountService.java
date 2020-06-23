package com.keith.modules.service.withdraw;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.withdraw.UserAdminAccount;

import java.util.Map;

/**
 * 供应商账户表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-18 11:21:07
 */
public interface UserAdminAccountService extends IService<UserAdminAccount> {

    PageUtils queryPage(Map<String, Object> params);
}

