package com.anton.gateway.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }
}
