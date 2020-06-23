package com.keith.modules.controller;

import com.keith.common.deliverQuery.DeliverQuery;
import com.keith.common.utils.Result;
import com.keith.modules.dto.DeliveryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lzy
 * @date 2020/6/11 17:02
 */
@RestController
@RequestMapping("query/delivery")
@Api(tags = "物流查询")
public class DeliveryController {

    Result r = new Result();
    @PostMapping(value = {"/deliveryQuery"})
    @ApiOperation("物流查询")
    public Result deliveryQuery(@RequestBody @ApiParam DeliveryDTO deliveryDTO) throws Exception {
        DeliverQuery deliverQuery = new DeliverQuery();
        Object query = deliverQuery.deliverQuery(deliveryDTO.getNumber(), deliveryDTO.getType(), deliveryDTO.getPhone());
        return r.ok(query);
    }
}
