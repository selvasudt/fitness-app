package com.fitness.aiservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
public class RabbitMqConfig {

    // No longer needs to be public if not used outside the package
    private final RabbitMqProperties rabbitMqProperties;

    public RabbitMqConfig(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public Queue activityQueue(){
        return new Queue(rabbitMqProperties.getQueue().getName(), true);
    }

    @Bean
    public DirectExchange activityExchange(){
        return new DirectExchange(rabbitMqProperties.getExchange().getName());
    }

    @Bean
    public Binding activityBinding(Queue activityQueue, DirectExchange activityExchange){
        return BindingBuilder.bind(activityQueue).to(activityExchange).with(rabbitMqProperties.getQueue().getRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }
}
