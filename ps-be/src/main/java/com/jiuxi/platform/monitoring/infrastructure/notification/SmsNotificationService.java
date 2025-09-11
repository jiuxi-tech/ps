package com.jiuxi.platform.monitoring.infrastructure.notification;

import com.jiuxi.platform.monitoring.domain.entity.Alert;
import com.jiuxi.platform.monitoring.domain.service.NotificationService;
import com.jiuxi.platform.monitoring.domain.valueobject.NotificationChannel;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信通知服务实现
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class SmsNotificationService implements NotificationService {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    
    // 短信模板
    private final Map<String, String> smsTemplates = new HashMap<>();
    
    // 短信配置
    private boolean enabled = true;
    private String accessKeyId = "your_access_key_id";
    private String accessKeySecret = "your_access_key_secret";
    private String signName = "监控系统";
    
    public SmsNotificationService() {
        initializeSmsTemplates();
    }
    
    @Override
    public boolean sendAlertNotification(Alert alert, NotificationChannel channel) {
        if (!channel.isSmsChannel() || !enabled) {
            return false;
        }
        
        try {
            String content = generateAlertSmsContent(alert);
            
            // 这里实际应该集成真正的短信发送服务（如阿里云短信、腾讯云短信等）
            // 为了不依赖外部服务，这里只做模拟发送
            simulateSmsSending(channel.getTarget(), content);
            
            return true;
        } catch (Exception e) {
            System.err.println("发送告警短信失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean sendAlertResolvedNotification(Alert alert, NotificationChannel channel) {
        if (!channel.isSmsChannel() || !enabled) {
            return false;
        }
        
        try {
            String content = generateResolvedSmsContent(alert);
            
            simulateSmsSending(channel.getTarget(), content);
            
            return true;
        } catch (Exception e) {
            System.err.println("发送告警恢复短信失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isAvailable() {
        return enabled && accessKeyId != null && accessKeySecret != null;
    }
    
    @Override
    public String getSupportedType() {
        return "SMS";
    }
    
    /**
     * 生成告警短信内容
     */
    private String generateAlertSmsContent(Alert alert) {
        String template = smsTemplates.get("alert");
        
        return template
                .replace("{SEVERITY}", getSeverityText(alert.getSeverity()))
                .replace("{RULE_NAME}", alert.getRuleName())
                .replace("{INSTANCE_ID}", alert.getInstanceId())
                .replace("{METRIC_NAME}", alert.getMetricName())
                .replace("{METRIC_VALUE}", formatValue(alert.getMetricValue()))
                .replace("{THRESHOLD}", formatValue(alert.getThreshold()))
                .replace("{CONDITION}", alert.getCondition())
                .replace("{TIME}", alert.getFireTime().format(TIME_FORMATTER))
                .replace("{SIGN_NAME}", signName);
    }
    
    /**
     * 生成告警恢复短信内容
     */
    private String generateResolvedSmsContent(Alert alert) {
        String template = smsTemplates.get("resolved");
        
        return template
                .replace("{RULE_NAME}", alert.getRuleName())
                .replace("{INSTANCE_ID}", alert.getInstanceId())
                .replace("{METRIC_NAME}", alert.getMetricName())
                .replace("{FIRE_TIME}", alert.getFireTime().format(TIME_FORMATTER))
                .replace("{RESOLVE_TIME}", alert.getResolveTime() != null ? alert.getResolveTime().format(TIME_FORMATTER) : "未知")
                .replace("{DURATION}", String.valueOf(alert.getDurationMinutes()))
                .replace("{SIGN_NAME}", signName);
    }
    
    /**
     * 获取严重性中文描述
     */
    private String getSeverityText(String severity) {
        switch (severity.toUpperCase()) {
            case "CRITICAL":
                return "严重";
            case "HIGH":
                return "高";
            case "MEDIUM":
                return "中";
            case "LOW":
                return "低";
            default:
                return severity;
        }
    }
    
    /**
     * 格式化数值
     */
    private String formatValue(Double value) {
        if (value == null) return "N/A";
        if (value % 1 == 0) {
            return String.valueOf(value.intValue());
        }
        return String.format("%.2f", value);
    }
    
    /**
     * 模拟短信发送
     */
    private void simulateSmsSending(String phoneNumber, String content) {
        System.out.println("=== 短信通知 ===");
        System.out.println("手机号: " + phoneNumber);
        System.out.println("内容: " + content);
        System.out.println("签名: " + signName);
        System.out.println("发送时间: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("===============");
    }
    
    /**
     * 初始化短信模板
     */
    private void initializeSmsTemplates() {
        // 告警短信模板（考虑短信字数限制，内容要精简）
        String alertTemplate = "【{SIGN_NAME}】{SEVERITY}告警：{RULE_NAME}，实例{INSTANCE_ID}的{METRIC_NAME}当前值{METRIC_VALUE}{CONDITION}{THRESHOLD}，时间{TIME}。请及时处理！";
        smsTemplates.put("alert", alertTemplate);
        
        // 告警恢复短信模板
        String resolvedTemplate = "【{SIGN_NAME}】告警恢复：{RULE_NAME}，实例{INSTANCE_ID}的{METRIC_NAME}已恢复正常，持续{DURATION}分钟，恢复时间{RESOLVE_TIME}。";
        smsTemplates.put("resolved", resolvedTemplate);
    }
    
    /**
     * 更新短信模板
     */
    public void updateSmsTemplate(String templateType, String template) {
        if (template != null && !template.trim().isEmpty()) {
            // 检查模板长度（一般短信限制70字符）
            if (template.length() <= 200) {
                smsTemplates.put(templateType, template);
            } else {
                throw new IllegalArgumentException("短信模板长度不能超过200字符");
            }
        }
    }
    
    /**
     * 获取短信模板
     */
    public String getSmsTemplate(String templateType) {
        return smsTemplates.get(templateType);
    }
    
    /**
     * 配置短信服务
     */
    public void configureSms(String accessKeyId, String accessKeySecret, String signName) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.signName = signName;
    }
    
    /**
     * 启用短信服务
     */
    public void enable() {
        this.enabled = true;
    }
    
    /**
     * 禁用短信服务
     */
    public void disable() {
        this.enabled = false;
    }
    
    /**
     * 验证手机号格式
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        // 简单的中国手机号验证（11位数字，以1开头）
        String pattern = "^1[3-9]\\d{9}$";
        return phoneNumber.matches(pattern);
    }
    
    /**
     * 获取短信服务配置信息
     */
    public Map<String, Object> getConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("enabled", enabled);
        config.put("signName", signName);
        config.put("hasAccessKey", accessKeyId != null && !accessKeyId.isEmpty());
        config.put("templateCount", smsTemplates.size());
        return config;
    }
    
    /**
     * 测试短信发送（发送测试短信）
     */
    public boolean sendTestMessage(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) {
            return false;
        }
        
        try {
            String testContent = String.format("【%s】这是一条测试短信，发送时间：%s。如收到此短信，表示短信服务配置正常。", 
                    signName, java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            
            simulateSmsSending(phoneNumber, testContent);
            return true;
        } catch (Exception e) {
            System.err.println("发送测试短信失败: " + e.getMessage());
            return false;
        }
    }
}