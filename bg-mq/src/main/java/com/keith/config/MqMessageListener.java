package com.keith.config;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Demo class
 *
 * @author John
 * @date 2019/09/31
 */
@Component
public class MqMessageListener implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MqMessageListener.class);



    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        System.out.println("Receive: " + message);
        log.info("接收到MQ消息 -- Topic:{}, tag:{},msgId:{} , Key:{}, body:{}",
                message.getTopic(),message.getTag(),message.getMsgID(),message.getKey(),new String(message.getBody()));
        try {
            String msgTag = message.getTag();//消息类型
            String msgKey = message.getKey();//业务唯一id

           byte[] body=message.getBody();
           String string=new String(body);
           JSONObject jsonObject=JSONObject.parseObject(string);
           String userId=jsonObject.getString("userId");

            //shopUserOrderService.findByOrederNo();

            log.info("3--------------"+userId+"++++++++++++++++++");
            //do something..
            return Action.CommitMessage;
        } catch (Exception e) {
            log.info("异常"+e);

            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
