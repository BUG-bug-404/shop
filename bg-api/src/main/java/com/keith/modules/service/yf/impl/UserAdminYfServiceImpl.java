package com.keith.modules.service.yf.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.yf.UserAdminYfDao;
import com.keith.modules.entity.yf.UserAdminYf;
import com.keith.modules.service.yf.UserAdminYfService;


@Service("userAdminYfService")
public class UserAdminYfServiceImpl extends ServiceImpl<UserAdminYfDao, UserAdminYf> implements UserAdminYfService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserAdminYf> page = this.page(
                new Query<UserAdminYf>().getPage(params),
                new QueryWrapper<UserAdminYf>()
        );

        return new PageUtils(page);
    }

}
