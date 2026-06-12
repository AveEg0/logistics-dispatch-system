package com.karmazyn.logisticsdispatchsystem.n8n.dto;

import com.karmazyn.logisticsdispatchsystem.n8n.entity.WebhookEventType;
import lombok.Data;

@Data
public class WebhookEventDto<T> {
    private WebhookEventType EventType;
    private T Data;
}
