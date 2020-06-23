package com.keith.modules.controller.bank;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.bank.BankCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 17:09:51
 */
@RestController
@RequestMapping("bank/bankcat")
public class BankCatController {
    @Autowired
    private BankCatService bankCatService;



}
