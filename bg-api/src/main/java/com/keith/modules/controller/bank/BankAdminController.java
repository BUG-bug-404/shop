package com.keith.modules.controller.bank;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.bank.BankAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 银行卡表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-18 11:25:22
 */
@RestController
@RequestMapping("bank/bankadmin")
public class BankAdminController {
    @Autowired
    private BankAdminService bankAdminService;



}
