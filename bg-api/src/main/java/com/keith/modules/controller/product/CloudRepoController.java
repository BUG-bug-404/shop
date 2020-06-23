package com.keith.modules.controller.product;


import com.keith.modules.service.product.CloudRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import io.swagger.annotations.Api;



/**
 * 回收申请表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-12 09:21:42
 */
@RestController
@RequestMapping("product/cloudrepo")
@Api(tags = {""})
public class CloudRepoController {
    @Autowired
    private CloudRepoService cloudRepoService;



}
