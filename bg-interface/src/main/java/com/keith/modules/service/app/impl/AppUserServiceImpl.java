package com.keith.modules.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.app.UserMemberAppDao;
import com.keith.modules.entity.app.UserMemberApp;
import com.keith.modules.service.app.AppUserService;
import org.springframework.stereotype.Service;

/**
 * @author gray
 * @version 1.0
 * @date 2020/6/13 15:41
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<UserMemberAppDao,UserMemberApp> implements AppUserService {


    @Override
    public void test() {

    }
}
