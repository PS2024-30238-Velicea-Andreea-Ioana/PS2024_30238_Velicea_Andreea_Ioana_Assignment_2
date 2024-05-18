
package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.Payload;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class RabbitMQSender {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RabbitMQSender.class);
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Payload payload) {
        log.info("the payload is sended "+ payload.toString());
        rabbitTemplate.convertAndSend(exchange,routingkey,payload);
    }
}
