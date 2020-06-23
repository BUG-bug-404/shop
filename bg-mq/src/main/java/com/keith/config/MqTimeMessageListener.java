package com.keith.config;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.keith.modules.service.order.OrderOrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 定时/延时MQ消息监听消费
 *
 * @author JohnSon
 */
@Component
@Slf4j
public class MqTimeMessageListener implements MessageListener {

    //private static final Logger log = LoggerFactory.getLogger(MqTimeMessageListener.class);

    @Autowired
    private OrderOrderService orderService;


    //实现MessageListtener监听器的消费方法
    @Override
    public Action consume(Message message, ConsumeContext context) {

        log.info("接收到MQ消息 -- Topic:{}, tag:{},msgId:{} , Key:{}, body:{}",
                message.getTopic(), message.getTag(), message.getMsgID(), message.getKey(), new String(message.getBody()));
        try {
            String msgTag = message.getTag();//消息类型
            String msgKey = message.getKey();//业务唯一id
            log.info("--" + msgTag);
            log.info("+++" + msgKey);
            byte[] body = message.getBody();
            String string = new String(body);
            JSONObject jsonObject = JSONObject.parseObject(string);
            String orderNo = jsonObject.getString("orderNo");
            log.info("订单编号" + orderNo);

            if (msgTag.equals("time-01")) {
                Map<String, Object> map = orderService.updateOrderStatus(orderNo);
            }

            if (msgTag.equals("time-02")) {

            }
            if (msgTag.equals("time-03")) {
                Map<String, Object> map = orderService.updateOrderStatus(orderNo);
            }
            if (msgTag.equals("time-04")) {
                Map<String, Object> map = orderService.updateOrderStatus(orderNo);
            }
            if (msgTag.equals("time-05")) {
                /*拿自己的货物走人*/
            }
            if (msgTag.equals("time-06")) {
                Map<String, Object> map = orderService.updateOrderStatus(orderNo);
            }
            if (msgTag.equals("time-07")) {
                /*付尾款*/
            }



            //消费成功，继续消费下一条消息
            return Action.CommitMessage;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("e" + e);

            log.error("消费MQ消息失败！ msgId:" + message.getMsgID() + "----ExceptionMsg:" + e.getMessage());
            //消费失败，告知服务器稍后再投递这条消息，继续消费其他消息
            return Action.ReconsumeLater;
        }
    }

}
