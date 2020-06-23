package com.keith.common.utils;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.OrderProducerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.keith.common.config.MqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MQ发送消息助手
 * @author laifuwei
 */
@Component
public class ProducerUtil {

    private static final Logger log = LoggerFactory.getLogger(ProducerUtil.class);

    @Autowired
    private MqConfig config;

    @Autowired
    private ProducerBean producer;

    @Autowired
    private OrderProducerBean orderProducer;


    /**
     * 同步发送消息
     * @param msgTag 标签，可用于消息小分类标注
     * @param messageBody 消息body内容，生产者自定义内容
     * @param msgKey 消息key值，建议设置全局唯一，可不传，不影响消息投递
     * @return success:SendResult or error:null
     */
    public SendResult sendMsg(String msgTag, byte[] messageBody, String msgKey) {
        Message msg = new Message(config.getTopic(),msgTag,msgKey,messageBody);
        return this.send(msg,Boolean.FALSE);
    }
    /**
     * 同步发送定时/延时消息
     * @param msgTag 标签，可用于消息小分类标注，对消息进行再归类
     * @param messageBody 消息body内容，生产者自定义内容，二进制形式的数据
     * @param msgKey 消息key值，建议设置全局唯一值，可不设置，不影响消息收发
     * @param delayTime 服务端发送消息时间，立即发送输入0或比更早的时间
     * @return success:SendResult or error:null
     */
    public SendResult sendTimeMsg(String msgTag,byte[] messageBody,String msgKey,long delayTime) {
        Message msg = new Message(config.getTimeTopic(),msgTag,msgKey,messageBody);
        msg.setStartDeliverTime(delayTime);
        return this.send(msg,Boolean.FALSE);
    }
    /**
     * 发送单向消息
     */
    public void sendOneWayMsg(String msgTag,byte[] messageBody,String msgKey) {
        Message msg = new Message(config.getTopic(),msgTag,msgKey,messageBody);
        this.send(msg,Boolean.TRUE);
    }

    /**
     * 普通消息发送发放
     * @param msg 消息
     * @param isOneWay 是否单向发送
     */
    private SendResult send(Message msg,Boolean isOneWay) {
        try {
            if(isOneWay) {
                //由于在 oneway 方式发送消息时没有请求应答处理，一旦出现消息发送失败，则会因为没有重试而导致数据丢失。
                //若数据不可丢，建议选用同步或异步发送方式。
                producer.sendOneway(msg);
                success(msg, "单向消息MsgId不返回");
                return null;
            }else {
                //可靠同步发送
                SendResult sendResult = producer.send(msg);
                //获取发送结果，不抛异常即发送成功
                if (sendResult != null) {
                    success(msg, sendResult.getMessageId());
                    return sendResult;
                }else {
                    error(msg,null);
                    return null;
                }
            }
        } catch (Exception e) {
            error(msg,e);
            return null;
        }
    }

    /**
     *分区顺序小心发送
     * @param msg
     * @param shardingKey
     * @return
     */
    private SendResult send(Message msg,String shardingKey) {
        try {
                //可靠同步发送
                SendResult sendResult = orderProducer.send(msg,shardingKey);
                //获取发送结果，不抛异常即发送成功
                if (sendResult != null) {
                    success(msg, sendResult.getMessageId());
                    return sendResult;
                }else {
                    error(msg,null);
                    return null;
                }
        } catch (Exception e) {
            error(msg,e);
            return null;
        }
    }

    //对于使用异步接口，可设置单独的回调处理线程池，拥有更灵活的配置和监控能力。
    //根据项目需要，服务器配置合理设置线程数，线程太多有OOM 风险，
    private ExecutorService threads = Executors.newFixedThreadPool(3);
    //仅建议执行轻量级的Callback任务，避免阻塞公共线程池 引起其它链路超时。

    /**
     * 异步发送普通消息
     * @param msgTag
     * @param messageBody
     * @param msgKey
     */
    public void sendAsyncMsg(String msgTag,byte[] messageBody,String msgKey) {
        producer.setCallbackExecutor(threads);

        Message msg = new Message(config.getTopic(),msgTag,msgKey,messageBody);
        try {
            producer.sendAsync(msg, new SendCallback() {
                @Override
                public void onSuccess(final SendResult sendResult) {
                    assert sendResult != null;
                    success(msg, sendResult.getMessageId());
                }
                @Override
                public void onException(final OnExceptionContext context) {
                    //出现异常意味着发送失败，为了避免消息丢失，建议缓存该消息然后进行重试。
                    error(msg,context.getException());
                }
            });
        } catch (ONSClientException e) {
            error(msg,e);
        }
    }

    /**
     *分区发送
     * @param msgTag
     * @param messageBody
     * @param msgKey
     * @return
     */
    public SendResult sendOrderMsg(String msgTag, byte[] messageBody, String msgKey) {
        Message msg = new Message(config.getFifoTopic(),msgTag,msgKey,messageBody);
        // 分区顺序消息中，区分不同分区的关键字段，Sharding Key 与普通消息的 key 是完全不同的概念。
        // 全局顺序消息，该字段可以设置为任意非空字符串。
        String shardingKey = String.valueOf(msgKey);
        return this.send(msg,shardingKey);
    }


    //--------------日志打印----------
    private void error(Message msg,Exception e) {
        e.printStackTrace();
        log.info("e"+e);
        log.error("发送MQ消息失败-- Topic:{}, Key:{}, tag:{}, body:{}"
                ,msg.getTopic(),msg.getKey(),msg.getTag(),new String(msg.getBody()));
        log.error("errorMsg --- {}",e.getMessage());
    }
    private void success(Message msg,String messageId) {
        log.info("发送MQ消息成功 -- Topic:{} ,msgId:{} , Key:{}, tag:{}, body:{}"
                ,msg.getTopic(),messageId,msg.getKey(),msg.getTag(),new String(msg.getBody()));
    }

}
