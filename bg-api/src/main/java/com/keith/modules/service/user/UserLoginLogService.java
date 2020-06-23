package com.keith.modules.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.user.UserLoginLog;

import java.util.Map;

/**
 * 用户登录日志
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-01 16:00:09
 */
public interface UserLoginLogService extends IService<UserLoginLog> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean save(Long userId);
}

