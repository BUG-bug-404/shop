package com.keith.modules.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.user.UserLevel;

import java.util.List;
import java.util.Map;

/**
 * 店家等级表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-04 09:53:52
 */
public interface UserLevelService extends IService<UserLevel> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 获取所有用户等级权益
     */
    List<UserLevel> getList();
    /**
     * 根据用户ID获取用户对应的等级权益
     */
    UserLevel getByUserId(Long userId);
}

