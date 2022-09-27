package com.macro.mall.config.RabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Caosen
 * @Date 2022/9/13 13:29
 * @Version 1.0
 */
@Configuration
public class TopicRabbitConfig {
    public final static String man = "topic.man";
    public final static String woman = "topic.woman";
    public final static String deadin = "topic.deadin";
    public final static String deadout = "topic.deadout";
    private String deadRoutingKey="dead_routingkey";


    @Bean("topic.man")
    public Queue fistQueue() {
        return new Queue(man,true);
    }
    @Bean("topic.woman")
    public Queue secondQueue() {
        return new Queue(woman,true);
    }

    @Bean("topic.deadin")
    public Queue thirdQueue() {
        Map<String,Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange",deadExchange());
        map.put("x-dead-letter-routing-key", deadout);
        map.put("x-message-ttl", 5000);
        return new Queue(deadin,true,false,false,map);
    }
    @Bean("topic.deadout")
    public Queue forthQueue() {
        return new Queue(deadout);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }
    @Bean
    Binding binding(){
        return BindingBuilder.bind(secondQueue()).to(topicExchange()).with("topic.woman");
    }
    @Bean
    Binding binding2(){
        return BindingBuilder.bind(fistQueue()).to(topicExchange()).with("topic.man");
    }
    @Bean
    Binding binding3() {
        return BindingBuilder.bind(thirdQueue()).to(topicExchange()).with("topic.deadin");
    }

    @Bean
    TopicExchange deadExchange() {
        return new TopicExchange("deadExchange");
    }

    @Bean
    Binding binding4(){
        return BindingBuilder.bind(forthQueue()).to(deadExchange()).with("topic.#");
    }


}
