package com.keith.modules.controller.productsettle;

import java.util.Arrays;
import java.util.Map;


import com.keith.modules.service.productsettle.UserProductSettleDateilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 商品结算详情表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 18:03:34
 */
@RestController
@RequestMapping("productsettle/userproductsettledateil")
public class UserProductSettleDateilController {
    @Autowired
    private UserProductSettleDateilService userProductSettleDateilService;



}
