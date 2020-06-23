package com.keith.modules.controller.withdraw;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.withdraw.SetWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 提现设置
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 15:44:42
 */
@RestController
@RequestMapping("withdraw/setwithdraw")
public class SetWithdrawController {
    @Autowired
    private SetWithdrawService setWithdrawService;



}
