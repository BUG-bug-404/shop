package com.keith.modules.service.withdraw.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.withdraw.UserAdminWithdrawDao;
import com.keith.modules.entity.withdraw.UserAdminWithdraw;
import com.keith.modules.service.withdraw.UserAdminWithdrawService;


@Service("userAdminWithdrawService")
public class UserAdminWithdrawServiceImpl extends ServiceImpl<UserAdminWithdrawDao, UserAdminWithdraw> implements UserAdminWithdrawService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserAdminWithdraw> page = this.page(
                new Query<UserAdminWithdraw>().getPage(params),
                new QueryWrapper<UserAdminWithdraw>()
        );

        return new PageUtils(page);
    }

}
