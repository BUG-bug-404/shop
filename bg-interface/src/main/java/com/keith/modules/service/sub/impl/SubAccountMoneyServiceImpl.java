package com.keith.modules.service.sub.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.sub.SubAccountMoneyDao;
import com.keith.modules.entity.sub.SubAccountMoney;
import com.keith.modules.service.sub.SubAccountMoneyService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("subAccountMoneyService")
public class SubAccountMoneyServiceImpl extends ServiceImpl<SubAccountMoneyDao, SubAccountMoney> implements SubAccountMoneyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SubAccountMoney> page = this.page(
                new Query<SubAccountMoney>().getPage(params),
                new QueryWrapper<SubAccountMoney>()
        );

        return new PageUtils(page);
    }

}
