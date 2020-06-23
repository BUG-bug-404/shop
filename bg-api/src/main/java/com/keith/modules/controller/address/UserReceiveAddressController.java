package com.keith.modules.controller.address;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.common.validator.ValidatorUtils;
import com.keith.modules.dto.address.AddressDTO;
import com.keith.modules.dto.user.AddUserDTO;
import com.keith.modules.entity.address.UserReceiveAddress;
import com.keith.modules.service.address.UserReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.keith.common.utils.R;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 收货地址表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-02 20:08:46
 */
@Api(tags = {"收货地址表"})
@RestController
@RequestMapping("/userreceiveaddress")
public class UserReceiveAddressController {
    @Autowired
    private UserReceiveAddressService userReceiveAddressService;

    /**
     * 列表
     */
    @Login
    @PostMapping("/list")
    @ApiOperation("获取地址集合")
    public Result<List<UserReceiveAddress>> list(@ApiIgnore @RequestAttribute("userId") long userId){
        Result result = new Result();
        return result.ok(userReceiveAddressService.getList(userId));
    }


    /**
     * 保存或新增
     */
    @Login
    @ApiOperation("新增保存 (需要token)")
    @PostMapping("/save")
    public  Result save(@RequestBody UserReceiveAddress userReceiveAddress, @ApiIgnore @RequestAttribute("userId") long userId){
        Result result = new Result();
        userReceiveAddressService.add(userReceiveAddress,userId);
        return result;
    }


    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody UserReceiveAddress userReceiveAddress){
        Result result = new Result();
        userReceiveAddressService.removeById(userReceiveAddress.getId());
        return result;
    }

    /**
     * 修改默认地址
     */
    @Login
    @ApiOperation("修改默认地址")
    @PostMapping("/updateStatus")
    public Result delete(@RequestBody UserReceiveAddress userReceiveAddress, @ApiIgnore @RequestAttribute("userId") long userId){
        Result result = new Result();
        userReceiveAddressService.updateStatus(userReceiveAddress.getId(),userId);
        return result;
    }

}
