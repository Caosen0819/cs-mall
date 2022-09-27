package com.macro.mall.component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * @Author Caosen
 * @Date 2022/9/13 14:55
 * @Version 1.0
 */

@Component
public class MyAckReceiver implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliverytag: " + deliveryTag);
        try {
            byte[] body = message.getBody();
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(body));
            Map<String, String> o = (Map<String, String>)objectInputStream.readObject();
            String messageId = o.get("messageId");
            String messageData = o.get("messageData");
            String createTime = o.get("createTime");
            objectInputStream.close();
            if ("TestDirectQueue".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("消费的消息来自的队列名为："+message.getMessageProperties().getConsumerQueue());
                System.out.println("消息成功消费到  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);
                System.out.println("执行TestDirectQueue中的消息的业务处理流程......");

            }
            if ("topic.man".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("  topic.man  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);
                System.out.println("消费的主题消息来自："+message.getMessageProperties().getConsumerQueue());
                channel.basicAck(deliveryTag, true);
            }
            if ("topic.woman".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("  topic.woman  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);
                System.out.println("消费的主题消息来自："+message.getMessageProperties().getConsumerQueue());
                channel.basicAck(deliveryTag, true);
            }
            if ("topic.deadin".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("这里是deadin队列");
                channel.basicReject(deliveryTag, false);
            }
            if ("topic.deadout".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("这里是deadout队列");
                channel.basicAck(deliveryTag, true);
            }


            ////第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
        } catch (Exception e) {
            channel.basicAck(deliveryTag, false);
            e.printStackTrace();
        }
    }

}
