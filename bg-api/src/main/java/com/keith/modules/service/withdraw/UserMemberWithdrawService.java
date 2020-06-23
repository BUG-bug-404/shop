package com.keith.modules.service.withdraw;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.withdraw.UserMemberWithdraw;

import java.util.Map;

/**
 * 提现申请表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 13:47:48
 */
public interface UserMemberWithdrawService extends IService<UserMemberWithdraw> {

    PageUtils queryPage(Map<String, Object> params);
}

