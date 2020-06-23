package com.keith.modules.controller.sms;


import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.utils.Format;
import com.keith.common.utils.Result;
import com.keith.modules.entity.sms.SysSendSms;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.sms.SysSendSmsService;
import com.keith.modules.service.user.UserMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lius
 * @Date 2020/5/20
 */
@RestController
@RequestMapping(value = {"/sms"})
@Api(tags = {"短信相关"})
public class SendSmsController {

    @Autowired
    private SysSendSmsService sendSmsService;
    @Autowired
    private UserMemberService userMemberService;
    /**
     * 注册验证码发送
     * @return
     */
    @PostMapping(value = "/sendSms")
    @ApiOperation(value = "用户注册验证码发送")
    public Result SendSms(@RequestBody @Validated SysSendSms sendSms){
        Result result = new Result();
        if (sendSms.getMobile() == null || StringUtils.isEmpty(sendSms.getMobile())) {
            return result.error("手机号码随意性为空");
        } else if (!Format.MatcherPhone(sendSms.getMobile())) {
            return result.error("手机号码格式错误");
        }

        UserMember user = userMemberService.getOne(new LambdaQueryWrapper<UserMember>().eq(UserMember::getMobile,sendSms.getMobile()));
        if (user==null){
            return result.error("该用户不存在");
        }
        if (user.getLevelId()==1){
            return result.error("该用户不支持");
        }

        sendSmsService.sendSms(sendSms);

        return result.ok("success");
    }

    public void main(String[] args) {
//        this.SendSms("15267080541");
    }
}
