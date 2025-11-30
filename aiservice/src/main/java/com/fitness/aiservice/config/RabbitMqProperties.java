package com.fitness.aiservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqProperties {

    private ActivityExchange exchange;
    private ActivityQueue queue;

    @Getter
    @Setter
    public static class ActivityExchange {
        private String name;
    }

    @Getter
    @Setter
    public static class ActivityQueue {
        private String name;
        private String routingKey;
    }
}