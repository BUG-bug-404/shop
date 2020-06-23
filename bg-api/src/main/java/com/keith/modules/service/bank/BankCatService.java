package com.keith.modules.service.bank;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.bank.BankCat;

import java.util.Map;

/**
 * 
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 17:09:51
 */
public interface BankCatService extends IService<BankCat> {

    PageUtils queryPage(Map<String, Object> params);
}

