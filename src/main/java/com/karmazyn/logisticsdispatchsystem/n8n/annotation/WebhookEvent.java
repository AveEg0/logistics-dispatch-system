package com.karmazyn.logisticsdispatchsystem.n8n.annotation;


import com.karmazyn.logisticsdispatchsystem.n8n.entity.WebhookEventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebhookEvent {
    WebhookEventType value();
}