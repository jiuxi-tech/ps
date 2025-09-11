package com.jiuxi.shared.security.audit;

import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 审计事件发布器
 * 负责审计事件的异步发布、持久化和过滤
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
@Component
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class AuditEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AuditEventPublisher.class);

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    // 事件过滤器列表
    private final List<AuditEventFilter> eventFilters = new CopyOnWriteArrayList<>();

    // 事件监听器列表
    private final List<AuditEventListener> eventListeners = new CopyOnWriteArrayList<>();

    // 统计信息
    private final AtomicLong totalEvents = new AtomicLong(0);
    private final AtomicLong filteredEvents = new AtomicLong(0);
    private final AtomicLong publishedEvents = new AtomicLong(0);
    private final AtomicLong failedEvents = new AtomicLong(0);

    // Redis键前缀
    private static final String AUDIT_EVENT_PREFIX = "ps:security:audit:event:";
    private static final String AUDIT_QUEUE_KEY = "ps:security:audit:queue";
    private static final String AUDIT_STATS_KEY = "ps:security:audit:stats";

    @PostConstruct
    public void init() {
        // 添加默认过滤器
        addDefaultFilters();
        
        // 添加默认监听器
        addDefaultListeners();
        
        logger.info("AuditEventPublisher initialized with {} filters and {} listeners", 
            eventFilters.size(), eventListeners.size());
    }

    /**
     * 发布审计事件
     */
    public void publishEvent(AuditEvent event) {
        if (event == null) {
            return;
        }

        totalEvents.incrementAndGet();

        try {
            // 应用事件过滤器
            if (!shouldPublishEvent(event)) {
                filteredEvents.incrementAndGet();
                logger.debug("Audit event filtered: {}", event.getEventId());
                return;
            }

            // 异步发布事件
            publishEventAsync(event);
            
        } catch (Exception e) {
            failedEvents.incrementAndGet();
            logger.error("Failed to publish audit event: {}", event.getEventId(), e);
        }
    }

    /**
     * 异步发布事件
     */
    @Async
    public void publishEventAsync(AuditEvent event) {
        try {
            // 持久化事件
            persistEvent(event);
            
            // 通知监听器
            notifyListeners(event);
            
            // 发布Spring事件（如果可用）
            if (applicationEventPublisher != null) {
                applicationEventPublisher.publishEvent(new AuditEventPublishedEvent(event));
            }
            
            publishedEvents.incrementAndGet();
            logger.debug("Audit event published successfully: {}", event.getEventId());
            
        } catch (Exception e) {
            failedEvents.incrementAndGet();
            logger.error("Failed to process audit event: {}", event.getEventId(), e);
        }
    }

    /**
     * 批量发布事件
     */
    @Async
    public void publishEvents(List<AuditEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        logger.debug("Publishing {} audit events in batch", events.size());
        
        for (AuditEvent event : events) {
            publishEvent(event);
        }
    }

    /**
     * 添加事件过滤器
     */
    public void addEventFilter(AuditEventFilter filter) {
        if (filter != null) {
            eventFilters.add(filter);
            logger.debug("Added audit event filter: {}", filter.getClass().getSimpleName());
        }
    }

    /**
     * 移除事件过滤器
     */
    public void removeEventFilter(AuditEventFilter filter) {
        if (filter != null) {
            eventFilters.remove(filter);
            logger.debug("Removed audit event filter: {}", filter.getClass().getSimpleName());
        }
    }

    /**
     * 添加事件监听器
     */
    public void addEventListener(AuditEventListener listener) {
        if (listener != null) {
            eventListeners.add(listener);
            logger.debug("Added audit event listener: {}", listener.getClass().getSimpleName());
        }
    }

    /**
     * 移除事件监听器
     */
    public void removeEventListener(AuditEventListener listener) {
        if (listener != null) {
            eventListeners.remove(listener);
            logger.debug("Removed audit event listener: {}", listener.getClass().getSimpleName());
        }
    }

    /**
     * 获取发布统计信息
     */
    public AuditPublishStatistics getStatistics() {
        AuditPublishStatistics stats = new AuditPublishStatistics();
        stats.setTotalEvents(totalEvents.get());
        stats.setFilteredEvents(filteredEvents.get());
        stats.setPublishedEvents(publishedEvents.get());
        stats.setFailedEvents(failedEvents.get());
        stats.setActiveFilters(eventFilters.size());
        stats.setActiveListeners(eventListeners.size());
        
        if (totalEvents.get() > 0) {
            stats.setSuccessRate((double) publishedEvents.get() / totalEvents.get() * 100);
            stats.setFilterRate((double) filteredEvents.get() / totalEvents.get() * 100);
        }
        
        return stats;
    }

    /**
     * 清理过期的审计事件
     */
    public void cleanupExpiredEvents() {
        if (redisTemplate == null) {
            return;
        }

        try {
            // 这里可以实现清理逻辑
            logger.debug("Cleaning up expired audit events");
            
        } catch (Exception e) {
            logger.error("Failed to cleanup expired audit events", e);
        }
    }

    // ================================ 私有方法 ================================

    /**
     * 检查事件是否应该发布
     */
    private boolean shouldPublishEvent(AuditEvent event) {
        for (AuditEventFilter filter : eventFilters) {
            try {
                if (!filter.shouldPublish(event)) {
                    logger.debug("Event filtered by {}: {}", filter.getClass().getSimpleName(), event.getEventId());
                    return false;
                }
            } catch (Exception e) {
                logger.warn("Event filter {} threw exception", filter.getClass().getSimpleName(), e);
            }
        }
        return true;
    }

    /**
     * 持久化事件
     */
    private void persistEvent(AuditEvent event) {
        if (redisTemplate == null) {
            logger.debug("Redis not available, skipping event persistence");
            return;
        }

        try {
            // 将事件存储到Redis
            String eventKey = AUDIT_EVENT_PREFIX + event.getEventId();
            redisTemplate.opsForValue().set(eventKey, event, 30, TimeUnit.DAYS);
            
            // 将事件ID添加到队列（用于批量处理）
            redisTemplate.opsForList().leftPush(AUDIT_QUEUE_KEY, event.getEventId());
            
            // 限制队列长度
            redisTemplate.opsForList().trim(AUDIT_QUEUE_KEY, 0, 10000);
            
        } catch (Exception e) {
            logger.error("Failed to persist audit event: {}", event.getEventId(), e);
        }
    }

    /**
     * 通知监听器
     */
    private void notifyListeners(AuditEvent event) {
        for (AuditEventListener listener : eventListeners) {
            try {
                listener.onAuditEvent(event);
            } catch (Exception e) {
                logger.warn("Audit event listener {} threw exception", listener.getClass().getSimpleName(), e);
            }
        }
    }

    /**
     * 添加默认过滤器
     */
    private void addDefaultFilters() {
        // 级别过滤器
        addEventFilter(new LevelBasedFilter());
        
        // 重复事件过滤器
        addEventFilter(new DuplicateEventFilter());
        
        // 敏感信息过滤器
        addEventFilter(new SensitiveDataFilter());
    }

    /**
     * 添加默认监听器
     */
    private void addDefaultListeners() {
        // 日志监听器
        addEventListener(new LoggingEventListener());
        
        // 高风险事件监听器
        addEventListener(new HighRiskEventListener());
    }

    // ================================ 内部类定义 ================================

    /**
     * 审计事件过滤器接口
     */
    public interface AuditEventFilter {
        boolean shouldPublish(AuditEvent event);
    }

    /**
     * 审计事件监听器接口
     */
    public interface AuditEventListener {
        void onAuditEvent(AuditEvent event);
    }

    /**
     * 基于级别的过滤器
     */
    private static class LevelBasedFilter implements AuditEventFilter {
        @Override
        public boolean shouldPublish(AuditEvent event) {
            // 可以根据配置过滤低级别事件
            return event.getLevel() != AuditLevel.LOW || 
                   Math.random() < 0.1; // 10%的低级别事件被记录
        }
    }

    /**
     * 重复事件过滤器
     */
    private static class DuplicateEventFilter implements AuditEventFilter {
        // 简化实现，实际应该使用缓存来检测重复
        @Override
        public boolean shouldPublish(AuditEvent event) {
            return true; // 暂时不过滤重复事件
        }
    }

    /**
     * 敏感信息过滤器
     */
    private static class SensitiveDataFilter implements AuditEventFilter {
        @Override
        public boolean shouldPublish(AuditEvent event) {
            // 检查并清理敏感信息
            if (StringUtils.hasText(event.getDescription())) {
                // 简单的敏感信息脱敏
                String description = event.getDescription();
                description = description.replaceAll("password=\\w+", "password=***");
                description = description.replaceAll("token=\\w+", "token=***");
                event.setDescription(description);
            }
            return true;
        }
    }

    /**
     * 日志监听器
     */
    private static class LoggingEventListener implements AuditEventListener {
        private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
        
        @Override
        public void onAuditEvent(AuditEvent event) {
            String logMessage = String.format("[%s] %s", event.getEventType().name(), event.getSummary());
            
            switch (event.getLevel()) {
                case CRITICAL:
                    auditLogger.error(logMessage);
                    break;
                case HIGH:
                    auditLogger.warn(logMessage);
                    break;
                case MEDIUM:
                    auditLogger.info(logMessage);
                    break;
                default:
                    auditLogger.debug(logMessage);
                    break;
            }
        }
    }

    /**
     * 高风险事件监听器
     */
    private static class HighRiskEventListener implements AuditEventListener {
        private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY");
        
        @Override
        public void onAuditEvent(AuditEvent event) {
            if (event.requiresRealTimeNotification()) {
                securityLogger.error("HIGH RISK SECURITY EVENT: {}", event.getSummary());
                // 这里可以集成告警系统，如发送邮件、短信等
            }
        }
    }

    /**
     * 审计事件发布统计
     */
    public static class AuditPublishStatistics {
        private long totalEvents;
        private long filteredEvents;
        private long publishedEvents;
        private long failedEvents;
        private int activeFilters;
        private int activeListeners;
        private double successRate;
        private double filterRate;

        // Getters and setters
        public long getTotalEvents() { return totalEvents; }
        public void setTotalEvents(long totalEvents) { this.totalEvents = totalEvents; }

        public long getFilteredEvents() { return filteredEvents; }
        public void setFilteredEvents(long filteredEvents) { this.filteredEvents = filteredEvents; }

        public long getPublishedEvents() { return publishedEvents; }
        public void setPublishedEvents(long publishedEvents) { this.publishedEvents = publishedEvents; }

        public long getFailedEvents() { return failedEvents; }
        public void setFailedEvents(long failedEvents) { this.failedEvents = failedEvents; }

        public int getActiveFilters() { return activeFilters; }
        public void setActiveFilters(int activeFilters) { this.activeFilters = activeFilters; }

        public int getActiveListeners() { return activeListeners; }
        public void setActiveListeners(int activeListeners) { this.activeListeners = activeListeners; }

        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }

        public double getFilterRate() { return filterRate; }
        public void setFilterRate(double filterRate) { this.filterRate = filterRate; }
    }

    /**
     * 审计事件发布完成事件
     */
    public static class AuditEventPublishedEvent {
        private final AuditEvent auditEvent;

        public AuditEventPublishedEvent(AuditEvent auditEvent) {
            this.auditEvent = auditEvent;
        }

        public AuditEvent getAuditEvent() {
            return auditEvent;
        }
    }
}