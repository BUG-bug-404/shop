package com.keith.modules.dao.sms;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.sms.SysSendSms;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信发送表
 * 
 * @author lius
 * @email @qq.com
 * @date 2020-05-21 10:59:35
 */
@Mapper
public interface SysSendSmsDao extends BaseMapper<SysSendSms> {
	void sendSms(SysSendSms sendSmsEntity);
}
