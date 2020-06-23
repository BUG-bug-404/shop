package com.keith.modules.service.withdraw;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.withdraw.SetWithdraw;

import java.util.Map;

/**
 * 提现设置
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 15:44:42
 */
public interface SetWithdrawService extends IService<SetWithdraw> {

    PageUtils queryPage(Map<String, Object> params);
}

