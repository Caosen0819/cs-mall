package com.macro.mall.controller;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author Caosen
 * @Date 2022/9/13 11:33
 * @Version 1.0
 */
@RestController
public class SendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //测试死信队列
    @GetMapping(value = "/sendDeadMessage")
    public String sendDeadMessage(){
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test Dead Message";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);

        rabbitTemplate.convertAndSend("topicExchange", "topic.deadin", map);
        return "ok1";
    }


    @GetMapping(value = "/sendDirectMessage")
    public String sendDirectMessage(){
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test Direct Message";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);

        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return "ok";
    }

    @GetMapping(value = "/sendTopicMessage1")
    public String sendTopicMessage1(){
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test topic.man Message";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);

        rabbitTemplate.convertAndSend("topicExchange", "topic.man", map);
        return "ok";
    }
    @GetMapping(value = "/sendTopicMessage2")
    public String sendTopicMessage2(){
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test Topic.woman Message";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);

        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", map);
        return "ok";
    }

    //消息推送到server，但是在server里找不到交换机
    @GetMapping("/TestMessageAck")
    public String TestMessageAck() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: non-existent-exchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", map);
        return "ok";
    }
    // 结论： ①这种情况触发的是 ConfirmCallback 回调函数。




    //②消息推送到server，找到交换机了，但是没找到队列
    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }
    // 结论：②这种情况触发的是 ConfirmCallback和RetrunCallback两个回调函数。

    @GetMapping("/TestMessageAck2")
    public String TestMessageAck2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: lonelyDirectExchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("lonelyDirectExchange", "TestDirectRouting", map);
        return "ok";
    }
}
