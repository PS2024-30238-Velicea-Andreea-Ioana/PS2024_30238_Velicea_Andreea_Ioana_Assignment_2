
package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.Payload;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * Service class for sending messages to RabbitMQ.
 */
@Service
public class RabbitMQSender {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RabbitMQSender.class);
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    /**
     * Constructs a RabbitMQSender with the specified RabbitTemplate.
     *
     * @param rabbitTemplate The RabbitTemplate used for sending messages.
     */
    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * Sends a payload to RabbitMQ.
     *
     * @param payload The payload to be sent.
     */
    public void send(Payload payload) {
        log.info("the payload is sended "+ payload.toString());
        rabbitTemplate.convertAndSend(exchange,routingkey,payload);
    }
}
