package com.jiuxi.shared.security.audit;

import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 审计日志服务
 * 提供审计日志的查询、统计、归档等功能
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
@Service
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    // Redis键前缀
    private static final String AUDIT_EVENT_PREFIX = "ps:security:audit:event:";
    private static final String AUDIT_QUEUE_KEY = "ps:security:audit:queue";
    private static final String AUDIT_STATS_KEY = "ps:security:audit:stats";
    private static final String AUDIT_INDEX_PREFIX = "ps:security:audit:index:";

    /**
     * 根据条件查询审计事件
     */
    public List<AuditEvent> queryAuditEvents(AuditQueryCriteria criteria) {
        if (redisTemplate == null) {
            logger.warn("Redis not available, cannot query audit events");
            return Collections.emptyList();
        }

        try {
            List<String> eventIds = getEventIdsByCriteria(criteria);
            List<AuditEvent> events = new ArrayList<>();

            for (String eventId : eventIds) {
                String eventKey = AUDIT_EVENT_PREFIX + eventId;
                AuditEvent event = (AuditEvent) redisTemplate.opsForValue().get(eventKey);
                if (event != null && matchesCriteria(event, criteria)) {
                    events.add(event);
                }
            }

            // 排序
            events.sort((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()));

            // 分页
            int startIndex = criteria.getOffset();
            int endIndex = Math.min(startIndex + criteria.getLimit(), events.size());
            
            if (startIndex >= events.size()) {
                return Collections.emptyList();
            }

            return events.subList(startIndex, endIndex);

        } catch (Exception e) {
            logger.error("Failed to query audit events", e);
            return Collections.emptyList();
        }
    }

    /**
     * 根据事件ID获取审计事件
     */
    public AuditEvent getAuditEvent(String eventId) {
        if (redisTemplate == null || !StringUtils.hasText(eventId)) {
            return null;
        }

        try {
            String eventKey = AUDIT_EVENT_PREFIX + eventId;
            return (AuditEvent) redisTemplate.opsForValue().get(eventKey);
        } catch (Exception e) {
            logger.error("Failed to get audit event: {}", eventId, e);
            return null;
        }
    }

    /**
     * 统计审计事件
     */
    public AuditStatistics getAuditStatistics(AuditQueryCriteria criteria) {
        if (redisTemplate == null) {
            logger.warn("Redis not available, cannot get audit statistics");
            return new AuditStatistics();
        }

        try {
            AuditStatistics statistics = new AuditStatistics();
            
            // 获取匹配条件的事件
            List<AuditEvent> events = queryAllEventsByCriteria(criteria);
            
            statistics.setTotalEvents(events.size());
            
            // 按类别统计
            Map<AuditCategory, Long> categoryStats = events.stream()
                .collect(Collectors.groupingBy(AuditEvent::getCategory, Collectors.counting()));
            statistics.setCategoryStatistics(categoryStats);
            
            // 按级别统计
            Map<AuditLevel, Long> levelStats = events.stream()
                .collect(Collectors.groupingBy(AuditEvent::getLevel, Collectors.counting()));
            statistics.setLevelStatistics(levelStats);
            
            // 按结果统计
            Map<AuditResult, Long> resultStats = events.stream()
                .collect(Collectors.groupingBy(AuditEvent::getResult, Collectors.counting()));
            statistics.setResultStatistics(resultStats);
            
            // 按用户统计
            Map<String, Long> userStats = events.stream()
                .filter(event -> StringUtils.hasText(event.getUserId()))
                .collect(Collectors.groupingBy(AuditEvent::getUserId, Collectors.counting()));
            statistics.setTopUsers(getTopEntries(userStats, 10));
            
            // 按IP统计
            Map<String, Long> ipStats = events.stream()
                .filter(event -> StringUtils.hasText(event.getClientIp()))
                .collect(Collectors.groupingBy(AuditEvent::getClientIp, Collectors.counting()));
            statistics.setTopIpAddresses(getTopEntries(ipStats, 10));
            
            // 时间范围
            if (!events.isEmpty()) {
                statistics.setStartTime(events.stream()
                    .map(AuditEvent::getTimestamp)
                    .min(LocalDateTime::compareTo)
                    .orElse(null));
                statistics.setEndTime(events.stream()
                    .map(AuditEvent::getTimestamp)
                    .max(LocalDateTime::compareTo)
                    .orElse(null));
            }
            
            return statistics;

        } catch (Exception e) {
            logger.error("Failed to get audit statistics", e);
            return new AuditStatistics();
        }
    }

    /**
     * 归档过期的审计事件
     */
    public int archiveExpiredEvents(int retentionDays) {
        if (redisTemplate == null || retentionDays <= 0) {
            return 0;
        }

        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusDays(retentionDays);
            int archivedCount = 0;

            // 获取所有事件ID
            List<String> eventIds = redisTemplate.opsForList().range(AUDIT_QUEUE_KEY, 0, -1)
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());

            for (String eventId : eventIds) {
                String eventKey = AUDIT_EVENT_PREFIX + eventId;
                AuditEvent event = (AuditEvent) redisTemplate.opsForValue().get(eventKey);
                
                if (event != null && event.getTimestamp().isBefore(cutoffTime)) {
                    // 归档事件（这里简化为删除，实际可以移动到归档存储）
                    redisTemplate.delete(eventKey);
                    redisTemplate.opsForList().remove(AUDIT_QUEUE_KEY, 1, eventId);
                    archivedCount++;
                    
                    logger.debug("Archived audit event: {}", eventId);
                }
            }

            logger.info("Archived {} expired audit events older than {} days", archivedCount, retentionDays);
            return archivedCount;

        } catch (Exception e) {
            logger.error("Failed to archive expired audit events", e);
            return 0;
        }
    }

    /**
     * 清理所有审计数据
     */
    public void clearAllAuditData() {
        if (redisTemplate == null) {
            return;
        }

        try {
            // 获取所有审计相关的键
            Set<String> auditKeys = redisTemplate.keys("ps:security:audit:*");
            if (auditKeys != null && !auditKeys.isEmpty()) {
                redisTemplate.delete(auditKeys);
                logger.info("Cleared {} audit data keys", auditKeys.size());
            }
        } catch (Exception e) {
            logger.error("Failed to clear audit data", e);
        }
    }

    /**
     * 获取审计事件总数
     */
    public long getTotalEventCount() {
        if (redisTemplate == null) {
            return 0;
        }

        try {
            Long queueSize = redisTemplate.opsForList().size(AUDIT_QUEUE_KEY);
            return queueSize != null ? queueSize : 0;
        } catch (Exception e) {
            logger.error("Failed to get total event count", e);
            return 0;
        }
    }

    /**
     * 检查审计系统健康状态
     */
    public AuditHealthStatus getHealthStatus() {
        AuditHealthStatus status = new AuditHealthStatus();
        
        try {
            // 检查Redis连接
            if (redisTemplate != null) {
                redisTemplate.opsForValue().get("ps:security:audit:health:check");
                status.setRedisAvailable(true);
            } else {
                status.setRedisAvailable(false);
            }
            
            // 检查事件队列大小
            long queueSize = getTotalEventCount();
            status.setQueueSize(queueSize);
            status.setQueueHealthy(queueSize < 100000); // 队列超过10万条记录认为不健康
            
            // 检查最近是否有事件
            List<AuditEvent> recentEvents = queryAuditEvents(
                AuditQueryCriteria.builder()
                    .startTime(LocalDateTime.now().minusHours(1))
                    .limit(1)
                    .build()
            );
            status.setRecentActivity(!recentEvents.isEmpty());
            
            // 总体健康状态
            status.setHealthy(status.isRedisAvailable() && status.isQueueHealthy());
            
        } catch (Exception e) {
            logger.error("Failed to check audit health status", e);
            status.setHealthy(false);
            status.setErrorMessage(e.getMessage());
        }
        
        return status;
    }

    // ================================ 私有方法 ================================

    /**
     * 根据条件获取事件ID列表
     */
    private List<String> getEventIdsByCriteria(AuditQueryCriteria criteria) {
        // 从队列获取所有事件ID
        List<Object> allEventIds = redisTemplate.opsForList().range(AUDIT_QUEUE_KEY, 0, -1);
        if (allEventIds == null) {
            return Collections.emptyList();
        }

        return allEventIds.stream()
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    /**
     * 查询所有匹配条件的事件（不分页）
     */
    private List<AuditEvent> queryAllEventsByCriteria(AuditQueryCriteria criteria) {
        List<String> eventIds = getEventIdsByCriteria(criteria);
        List<AuditEvent> events = new ArrayList<>();

        for (String eventId : eventIds) {
            String eventKey = AUDIT_EVENT_PREFIX + eventId;
            AuditEvent event = (AuditEvent) redisTemplate.opsForValue().get(eventKey);
            if (event != null && matchesCriteria(event, criteria)) {
                events.add(event);
            }
        }

        return events;
    }

    /**
     * 检查事件是否匹配查询条件
     */
    private boolean matchesCriteria(AuditEvent event, AuditQueryCriteria criteria) {
        if (criteria == null) {
            return true;
        }

        // 时间范围检查
        if (criteria.getStartTime() != null && event.getTimestamp().isBefore(criteria.getStartTime())) {
            return false;
        }
        if (criteria.getEndTime() != null && event.getTimestamp().isAfter(criteria.getEndTime())) {
            return false;
        }

        // 用户ID检查
        if (StringUtils.hasText(criteria.getUserId()) && 
            !criteria.getUserId().equals(event.getUserId())) {
            return false;
        }

        // 事件类型检查
        if (criteria.getEventType() != null && 
            !criteria.getEventType().equals(event.getEventType())) {
            return false;
        }

        // 类别检查
        if (criteria.getCategory() != null && 
            !criteria.getCategory().equals(event.getCategory())) {
            return false;
        }

        // 级别检查
        if (criteria.getLevel() != null && 
            !criteria.getLevel().equals(event.getLevel())) {
            return false;
        }

        // 结果检查
        if (criteria.getResult() != null && 
            !criteria.getResult().equals(event.getResult())) {
            return false;
        }

        // IP地址检查
        if (StringUtils.hasText(criteria.getClientIp()) && 
            !criteria.getClientIp().equals(event.getClientIp())) {
            return false;
        }

        // 关键词搜索
        if (StringUtils.hasText(criteria.getKeyword())) {
            String keyword = criteria.getKeyword().toLowerCase();
            String summary = event.getSummary() != null ? event.getSummary().toLowerCase() : "";
            String description = event.getDescription() != null ? event.getDescription().toLowerCase() : "";
            
            if (!summary.contains(keyword) && !description.contains(keyword)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取统计结果的前N项
     */
    private Map<String, Long> getTopEntries(Map<String, Long> stats, int limit) {
        return stats.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    // ================================ 内部类定义 ================================

    /**
     * 审计查询条件
     */
    public static class AuditQueryCriteria {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String userId;
        private AuditEventType eventType;
        private AuditCategory category;
        private AuditLevel level;
        private AuditResult result;
        private String clientIp;
        private String keyword;
        private int offset = 0;
        private int limit = 100;

        // Builder模式
        public static AuditQueryCriteriaBuilder builder() {
            return new AuditQueryCriteriaBuilder();
        }

        public static class AuditQueryCriteriaBuilder {
            private AuditQueryCriteria criteria = new AuditQueryCriteria();

            public AuditQueryCriteriaBuilder startTime(LocalDateTime startTime) {
                criteria.startTime = startTime;
                return this;
            }

            public AuditQueryCriteriaBuilder endTime(LocalDateTime endTime) {
                criteria.endTime = endTime;
                return this;
            }

            public AuditQueryCriteriaBuilder userId(String userId) {
                criteria.userId = userId;
                return this;
            }

            public AuditQueryCriteriaBuilder eventType(AuditEventType eventType) {
                criteria.eventType = eventType;
                return this;
            }

            public AuditQueryCriteriaBuilder category(AuditCategory category) {
                criteria.category = category;
                return this;
            }

            public AuditQueryCriteriaBuilder level(AuditLevel level) {
                criteria.level = level;
                return this;
            }

            public AuditQueryCriteriaBuilder result(AuditResult result) {
                criteria.result = result;
                return this;
            }

            public AuditQueryCriteriaBuilder clientIp(String clientIp) {
                criteria.clientIp = clientIp;
                return this;
            }

            public AuditQueryCriteriaBuilder keyword(String keyword) {
                criteria.keyword = keyword;
                return this;
            }

            public AuditQueryCriteriaBuilder offset(int offset) {
                criteria.offset = offset;
                return this;
            }

            public AuditQueryCriteriaBuilder limit(int limit) {
                criteria.limit = limit;
                return this;
            }

            public AuditQueryCriteria build() {
                return criteria;
            }
        }

        // Getters
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public String getUserId() { return userId; }
        public AuditEventType getEventType() { return eventType; }
        public AuditCategory getCategory() { return category; }
        public AuditLevel getLevel() { return level; }
        public AuditResult getResult() { return result; }
        public String getClientIp() { return clientIp; }
        public String getKeyword() { return keyword; }
        public int getOffset() { return offset; }
        public int getLimit() { return limit; }
    }

    /**
     * 审计统计信息
     */
    public static class AuditStatistics {
        private long totalEvents;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Map<AuditCategory, Long> categoryStatistics = new HashMap<>();
        private Map<AuditLevel, Long> levelStatistics = new HashMap<>();
        private Map<AuditResult, Long> resultStatistics = new HashMap<>();
        private Map<String, Long> topUsers = new HashMap<>();
        private Map<String, Long> topIpAddresses = new HashMap<>();

        // Getters and setters
        public long getTotalEvents() { return totalEvents; }
        public void setTotalEvents(long totalEvents) { this.totalEvents = totalEvents; }

        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

        public Map<AuditCategory, Long> getCategoryStatistics() { return categoryStatistics; }
        public void setCategoryStatistics(Map<AuditCategory, Long> categoryStatistics) { 
            this.categoryStatistics = categoryStatistics; 
        }

        public Map<AuditLevel, Long> getLevelStatistics() { return levelStatistics; }
        public void setLevelStatistics(Map<AuditLevel, Long> levelStatistics) { 
            this.levelStatistics = levelStatistics; 
        }

        public Map<AuditResult, Long> getResultStatistics() { return resultStatistics; }
        public void setResultStatistics(Map<AuditResult, Long> resultStatistics) { 
            this.resultStatistics = resultStatistics; 
        }

        public Map<String, Long> getTopUsers() { return topUsers; }
        public void setTopUsers(Map<String, Long> topUsers) { this.topUsers = topUsers; }

        public Map<String, Long> getTopIpAddresses() { return topIpAddresses; }
        public void setTopIpAddresses(Map<String, Long> topIpAddresses) { 
            this.topIpAddresses = topIpAddresses; 
        }
    }

    /**
     * 审计系统健康状态
     */
    public static class AuditHealthStatus {
        private boolean healthy;
        private boolean redisAvailable;
        private boolean queueHealthy;
        private boolean recentActivity;
        private long queueSize;
        private String errorMessage;

        // Getters and setters
        public boolean isHealthy() { return healthy; }
        public void setHealthy(boolean healthy) { this.healthy = healthy; }

        public boolean isRedisAvailable() { return redisAvailable; }
        public void setRedisAvailable(boolean redisAvailable) { this.redisAvailable = redisAvailable; }

        public boolean isQueueHealthy() { return queueHealthy; }
        public void setQueueHealthy(boolean queueHealthy) { this.queueHealthy = queueHealthy; }

        public boolean isRecentActivity() { return recentActivity; }
        public void setRecentActivity(boolean recentActivity) { this.recentActivity = recentActivity; }

        public long getQueueSize() { return queueSize; }
        public void setQueueSize(long queueSize) { this.queueSize = queueSize; }

        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}