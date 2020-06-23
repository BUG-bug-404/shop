package com.keith.modules.service.yf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.yf.UserAdminYf;

import java.util.Map;

/**
 * 
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-17 14:39:03
 */
public interface UserAdminYfService extends IService<UserAdminYf> {

    PageUtils queryPage(Map<String, Object> params);
}

