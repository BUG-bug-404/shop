package com.keith.modules.controller.sub;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.sub.SubAccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 添加分账方表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-12 14:56:10
 */
@RestController
@RequestMapping("sub/subaccountuser")
public class SubAccountUserController {
    @Autowired
    private SubAccountUserService subAccountUserService;



}
