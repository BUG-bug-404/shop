package com.keith.common.config;

import com.aliyun.openservices.ons.api.bean.OrderProducerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ配置注入生成消息实例
 *
 * @author John
 * @date 2019/08/01
 */
@Configuration
public class ProducerClient {

    @Autowired
    private MqConfig mqConfig;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        //ProducerBean用于将Producer集成至Spring Bean中
        ProducerBean producer = new ProducerBean();
        producer.setProperties(mqConfig.getMqPropertie());
        return producer;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public OrderProducerBean buildOrderProducer() {
        //ProducerBean用于将Producer集成至Spring Bean中
        OrderProducerBean orderProducer = new OrderProducerBean();
        orderProducer.setProperties(mqConfig.getMqPropertie());
        return orderProducer;
    }
}