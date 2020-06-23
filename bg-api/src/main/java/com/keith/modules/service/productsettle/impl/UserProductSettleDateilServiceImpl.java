package com.keith.modules.service.productsettle.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.productsettle.UserProductSettleDateilDao;
import com.keith.modules.entity.productsettle.UserProductSettleDateil;
import com.keith.modules.service.productsettle.UserProductSettleDateilService;


@Service("userProductSettleDateilService")
public class UserProductSettleDateilServiceImpl extends ServiceImpl<UserProductSettleDateilDao, UserProductSettleDateil> implements UserProductSettleDateilService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserProductSettleDateil> page = this.page(
                new Query<UserProductSettleDateil>().getPage(params),
                new QueryWrapper<UserProductSettleDateil>()
        );

        return new PageUtils(page);
    }

}
