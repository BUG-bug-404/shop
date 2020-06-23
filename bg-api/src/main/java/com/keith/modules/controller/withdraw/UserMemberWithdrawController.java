package com.keith.modules.controller.withdraw;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.withdraw.UserMemberWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 提现申请表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 13:47:48
 */
@RestController
@RequestMapping("withdraw/usermemberwithdraw")
public class UserMemberWithdrawController {
    @Autowired
    private UserMemberWithdrawService userMemberWithdrawService;



}
