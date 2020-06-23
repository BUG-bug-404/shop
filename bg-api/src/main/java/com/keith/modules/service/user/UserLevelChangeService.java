package com.keith.modules.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.user.UserLevelChange;

import java.util.Map;

/**
 * 店家等级变化表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-04 09:53:52
 */
public interface UserLevelChangeService extends IService<UserLevelChange> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户花199升级成为A级店长
     */
    void updatelevelA(Long userId);
    void updatelevelB(Long userId);
    void updatelevelC(Long userId);

    void updateLevelByOrder(Long orderId);
}

