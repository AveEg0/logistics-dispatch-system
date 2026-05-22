package com.karmazyn.logisticsdispatchsystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.webhooks")
@Data
public class WebhookProperties {
    private String orderAssignedUrl;
}
