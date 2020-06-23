package com.keith.modules.controller.wx;

import com.keith.annotation.Login;
import com.keith.common.utils.Result;
import com.keith.modules.service.wx.WeXinService;
import com.keith.modules.service.wx.WxUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("wx")
@Api(tags = "小程序")
public class WeXinController {
    @Autowired
    private WeXinService weXinService;
    Result result=new Result();
    @GetMapping("/wxLogin")
    @ApiOperation("小程序授权")
    public Result wxLogin(@RequestParam("code") String code) throws Exception{

        Map<String,Object> map=weXinService.wxLogin(code);
        return  result.ok(map);
    }

    @Login
    @PostMapping("/wxSaveInfo")
    @ApiOperation("小程序获取信息")
    public Result wxSaveInfo(@RequestBody @ApiParam WxUserInfo wxUserInfo,
                             @ApiIgnore @RequestAttribute("userId") long userId) throws Exception{

        boolean userInfo = weXinService.saveUserInfo(wxUserInfo, userId);
        if(userInfo){
            return  result.ok(userInfo);
        }
        return result.error("失败");
    }



}
