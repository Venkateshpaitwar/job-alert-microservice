package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertMessageConsumer {

    private final AlertService alertService;

    @RabbitListener(queues = RabbitMQConfig.ALERT_QUEUE)
    public void consumeAlertTrigger(String message) {
        log.info("Received message from queue: {}", message);
        alertService.processAlerts();
    }
}