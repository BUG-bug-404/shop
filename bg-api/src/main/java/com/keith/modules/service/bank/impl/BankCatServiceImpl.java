package com.keith.modules.service.bank.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.bank.BankCatDao;
import com.keith.modules.entity.bank.BankCat;
import com.keith.modules.service.bank.BankCatService;


@Service("bankCatService")
public class BankCatServiceImpl extends ServiceImpl<BankCatDao, BankCat> implements BankCatService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BankCat> page = this.page(
                new Query<BankCat>().getPage(params),
                new QueryWrapper<BankCat>()
        );

        return new PageUtils(page);
    }

}
