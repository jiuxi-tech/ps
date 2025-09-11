package com.jiuxi.platform.monitoring.app.service;

import com.jiuxi.platform.monitoring.domain.entity.Alert;
import com.jiuxi.platform.monitoring.domain.service.AlertService;
import com.jiuxi.platform.monitoring.domain.service.NotificationService;
import com.jiuxi.platform.monitoring.domain.valueobject.NotificationChannel;
import com.jiuxi.platform.monitoring.infrastructure.notification.EmailNotificationService;
import com.jiuxi.platform.monitoring.infrastructure.notification.SmsNotificationService;
import com.jiuxi.platform.monitoring.infrastructure.notification.WebhookNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通知管理服务
 * 统一管理所有通知渠道和通知发送
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class NotificationManagerService {
    
    private final AlertService alertService;
    private final Map<String, NotificationService> notificationServices;
    
    // 通知渠道配置
    private final Map<String, List<NotificationChannel>> instanceChannels = new ConcurrentHashMap<>();
    private final Map<String, NotificationChannel> globalChannels = new ConcurrentHashMap<>();
    
    // 通知统计
    private long totalNotificationsSent = 0;
    private long totalNotificationsFailed = 0;
    private final Map<String, Long> notificationStats = new ConcurrentHashMap<>();
    
    @Autowired
    public NotificationManagerService(AlertService alertService,
                                    EmailNotificationService emailService,
                                    SmsNotificationService smsService,
                                    WebhookNotificationService webhookService) {
        this.alertService = alertService;
        
        // 注册通知服务
        this.notificationServices = new HashMap<>();
        this.notificationServices.put("EMAIL", emailService);
        this.notificationServices.put("SMS", smsService);
        this.notificationServices.put("WEBHOOK", webhookService);
        
        // 初始化默认通知渠道
        initializeDefaultChannels();
    }
    
    /**
     * 定期检查并发送告警通知
     */
    @Scheduled(fixedRate = 30000) // 每30秒检查一次
    public void processAlertNotifications() {
        try {
            List<Alert> alertsNeedingNotification = alertService.getAlertsNeedingNotification();
            
            for (Alert alert : alertsNeedingNotification) {
                sendAlertNotification(alert);
                alertService.recordAlertNotification(alert.getAlertId());
            }
            
        } catch (Exception e) {
            System.err.println("处理告警通知时出错: " + e.getMessage());
        }
    }
    
    /**
     * 发送告警通知
     */
    public boolean sendAlertNotification(Alert alert) {
        List<NotificationChannel> channels = getNotificationChannels(alert.getInstanceId(), alert.getSeverity());
        boolean success = false;
        
        for (NotificationChannel channel : channels) {
            if (channel.isEnabled()) {
                boolean result = sendNotification(alert, channel, "alert");
                if (result) {
                    success = true;
                    updateNotificationStats(channel.getChannelType(), true);
                } else {
                    updateNotificationStats(channel.getChannelType(), false);
                }
            }
        }
        
        return success;
    }
    
    /**
     * 发送告警恢复通知
     */
    public boolean sendAlertResolvedNotification(Alert alert) {
        List<NotificationChannel> channels = getNotificationChannels(alert.getInstanceId(), alert.getSeverity());
        boolean success = false;
        
        for (NotificationChannel channel : channels) {
            if (channel.isEnabled()) {
                boolean result = sendNotification(alert, channel, "resolved");
                if (result) {
                    success = true;
                    updateNotificationStats(channel.getChannelType(), true);
                } else {
                    updateNotificationStats(channel.getChannelType(), false);
                }
            }
        }
        
        return success;
    }
    
    /**
     * 发送通知
     */
    private boolean sendNotification(Alert alert, NotificationChannel channel, String type) {
        try {
            NotificationService service = notificationServices.get(channel.getChannelType());
            if (service == null || !service.isAvailable()) {
                return false;
            }
            
            boolean result;
            if ("resolved".equals(type)) {
                result = service.sendAlertResolvedNotification(alert, channel);
            } else {
                result = service.sendAlertNotification(alert, channel);
            }
            
            // 记录通知日志
            logNotification(alert, channel, type, result);
            
            return result;
            
        } catch (Exception e) {
            System.err.println("发送通知失败: " + e.getMessage());
            logNotification(alert, channel, type, false);
            return false;
        }
    }
    
    /**
     * 记录通知日志
     */
    private void logNotification(Alert alert, NotificationChannel channel, String type, boolean success) {
        String status = success ? "成功" : "失败";
        System.out.printf("[通知日志] %s - %s通知%s - 渠道: %s(%s) - 告警: %s%n", 
                LocalDateTime.now().toString(), type, status, 
                channel.getChannelName(), channel.getChannelType(), alert.getRuleName());
    }
    
    /**
     * 获取通知渠道
     */
    private List<NotificationChannel> getNotificationChannels(String instanceId, String severity) {
        List<NotificationChannel> channels = new ArrayList<>();
        
        // 获取实例特定的渠道
        List<NotificationChannel> instanceSpecific = instanceChannels.get(instanceId);
        if (instanceSpecific != null) {
            channels.addAll(instanceSpecific);
        }
        
        // 获取全局渠道
        for (NotificationChannel channel : globalChannels.values()) {
            if (shouldUseChannelForSeverity(channel, severity)) {
                channels.add(channel);
            }
        }
        
        return channels;
    }
    
    /**
     * 检查渠道是否应该用于指定严重性
     */
    private boolean shouldUseChannelForSeverity(NotificationChannel channel, String severity) {
        // 如果渠道配置中指定了严重性过滤器
        if (channel.getConfiguration() != null) {
            Object severities = channel.getConfiguration().get("severities");
            if (severities instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> allowedSeverities = (List<String>) severities;
                return allowedSeverities.contains(severity);
            }
        }
        
        // 默认情况下，根据严重性决定使用哪些渠道
        switch (severity.toUpperCase()) {
            case "CRITICAL":
                return true; // CRITICAL级别使用所有渠道
            case "HIGH":
                return !"SMS".equals(channel.getChannelType()); // HIGH级别不使用短信
            case "MEDIUM":
                return "EMAIL".equals(channel.getChannelType()) || "WEBHOOK".equals(channel.getChannelType());
            case "LOW":
                return "WEBHOOK".equals(channel.getChannelType());
            default:
                return false;
        }
    }
    
    /**
     * 添加全局通知渠道
     */
    public void addGlobalNotificationChannel(NotificationChannel channel) {
        if (channel != null && channel.isValid()) {
            globalChannels.put(channel.getChannelId(), channel);
        }
    }
    
    /**
     * 添加实例特定的通知渠道
     */
    public void addInstanceNotificationChannel(String instanceId, NotificationChannel channel) {
        if (instanceId != null && channel != null && channel.isValid()) {
            instanceChannels.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(channel);
        }
    }
    
    /**
     * 移除通知渠道
     */
    public void removeNotificationChannel(String channelId) {
        globalChannels.remove(channelId);
        
        for (List<NotificationChannel> channels : instanceChannels.values()) {
            channels.removeIf(channel -> channelId.equals(channel.getChannelId()));
        }
    }
    
    /**
     * 更新通知统计
     */
    private void updateNotificationStats(String channelType, boolean success) {
        if (success) {
            totalNotificationsSent++;
            notificationStats.merge(channelType + "_success", 1L, Long::sum);
        } else {
            totalNotificationsFailed++;
            notificationStats.merge(channelType + "_failed", 1L, Long::sum);
        }
    }
    
    /**
     * 获取通知统计信息
     */
    public Map<String, Object> getNotificationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSent", totalNotificationsSent);
        stats.put("totalFailed", totalNotificationsFailed);
        stats.put("successRate", totalNotificationsSent + totalNotificationsFailed > 0 ? 
                (double) totalNotificationsSent / (totalNotificationsSent + totalNotificationsFailed) * 100 : 0);
        
        // 按渠道类型统计
        Map<String, Object> byChannel = new HashMap<>();
        for (String serviceType : notificationServices.keySet()) {
            Map<String, Object> channelStats = new HashMap<>();
            channelStats.put("success", notificationStats.getOrDefault(serviceType + "_success", 0L));
            channelStats.put("failed", notificationStats.getOrDefault(serviceType + "_failed", 0L));
            byChannel.put(serviceType, channelStats);
        }
        stats.put("byChannel", byChannel);
        
        // 渠道配置统计
        stats.put("globalChannels", globalChannels.size());
        stats.put("instanceChannels", instanceChannels.size());
        
        return stats;
    }
    
    /**
     * 测试通知渠道
     */
    public boolean testNotificationChannel(String channelId) {
        NotificationChannel channel = globalChannels.get(channelId);
        if (channel == null) {
            return false;
        }
        
        NotificationService service = notificationServices.get(channel.getChannelType());
        if (service == null || !service.isAvailable()) {
            return false;
        }
        
        // 发送测试通知
        try {
            // 创建测试告警
            Alert testAlert = createTestAlert();
            return sendNotification(testAlert, channel, "test");
        } catch (Exception e) {
            System.err.println("测试通知渠道失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 创建测试告警
     */
    private Alert createTestAlert() {
        Alert testAlert = new Alert();
        testAlert.setAlertId("test-alert-" + System.currentTimeMillis());
        testAlert.setRuleName("测试告警规则");
        testAlert.setInstanceId("test-instance");
        testAlert.setMetricName("test.metric");
        testAlert.setMetricValue(99.0);
        testAlert.setThreshold(80.0);
        testAlert.setCondition(">");
        testAlert.setSeverity("MEDIUM");
        testAlert.setMessage("这是一个测试告警通知，用于验证通知渠道配置是否正确。");
        testAlert.setFireTime(LocalDateTime.now());
        return testAlert;
    }
    
    /**
     * 初始化默认通知渠道
     */
    private void initializeDefaultChannels() {
        // 默认邮件通知渠道
        NotificationChannel emailChannel = new NotificationChannel(
                "default-email", "默认邮件通知", "EMAIL", "admin@example.com");
        emailChannel.setConfiguration(Map.of("severities", Arrays.asList("CRITICAL", "HIGH", "MEDIUM")));
        addGlobalNotificationChannel(emailChannel);
        
        // 默认Webhook通知渠道
        NotificationChannel webhookChannel = new NotificationChannel(
                "default-webhook", "默认Webhook通知", "WEBHOOK", "http://localhost:8080/webhook/alerts");
        webhookChannel.setConfiguration(Map.of(
                "severities", Arrays.asList("CRITICAL", "HIGH", "MEDIUM", "LOW"),
                "headers", Map.of("Authorization", "Bearer your-token-here")
        ));
        addGlobalNotificationChannel(webhookChannel);
        
        // 默认短信通知渠道（仅用于CRITICAL级别）
        NotificationChannel smsChannel = new NotificationChannel(
                "default-sms", "默认短信通知", "SMS", "13800000000");
        smsChannel.setConfiguration(Map.of("severities", Arrays.asList("CRITICAL")));
        addGlobalNotificationChannel(smsChannel);
    }
    
    /**
     * 获取所有通知渠道
     */
    public Map<String, Object> getAllNotificationChannels() {
        Map<String, Object> result = new HashMap<>();
        result.put("globalChannels", new ArrayList<>(globalChannels.values()));
        result.put("instanceChannels", instanceChannels);
        return result;
    }
    
    /**
     * 获取可用的通知服务类型
     */
    public List<String> getAvailableNotificationTypes() {
        List<String> availableTypes = new ArrayList<>();
        for (Map.Entry<String, NotificationService> entry : notificationServices.entrySet()) {
            if (entry.getValue().isAvailable()) {
                availableTypes.add(entry.getKey());
            }
        }
        return availableTypes;
    }
}