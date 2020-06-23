package com.keith.modules.service.bank;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.bank.Bank;

import java.util.Map;

/**
 * 银行卡表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 10:56:12
 */
public interface BankService extends IService<Bank> {

    PageUtils queryPage(Map<String, Object> params);
}

