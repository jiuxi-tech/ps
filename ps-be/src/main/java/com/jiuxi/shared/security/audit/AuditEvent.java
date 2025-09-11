package com.jiuxi.shared.security.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 安全审计事件模型
 * 记录系统中的所有安全相关操作和事件
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
public class AuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件唯一标识
     */
    private String eventId;

    /**
     * 事件类型
     */
    private AuditEventType eventType;

    /**
     * 事件分类
     */
    private AuditCategory category;

    /**
     * 事件级别
     */
    private AuditLevel level;

    /**
     * 事件标题
     */
    private String title;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * User-Agent
     */
    private String userAgent;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * 操作结果
     */
    private AuditResult result;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 事件发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 事件持续时间（毫秒）
     */
    private Long duration;

    /**
     * 扩展属性
     */
    private Map<String, Object> attributes;

    /**
     * 关联的资源ID
     */
    private String resourceId;

    /**
     * 关联的资源类型
     */
    private String resourceType;

    /**
     * 操作前的数据（敏感信息已脱敏）
     */
    private String beforeData;

    /**
     * 操作后的数据（敏感信息已脱敏）
     */
    private String afterData;

    /**
     * 默认构造函数
     */
    public AuditEvent() {
        this.eventId = UUID.randomUUID().toString().replace("-", "");
        this.timestamp = LocalDateTime.now();
        this.attributes = new HashMap<>();
    }

    /**
     * 构造函数
     */
    public AuditEvent(AuditEventType eventType, String title, String description) {
        this();
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.category = eventType.getCategory();
        this.level = eventType.getDefaultLevel();
    }

    /**
     * 创建认证事件
     */
    public static AuditEvent createAuthEvent(AuditEventType eventType, String username, String clientIp) {
        AuditEvent event = new AuditEvent(eventType, eventType.getDisplayName(), eventType.getDescription());
        event.setUsername(username);
        event.setClientIp(clientIp);
        return event;
    }

    /**
     * 创建授权事件
     */
    public static AuditEvent createAuthzEvent(AuditEventType eventType, String userId, String resourceId, String resourceType) {
        AuditEvent event = new AuditEvent(eventType, eventType.getDisplayName(), eventType.getDescription());
        event.setUserId(userId);
        event.setResourceId(resourceId);
        event.setResourceType(resourceType);
        return event;
    }

    /**
     * 创建数据操作事件
     */
    public static AuditEvent createDataEvent(AuditEventType eventType, String userId, String resourceId, String resourceType) {
        AuditEvent event = new AuditEvent(eventType, eventType.getDisplayName(), eventType.getDescription());
        event.setUserId(userId);
        event.setResourceId(resourceId);
        event.setResourceType(resourceType);
        return event;
    }

    /**
     * 创建系统事件
     */
    public static AuditEvent createSystemEvent(AuditEventType eventType, String description) {
        AuditEvent event = new AuditEvent(eventType, eventType.getDisplayName(), description);
        return event;
    }

    /**
     * 设置成功结果
     */
    public AuditEvent success() {
        this.result = AuditResult.SUCCESS;
        return this;
    }

    /**
     * 设置失败结果
     */
    public AuditEvent failure(String errorCode, String errorMessage) {
        this.result = AuditResult.FAILURE;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * 设置警告结果
     */
    public AuditEvent warning(String message) {
        this.result = AuditResult.WARNING;
        this.errorMessage = message;
        return this;
    }

    /**
     * 添加属性
     */
    public AuditEvent addAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
        return this;
    }

    /**
     * 设置请求信息
     */
    public AuditEvent setRequestInfo(String requestUri, String httpMethod, String userAgent) {
        this.requestUri = requestUri;
        this.httpMethod = httpMethod;
        this.userAgent = userAgent;
        return this;
    }

    /**
     * 设置用户信息
     */
    public AuditEvent setUserInfo(String userId, String username, String sessionId) {
        this.userId = userId;
        this.username = username;
        this.sessionId = sessionId;
        return this;
    }

    /**
     * 设置数据变更信息
     */
    public AuditEvent setDataChange(String beforeData, String afterData) {
        this.beforeData = beforeData;
        this.afterData = afterData;
        return this;
    }

    /**
     * 设置持续时间
     */
    public AuditEvent setDuration(long startTime) {
        this.duration = System.currentTimeMillis() - startTime;
        return this;
    }

    /**
     * 转换为JSON字符串
     */
    @JsonIgnore
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return String.format("{\"eventId\":\"%s\",\"error\":\"JSON serialization failed: %s\"}", 
                eventId, e.getMessage());
        }
    }

    /**
     * 从JSON字符串创建事件
     */
    public static AuditEvent fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, AuditEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize audit event from JSON", e);
        }
    }

    /**
     * 获取事件摘要信息
     */
    @JsonIgnore
    public String getSummary() {
        return String.format("[%s] %s - %s (User: %s, IP: %s, Result: %s)", 
            category.name(), title, description, 
            username != null ? username : "N/A", 
            clientIp != null ? clientIp : "N/A", 
            result != null ? result.name() : "N/A");
    }

    /**
     * 检查是否为敏感事件
     */
    @JsonIgnore
    public boolean isSensitive() {
        return level == AuditLevel.HIGH || level == AuditLevel.CRITICAL;
    }

    /**
     * 检查是否需要实时通知
     */
    @JsonIgnore
    public boolean requiresRealTimeNotification() {
        return level == AuditLevel.CRITICAL || 
               (level == AuditLevel.HIGH && result == AuditResult.FAILURE);
    }

    // Getters and setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public AuditEventType getEventType() { return eventType; }
    public void setEventType(AuditEventType eventType) { this.eventType = eventType; }

    public AuditCategory getCategory() { return category; }
    public void setCategory(AuditCategory category) { this.category = category; }

    public AuditLevel getLevel() { return level; }
    public void setLevel(AuditLevel level) { this.level = level; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getRequestUri() { return requestUri; }
    public void setRequestUri(String requestUri) { this.requestUri = requestUri; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public AuditResult getResult() { return result; }
    public void setResult(AuditResult result) { this.result = result; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getDuration() { return duration; }
    public void setDuration(Long duration) { this.duration = duration; }

    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getBeforeData() { return beforeData; }
    public void setBeforeData(String beforeData) { this.beforeData = beforeData; }

    public String getAfterData() { return afterData; }
    public void setAfterData(String afterData) { this.afterData = afterData; }

    @Override
    public String toString() {
        return getSummary();
    }
}