package com.karmazyn.logisticsdispatchsystem.n8n.aspect;

import com.karmazyn.logisticsdispatchsystem.n8n.annotation.WebhookEvent;
import com.karmazyn.logisticsdispatchsystem.n8n.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WebhookAspect {

    private final WebhookService webhookService;

    @Around("@annotation(webhookEvent)")
    public Object handleEvent(ProceedingJoinPoint joinPoint, WebhookEvent event) throws Throwable {

        log.info(">>> WebhookAspect triggered: {}", event.value());
        Object result = joinPoint.proceed();

        try {

            log.info(">>> WebhookAspect firing send for: {}", event.value());
            webhookService.send(event.value(), result);
        } catch (Exception e) {
            log.warn("Webhook event error [{}]: {}", event.value(), e.getMessage());
        }

        return result;
    }

}
