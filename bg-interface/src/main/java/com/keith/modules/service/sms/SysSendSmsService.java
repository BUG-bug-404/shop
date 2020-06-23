package com.keith.modules.service.sms;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.modules.entity.sms.SysSendSms;

public interface SysSendSmsService extends IService<SysSendSms> {
    void sendSms(SysSendSms sendSmsEntity);
}
