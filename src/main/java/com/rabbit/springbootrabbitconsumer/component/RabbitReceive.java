package com.rabbit.springbootrabbitconsumer.component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @Author zhunc
 * @Date 2022/5/14 18:28
 */
@Component
public class RabbitReceive {
    /**
     * 组合使用监听
     * @RabbitListener  @QueueBinding @Queue @Exchange
     * @param message
     * @param channel
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(value = "exchange-1", durable = "true", type = "topic"),
            ignoreDeclarationExceptions = "true",
            key = "springboot.*")
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        //1.收到消息后进行业务端消费处理
        System.out.println("-------------------------");
        System.out.println("消费消息："+ message.getPayload());
        //2.处理成功后 获取deliveryTag 并进行手工的ACK操作，因为我们配置文件里配置是手工签收
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, true);
    }
}
