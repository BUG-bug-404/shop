package com.keith.modules.service.sub.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.sub.SubAccountUserDao;
import com.keith.modules.entity.sub.SubAccountUser;
import com.keith.modules.service.sub.SubAccountUserService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("subAccountUserService")
public class SubAccountUserServiceImpl extends ServiceImpl<SubAccountUserDao, SubAccountUser> implements SubAccountUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SubAccountUser> page = this.page(
                new Query<SubAccountUser>().getPage(params),
                new QueryWrapper<SubAccountUser>()
        );

        return new PageUtils(page);
    }

}
