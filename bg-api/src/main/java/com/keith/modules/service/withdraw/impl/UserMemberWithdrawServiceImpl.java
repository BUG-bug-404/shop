package com.keith.modules.service.withdraw.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.withdraw.UserMemberWithdrawDao;
import com.keith.modules.entity.withdraw.UserMemberWithdraw;
import com.keith.modules.service.withdraw.UserMemberWithdrawService;


@Service("userMemberWithdrawService")
public class UserMemberWithdrawServiceImpl extends ServiceImpl<UserMemberWithdrawDao, UserMemberWithdraw> implements UserMemberWithdrawService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserMemberWithdraw> page = this.page(
                new Query<UserMemberWithdraw>().getPage(params),
                new QueryWrapper<UserMemberWithdraw>()
        );

        return new PageUtils(page);
    }

}
