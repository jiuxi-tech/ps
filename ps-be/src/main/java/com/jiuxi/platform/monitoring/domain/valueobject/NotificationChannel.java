package com.jiuxi.platform.monitoring.domain.valueobject;

import java.util.Map;

/**
 * 通知渠道值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class NotificationChannel {
    
    private String channelId;
    private String channelName;
    private String channelType; // EMAIL, SMS, WEBHOOK
    private String target; // 邮箱地址、手机号、Webhook URL
    private boolean enabled;
    private Map<String, Object> configuration;
    
    public NotificationChannel() {
    }
    
    public NotificationChannel(String channelId, String channelName, String channelType, String target) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelType = channelType;
        this.target = target;
        this.enabled = true;
    }
    
    /**
     * 检查渠道是否有效
     */
    public boolean isValid() {
        return channelId != null && !channelId.trim().isEmpty() &&
               channelType != null && !channelType.trim().isEmpty() &&
               target != null && !target.trim().isEmpty();
    }
    
    /**
     * 检查是否为邮件渠道
     */
    public boolean isEmailChannel() {
        return "EMAIL".equalsIgnoreCase(channelType);
    }
    
    /**
     * 检查是否为短信渠道
     */
    public boolean isSmsChannel() {
        return "SMS".equalsIgnoreCase(channelType);
    }
    
    /**
     * 检查是否为Webhook渠道
     */
    public boolean isWebhookChannel() {
        return "WEBHOOK".equalsIgnoreCase(channelType);
    }
    
    // Getters and Setters
    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    
    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    
    public String getChannelType() { return channelType; }
    public void setChannelType(String channelType) { this.channelType = channelType; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public Map<String, Object> getConfiguration() { return configuration; }
    public void setConfiguration(Map<String, Object> configuration) { this.configuration = configuration; }
    
    @Override
    public String toString() {
        return String.format("NotificationChannel{channelId='%s', channelName='%s', channelType='%s', target='%s', enabled=%s}", 
                channelId, channelName, channelType, target, enabled);
    }
}