package com.keith.modules.controller.productsettle;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.productsettle.UserProductSettleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 供应商，商品结算表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 18:03:34
 */
@RestController
@RequestMapping("productsettle/userproductsettle")
public class UserProductSettleController {
    @Autowired
    private UserProductSettleService userProductSettleService;



}
