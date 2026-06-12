package com.karmazyn.logisticsdispatchsystem.n8n.service;

import com.karmazyn.logisticsdispatchsystem.config.WebhookProperties;
import com.karmazyn.logisticsdispatchsystem.n8n.dto.WebhookEventDto;
import com.karmazyn.logisticsdispatchsystem.n8n.entity.WebhookEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {
    private final RestTemplate restTemplate;
    private final WebhookProperties webhookProperties;

    @Async("taskExecutor")
    public void send(WebhookEventType webhookEventType, Object payload) {

       String url = webhookProperties.getLogisticsEventUrl();

       if (url == null || url.isBlank()) {
           log.warn("Webhook URL is null or blank");
           return;
       }

       try {
           log.info("Sending webhook event webhook to {}", url);
           WebhookEventDto webhookEvent = new WebhookEventDto();
           webhookEvent.setEventType(webhookEventType);
           webhookEvent.setData(payload);
           restTemplate.postForEntity(url, webhookEvent, String.class);
       }catch (Exception e) {
           log.warn("Failed to send webhook event webhook [{}]: to {}", url, e.getMessage());
       }
    }

}
