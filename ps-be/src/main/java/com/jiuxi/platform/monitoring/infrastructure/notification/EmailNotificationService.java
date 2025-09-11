package com.jiuxi.platform.monitoring.infrastructure.notification;

import com.jiuxi.platform.monitoring.domain.entity.Alert;
import com.jiuxi.platform.monitoring.domain.service.NotificationService;
import com.jiuxi.platform.monitoring.domain.valueobject.NotificationChannel;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件通知服务实现
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class EmailNotificationService implements NotificationService {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 邮件模板
    private final Map<String, String> emailTemplates = new HashMap<>();
    
    public EmailNotificationService() {
        initializeEmailTemplates();
    }
    
    @Override
    public boolean sendAlertNotification(Alert alert, NotificationChannel channel) {
        if (!channel.isEmailChannel()) {
            return false;
        }
        
        try {
            String subject = generateAlertSubject(alert);
            String content = generateAlertContent(alert);
            
            // 这里实际应该集成真正的邮件发送服务（如JavaMail、阿里云邮件等）
            // 为了不依赖外部服务，这里只做模拟发送
            simulateEmailSending(channel.getTarget(), subject, content);
            
            return true;
        } catch (Exception e) {
            System.err.println("发送告警邮件失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean sendAlertResolvedNotification(Alert alert, NotificationChannel channel) {
        if (!channel.isEmailChannel()) {
            return false;
        }
        
        try {
            String subject = generateResolvedSubject(alert);
            String content = generateResolvedContent(alert);
            
            simulateEmailSending(channel.getTarget(), subject, content);
            
            return true;
        } catch (Exception e) {
            System.err.println("发送告警恢复邮件失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isAvailable() {
        // 这里可以检查邮件服务配置是否正确
        return true;
    }
    
    @Override
    public String getSupportedType() {
        return "EMAIL";
    }
    
    /**
     * 生成告警邮件主题
     */
    private String generateAlertSubject(Alert alert) {
        return String.format("【%s告警】%s - %s", alert.getSeverity(), alert.getRuleName(), alert.getInstanceId());
    }
    
    /**
     * 生成告警邮件内容
     */
    private String generateAlertContent(Alert alert) {
        String template = emailTemplates.get("alert");
        
        return template
                .replace("{SEVERITY}", alert.getSeverity())
                .replace("{RULE_NAME}", alert.getRuleName())
                .replace("{INSTANCE_ID}", alert.getInstanceId())
                .replace("{METRIC_NAME}", alert.getMetricName())
                .replace("{METRIC_VALUE}", String.valueOf(alert.getMetricValue()))
                .replace("{THRESHOLD}", String.valueOf(alert.getThreshold()))
                .replace("{CONDITION}", alert.getCondition())
                .replace("{FIRE_TIME}", alert.getFireTime().format(TIME_FORMATTER))
                .replace("{MESSAGE}", alert.getMessage())
                .replace("{ALERT_ID}", alert.getAlertId());
    }
    
    /**
     * 生成告警恢复邮件主题
     */
    private String generateResolvedSubject(Alert alert) {
        return String.format("【告警恢复】%s - %s", alert.getRuleName(), alert.getInstanceId());
    }
    
    /**
     * 生成告警恢复邮件内容
     */
    private String generateResolvedContent(Alert alert) {
        String template = emailTemplates.get("resolved");
        
        return template
                .replace("{RULE_NAME}", alert.getRuleName())
                .replace("{INSTANCE_ID}", alert.getInstanceId())
                .replace("{METRIC_NAME}", alert.getMetricName())
                .replace("{FIRE_TIME}", alert.getFireTime().format(TIME_FORMATTER))
                .replace("{RESOLVE_TIME}", alert.getResolveTime() != null ? alert.getResolveTime().format(TIME_FORMATTER) : "未知")
                .replace("{DURATION}", String.valueOf(alert.getDurationMinutes()))
                .replace("{ALERT_ID}", alert.getAlertId());
    }
    
    /**
     * 模拟邮件发送
     */
    private void simulateEmailSending(String to, String subject, String content) {
        System.out.println("=== 邮件通知 ===");
        System.out.println("收件人: " + to);
        System.out.println("主题: " + subject);
        System.out.println("内容: " + content);
        System.out.println("发送时间: " + java.time.LocalDateTime.now().format(TIME_FORMATTER));
        System.out.println("===============");
    }
    
    /**
     * 初始化邮件模板
     */
    private void initializeEmailTemplates() {
        // 告警邮件模板
        String alertTemplate = "<!DOCTYPE html>" +
                "<html><head><title>系统告警通知</title></head><body>" +
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #d32f2f;'>🚨 系统告警通知</h2>" +
                "<div style='background-color: #f5f5f5; padding: 15px; border-radius: 5px;'>" +
                "<table style='width: 100%; border-collapse: collapse;'>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>告警级别:</td><td style='color: #d32f2f; padding: 5px 0;'>{SEVERITY}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>规则名称:</td><td style='padding: 5px 0;'>{RULE_NAME}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>实例ID:</td><td style='padding: 5px 0;'>{INSTANCE_ID}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>监控指标:</td><td style='padding: 5px 0;'>{METRIC_NAME}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>当前值:</td><td style='padding: 5px 0;'>{METRIC_VALUE}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>阈值条件:</td><td style='padding: 5px 0;'>{CONDITION} {THRESHOLD}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>触发时间:</td><td style='padding: 5px 0;'>{FIRE_TIME}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>告警消息:</td><td style='padding: 5px 0;'>{MESSAGE}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>告警ID:</td><td style='padding: 5px 0; font-family: monospace;'>{ALERT_ID}</td></tr>" +
                "</table>" +
                "</div>" +
                "<p style='margin-top: 20px; font-size: 12px; color: #666;'>" +
                "此邮件由监控系统自动发送，请勿回复。<br>" +
                "如有问题请联系系统管理员。" +
                "</p>" +
                "</div></body></html>";
        
        emailTemplates.put("alert", alertTemplate);
        
        // 告警恢复邮件模板
        String resolvedTemplate = "<!DOCTYPE html>" +
                "<html><head><title>告警恢复通知</title></head><body>" +
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #2e7d32;'>✅ 告警恢复通知</h2>" +
                "<div style='background-color: #e8f5e8; padding: 15px; border-radius: 5px;'>" +
                "<table style='width: 100%; border-collapse: collapse;'>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>规则名称:</td><td style='padding: 5px 0;'>{RULE_NAME}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>实例ID:</td><td style='padding: 5px 0;'>{INSTANCE_ID}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>监控指标:</td><td style='padding: 5px 0;'>{METRIC_NAME}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>触发时间:</td><td style='padding: 5px 0;'>{FIRE_TIME}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>恢复时间:</td><td style='padding: 5px 0;'>{RESOLVE_TIME}</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>持续时间:</td><td style='padding: 5px 0;'>{DURATION} 分钟</td></tr>" +
                "<tr><td style='font-weight: bold; padding: 5px 0;'>告警ID:</td><td style='padding: 5px 0; font-family: monospace;'>{ALERT_ID}</td></tr>" +
                "</table>" +
                "</div>" +
                "<p style='margin-top: 20px; font-size: 12px; color: #666;'>" +
                "此邮件由监控系统自动发送，请勿回复。<br>" +
                "如有问题请联系系统管理员。" +
                "</p>" +
                "</div></body></html>";
        
        emailTemplates.put("resolved", resolvedTemplate);
    }
    
    /**
     * 更新邮件模板
     */
    public void updateEmailTemplate(String templateType, String template) {
        if (template != null && !template.trim().isEmpty()) {
            emailTemplates.put(templateType, template);
        }
    }
    
    /**
     * 获取邮件模板
     */
    public String getEmailTemplate(String templateType) {
        return emailTemplates.get(templateType);
    }
    
    /**
     * 获取所有模板类型
     */
    public Map<String, String> getAllTemplates() {
        return new HashMap<>(emailTemplates);
    }
}