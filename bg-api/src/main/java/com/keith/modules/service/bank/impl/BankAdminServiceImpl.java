package com.keith.modules.service.bank.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.bank.BankAdminDao;
import com.keith.modules.entity.bank.BankAdmin;
import com.keith.modules.service.bank.BankAdminService;


@Service("bankAdminService")
public class BankAdminServiceImpl extends ServiceImpl<BankAdminDao, BankAdmin> implements BankAdminService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BankAdmin> page = this.page(
                new Query<BankAdmin>().getPage(params),
                new QueryWrapper<BankAdmin>()
        );

        return new PageUtils(page);
    }

}
