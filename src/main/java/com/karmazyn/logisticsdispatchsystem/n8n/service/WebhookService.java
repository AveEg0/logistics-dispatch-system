package com.karmazyn.logisticsdispatchsystem.n8n.service;

import com.karmazyn.logisticsdispatchsystem.config.WebhookProperties;
import com.karmazyn.logisticsdispatchsystem.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final RestTemplate restTemplate;
    private final WebhookProperties webhookProperties;
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebhookService.class);

    @Async
    public void sendOrderAssignedEvent(OrderResponseDto order) {

        try {
            log.warn("Sending order assigned event webhook to {}", webhookProperties.getOrderAssignedUrl());
            restTemplate.postForEntity(webhookProperties.getOrderAssignedUrl(), order, String.class);
        } catch (Exception e) {
            log.warn("Failed to send order assigned event webhook", e);
        }
    }
}
