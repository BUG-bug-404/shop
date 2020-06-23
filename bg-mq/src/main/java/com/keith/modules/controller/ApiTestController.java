package com.keith.modules.controller;


import com.alibaba.fastjson.JSONObject;
import com.keith.common.utils.ProducerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author JohnSon
 */
@RestController
@RequestMapping("/api")
@Api(tags="测试接口")
public class ApiTestController {
    //普通消息的Producer 已经注册到了spring容器中，后面需要使用时可以 直接注入到其它类中
    @Autowired
    private ProducerUtil producer;

    /**
     * 演示方法，可在自己的业务系统方法中进行发送消息
     */
    @PostMapping("mqTest")
    @ApiOperation(value = "mqTest")
    public String mqTest() {
        /*  使用前面封装的方法，传入对应的参数即可发送消息
         *  msgTag 标签，可用于消息小分类标注
         *  messageBody 消息body内容，生产者自定义内容,任何二进制数据，生产者和消费者协定数据的序列化和反序列化
         *  msgKey 消息key值，建议设置全局唯一，可不传，不影响消息投递
         */
        //body内容自定义
        JSONObject body = new JSONObject();
        body.put("userId", "this is userId");
        body.put("notice", "同步消息");
        //同步发送消息
        producer.sendMsg("userMessage1", body.toJSONString().getBytes(), "messageId");
        return "ok";
    }
    @PostMapping("sendOneWayMsg")
    @ApiOperation(value = "sendOneWayMsg")
    public String sendOneWayMsg() {
        /*  使用前面封装的方法，传入对应的参数即可发送消息
         *  msgTag 标签，可用于消息小分类标注
         *  messageBody 消息body内容，生产者自定义内容,任何二进制数据，生产者和消费者协定数据的序列化和反序列化
         *  msgKey 消息key值，建议设置全局唯一，可不传，不影响消息投递
         */
        //单向消息
        producer.sendOneWayMsg("userMessage2", "单向消息".getBytes(), "messageId01");
        return "ok";
    }
    @PostMapping("sendAsyncMsg")
    @ApiOperation(value = "sendAsyncMsg")
    public String sendAsyncMsg() {
        /*  使用前面封装的方法，传入对应的参数即可发送消息
         *  msgTag 标签，可用于消息小分类标注
         *  messageBody 消息body内容，生产者自定义内容,任何二进制数据，生产者和消费者协定数据的序列化和反序列化
         *  msgKey 消息key值，建议设置全局唯一，可不传，不影响消息投递
         */
        //异步消息
        producer.sendAsyncMsg("userMessage3", "异步消息".getBytes(), "messageId02");
        return "ok";
    }
    @PostMapping("sendTimeMsg")
    @ApiOperation(value = "sendTimeMsg")
    public String sendTimeMsg() {
        /*  使用前面封装的方法，传入对应的参数即可发送消息
         *  msgTag 标签，可用于消息小分类标注
         *  messageBody 消息body内容，生产者自定义内容,任何二进制数据，生产者和消费者协定数据的序列化和反序列化
         *  msgKey 消息key值，建议设置全局唯一，可不传，不影响消息投递
         */
        //定时/延时消息,当前时间的30秒后推送。时间自己定义

        JSONObject body = new JSONObject();
        body.put("userId", "this is userId");
        body.put("notice", "延时消息");

        producer.sendTimeMsg("time", body.toJSONString().getBytes(), "messageId", System.currentTimeMillis()+100000);
        return "ok";
    }

    @PostMapping("sendOrderMsg")
    @ApiOperation(value = "sendOrderMsg")
    public String sendOrderMsg() {
        /*  使用前面封装的方法，传入对应的参数即可发送消息
         *  msgTag 标签，可用于消息小分类标注
         *  messageBody 消息body内容，生产者自定义内容,任何二进制数据，生产者和消费者协定数据的序列化和反序列化
         *  msgKey 消息key值，建议设置全局唯一，可不传，不影响消息投递
         */
        //顺序消息（全局顺序 / 分区顺序）
        producer.sendOrderMsg("fifo-01", "分区顺序消息".getBytes(), "messageId");
        return "ok";
    }


}
