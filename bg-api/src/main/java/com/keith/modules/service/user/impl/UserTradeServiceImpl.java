package com.keith.modules.service.user.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.user.UserTradeDao;
import com.keith.modules.entity.user.UserTrade;
import com.keith.modules.service.user.UserTradeService;


@Service("userTradeService")
public class UserTradeServiceImpl extends ServiceImpl<UserTradeDao, UserTrade> implements UserTradeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTrade> page = this.page(
                new Query<UserTrade>().getPage(params),
                new QueryWrapper<UserTrade>()
        );

        return new PageUtils(page);
    }

}
