package com.jobalert.job_alert_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ALERT_QUEUE = "alert-queue";

    @Bean
    public Queue alertQueue() {
        return new Queue(ALERT_QUEUE, true); // durable queue
    }
}