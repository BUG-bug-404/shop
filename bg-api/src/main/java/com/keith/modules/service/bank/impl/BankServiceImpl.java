package com.keith.modules.service.bank.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.bank.BankDao;
import com.keith.modules.entity.bank.Bank;
import com.keith.modules.service.bank.BankService;


@Service("bankService")
public class BankServiceImpl extends ServiceImpl<BankDao, Bank> implements BankService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Bank> page = this.page(
                new Query<Bank>().getPage(params),
                new QueryWrapper<Bank>()
        );

        return new PageUtils(page);
    }

}
