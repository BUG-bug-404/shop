package com.keith.modules.service.withdraw.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.withdraw.UserAdminAccountDao;
import com.keith.modules.entity.withdraw.UserAdminAccount;
import com.keith.modules.service.withdraw.UserAdminAccountService;


@Service("userAdminAccountService")
public class UserAdminAccountServiceImpl extends ServiceImpl<UserAdminAccountDao, UserAdminAccount> implements UserAdminAccountService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserAdminAccount> page = this.page(
                new Query<UserAdminAccount>().getPage(params),
                new QueryWrapper<UserAdminAccount>()
        );

        return new PageUtils(page);
    }

}
