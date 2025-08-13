package com.company.fastweb.core.infra.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 事件发布器
 *
 * @author fastweb
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 同步发布事件
     *
     * @param event 事件对象
     */
    public void publishEvent(Object event) {
        try {
            applicationEventPublisher.publishEvent(event);
            log.debug("Event published successfully: {}", event.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.getClass().getSimpleName(), e);
        }
    }

    /**
     * 异步发布事件
     *
     * @param event 事件对象
     */
    @Async
    public void publishEventAsync(Object event) {
        publishEvent(event);
    }
}