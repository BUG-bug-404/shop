package com.keith.config;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
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
public class MqfifoMessageListener implements MessageOrderListener {

    private static final Logger log = LoggerFactory.getLogger(MqfifoMessageListener.class);

    @Override
    public OrderAction consume(Message message, ConsumeOrderContext consumeOrderContext) {
                log.info("接收到MQ消息 -- Topic:{}, tag:{},msgId:{} , Key:{}, body:{}",
                message.getTopic(),message.getTag(),message.getMsgID(),message.getKey(),new String(message.getBody()));
          try {
            String msgTag = message.getTag();//消息类型
            String msgKey = message.getKey();//业务唯一id
            //do something..
            return OrderAction.Success;
        } catch (Exception e) {
            //消费失败
            return OrderAction.Suspend;
        }
    }
}
