package com.macro.mall.component;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author Caosen
 * @Date 2022/9/13 13:25
 * @Version 1.0
 */
//@Component
//@RabbitListener(queues = "TestDirectQueue")
//public class DirectReceiver {
//
//    @RabbitHandler
//    public void process(Map testMessage) {
//        System.out.println("测试直接交换机：" + testMessage.toString());
//    }
//
//}
