package com.keith.modules.service.user;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dto.user.UserBanlanceDTO;
import com.keith.modules.entity.user.UserBanlance;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 用户余额表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-02 15:36:25
 */
public interface UserBanlanceService extends IService<UserBanlance> {

    PageUtils queryPage(Map<String, Object> params);



    /**
     * 充值
     * @param banlanceDTO
     * @param userMemberId
     * @return
     */
    JSONObject  rechargeUser(UserBanlanceDTO banlanceDTO, Long userMemberId, HttpServletRequest request);

    /**
     * 充值回调
     * @param request
     * @return
     */
    String getWxpayNotifyInfo(HttpServletRequest request);

    /**
     * 更新金额到钱包
     */
    void updateBanlance(Long userId,BigDecimal bigDecimal);
    /**
     * 查询账户余额
     */
    Map<String,BigDecimal> getBanlance(Long userId);

}

