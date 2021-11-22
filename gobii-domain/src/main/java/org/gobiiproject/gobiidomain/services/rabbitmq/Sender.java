package org.gobiiproject.gobiidomain.services.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sender {

    @Value("${rabbitmq.sender.host}")
    private String rabbitHost;

    public void send(String queueName, String message) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.convertAndSend(queueName, message);
        log.debug(" [x] Sent '" + message + "'");
    }
}
