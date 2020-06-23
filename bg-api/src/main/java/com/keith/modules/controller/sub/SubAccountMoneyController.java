package com.keith.modules.controller.sub;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.sub.SubAccountMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 分账记录表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:10
 */
@RestController
@RequestMapping("sub/subaccountmoney")
public class SubAccountMoneyController {
    @Autowired
    private SubAccountMoneyService subAccountMoneyService;



}
