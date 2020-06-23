package com.keith.modules.service.sms.impl;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.utils.RandomUtil;
import com.keith.common.exception.RRException;
import com.keith.modules.dao.sms.SysSendSmsDao;
import com.keith.modules.entity.sms.SysSendSms;
import com.keith.modules.service.sms.SysSendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service("sysSendSmsService")
public class SysSendSmsServiceImpl extends ServiceImpl<SysSendSmsDao, SysSendSms> implements SysSendSmsService {


    @Value("${sms.AccessKeyId}")
    private String accountKeyId;

    @Value("${sms.AccessKeySecret}")
    private String accessKeySecret;

    @Value("${sms.TemplateCode}")
    private String templateCode;

    @Value("${sms.signName}")
    private String signName;


    @Autowired
    private RedisTemplate redisTemplate = null;


    public void sendSms(SysSendSms sendSms) {
        String smsCode = RandomUtil.generateNumberString(6);
        //验证码设置一分钟失效
        redisTemplate.opsForValue().set("smsCode"+sendSms.getMobile()+sendSms.getType(),smsCode,180, TimeUnit.SECONDS);
        sendSmsCode(sendSms,smsCode);
    }

    @Async("asyncServiceExecutor")
    public  void  sendSmsCode(SysSendSms sendSms, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("default", accountKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setMethod(MethodType.POST);
        commonRequest.setDomain("dysmsapi.aliyuncs.com");
        commonRequest.setVersion("2017-05-25");
        commonRequest.setAction("SendSms");
        commonRequest.putBodyParameter("PhoneNumbers", sendSms.getMobile());
//       注册验证码短信 SMS_190510541
//        commonRequest.putBodyParameter("TemplateCode", smsCodeEnums.getTemplateCode());
        commonRequest.putBodyParameter("TemplateCode",templateCode);
//        commonRequest.putBodyParameter("templateCode","SMS_190510541");
        commonRequest.putBodyParameter("TemplateParam", "{code:" + code + "}");
//        SignName", "杭州硕旸科技有限公司");
        commonRequest.putBodyParameter("SignName", signName);

        //编辑短信实体类
        SysSendSms sysSendSms = new SysSendSms();
        sysSendSms.setMobile(sendSms.getMobile());
        sysSendSms.setType(sendSms.getType());
        sysSendSms.setSendContent("{code:" + code + "}");
        sysSendSms.setSendTime(new Date());
        try {
            CommonResponse response = client.getCommonResponse(commonRequest);
            sysSendSms.setSendData(response.getData());
            sysSendSms.setSendStatus(1);
            System.out.println("短信发送结果：{}"+response.getData());
            this.baseMapper.insert(sysSendSms);
        } catch (Exception e) {
            sysSendSms.setSendData(e.getMessage());
            sysSendSms.setSendStatus(2);
            this.baseMapper.insert(sysSendSms);
            throw new RRException("短信发送失败，请稍后重新尝试！");
        }
    }

}
