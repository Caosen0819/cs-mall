//package com.macro.mall.component;
//
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * @Author Caosen
// * @Date 2022/9/13 13:36
// * @Version 1.0
// */
//@Component
//@RabbitListener(queues = "topic.man")
//public class TopicManReceiver {
//
//    @RabbitHandler
//    public void process(Map map){
//        System.out.println("test:topic.man" + map);
//
//    }
//}
