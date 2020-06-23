package com.keith.modules.service.productsettle.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.productsettle.UserProductSettleDao;
import com.keith.modules.entity.productsettle.UserProductSettle;
import com.keith.modules.service.productsettle.UserProductSettleService;


@Service("userProductSettleService")
public class UserProductSettleServiceImpl extends ServiceImpl<UserProductSettleDao, UserProductSettle> implements UserProductSettleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserProductSettle> page = this.page(
                new Query<UserProductSettle>().getPage(params),
                new QueryWrapper<UserProductSettle>()
        );

        return new PageUtils(page);
    }

}
