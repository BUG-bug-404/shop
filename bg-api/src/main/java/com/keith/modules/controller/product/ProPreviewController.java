package com.keith.modules.controller.product;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.keith.modules.service.product.ProPreviewService;
import io.swagger.annotations.Api;



/**
 * 预售设置表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-17 15:01:55
 */
@RestController
@RequestMapping("product/propreview")
@Api(tags = {""})
public class ProPreviewController {
    @Autowired
    private ProPreviewService proPreviewService;



}
