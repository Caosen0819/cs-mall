package com.macro.mall.config.RabbitMQ;

import com.macro.mall.component.MyAckReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Caosen
 * @Date 2022/9/13 14:50
 * @Version 1.0
 */

@Configuration
public class MessageListenerConfig {
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MyAckReceiver myAckReceiver;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(cachingConnectionFactory);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMaxConcurrentConsumers(1);
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        simpleMessageListenerContainer.setQueueNames( "topic.man", "topic.woman", "topic.deadin", "topic.deadout");

        simpleMessageListenerContainer.setMessageListener(myAckReceiver);

        return simpleMessageListenerContainer;
    }
}
