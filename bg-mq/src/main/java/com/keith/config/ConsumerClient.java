package com.keith.config;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.google.common.collect.Maps;
import com.keith.common.config.MqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.Properties;
/**
 * Demo class
 *
 * @author John
 * @date 2019/08/31
 */
@Configuration
public class ConsumerClient {

    @Autowired
    private MqConfig mqConfig;

    //普通消息监听器，Consumer注册消息监听器来订阅消息.
    @Autowired
    private MqMessageListener messageListener;

    //定时消息监听器，Consumer注册消息监听器来订阅消息.
    @Autowired
    private MqTimeMessageListener timeMessageListener;

    //顺序消息监听器，Consumer注册消息监听器来订阅消息.
    @Autowired
    private MqfifoMessageListener fifoMessageListener;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqPropertie();
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqConfig.getGroupId());
        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");
        consumerBean.setProperties(properties);

        //订阅消息
        Map<Subscription, MessageListener> subscriptionTable =  Maps.newHashMap();
        //订阅普通消息
        Subscription subscription = new Subscription();
        subscription.setTopic(mqConfig.getTopic());
        subscription.setExpression(mqConfig.getTag());
        subscriptionTable.put(subscription, messageListener);

        //订阅定时/延时消息
        Subscription subscriptionTime = new Subscription();
        subscriptionTime.setTopic(mqConfig.getTimeTopic());
        subscriptionTime.setExpression(mqConfig.getTimeTag());
        subscriptionTable.put(subscriptionTime, timeMessageListener);

        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public OrderConsumerBean buildFifoConsumer() {
        OrderConsumerBean orderConsumerBean = new OrderConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqPropertie();
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqConfig.getFifoGroupId());
        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");
        orderConsumerBean.setProperties(properties);

        //订阅消息
        Map<Subscription, MessageOrderListener> subscriptionTable = Maps.newHashMap();
        //订阅分区顺序
        Subscription subscription = new Subscription();
        subscription.setTopic(mqConfig.getFifoTopic());
        subscription.setExpression(mqConfig.getFifoTag());
        subscriptionTable.put(subscription, fifoMessageListener);

        orderConsumerBean.setSubscriptionTable(subscriptionTable);
        return orderConsumerBean;
    }

}
