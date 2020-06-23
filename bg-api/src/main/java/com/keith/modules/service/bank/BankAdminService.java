package com.keith.modules.service.bank;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.bank.BankAdmin;

import java.util.Map;

/**
 * 银行卡表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-18 11:25:22
 */
public interface BankAdminService extends IService<BankAdmin> {

    PageUtils queryPage(Map<String, Object> params);
}

