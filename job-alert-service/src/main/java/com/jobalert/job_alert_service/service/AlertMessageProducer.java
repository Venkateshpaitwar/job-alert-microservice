package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishAlertTrigger(String message) {
        log.info("Publishing message to queue: {}", message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.ALERT_QUEUE, message);
    }
}