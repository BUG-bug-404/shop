package com.keith.modules.service.user.impl;

import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.user.UserLoginLogDao;
import com.keith.modules.entity.user.UserLoginLog;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.service.user.UserLoginLogService;


@Service("userLoginLogService")
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogDao, UserLoginLog> implements UserLoginLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLoginLog> page = this.page(
                new Query<UserLoginLog>().getPage(params),
                new QueryWrapper<UserLoginLog>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean save(Long userId) {
        UserLoginLog userLoginLog =new UserLoginLog();
        userLoginLog.setUserId(userId);
        return this.save(userLoginLog);
    }

}
