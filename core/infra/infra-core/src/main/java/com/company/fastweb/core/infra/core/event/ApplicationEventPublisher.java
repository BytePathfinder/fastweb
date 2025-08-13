package com.company.fastweb.core.infra.core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * 应用事件发布器
 *
 * @author fastweb
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationEventPublisher {

    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    /**
     * 发布事件
     *
     * @param event 事件对象
     */
    public void publishEvent(ApplicationEvent event) {
        try {
            eventPublisher.publishEvent(event);
            log.debug("Event published successfully: {}", event.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.getClass().getSimpleName(), e);
        }
    }

    /**
     * 发布事件
     *
     * @param event 事件对象
     */
    public void publishEvent(Object event) {
        try {
            eventPublisher.publishEvent(event);
            log.debug("Event published successfully: {}", event.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.getClass().getSimpleName(), e);
        }
    }
}