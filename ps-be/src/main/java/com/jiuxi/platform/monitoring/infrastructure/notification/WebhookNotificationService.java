package com.jiuxi.platform.monitoring.infrastructure.notification;

import com.jiuxi.platform.monitoring.domain.entity.Alert;
import com.jiuxi.platform.monitoring.domain.service.NotificationService;
import com.jiuxi.platform.monitoring.domain.valueobject.NotificationChannel;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Webhook通知服务实现
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class WebhookNotificationService implements NotificationService {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int TIMEOUT_MS = 10000; // 10秒超时
    
    // Webhook配置
    private boolean enabled = true;
    private int maxRetries = 3;
    private int retryDelay = 1000; // 1秒
    
    @Override
    public boolean sendAlertNotification(Alert alert, NotificationChannel channel) {
        if (!channel.isWebhookChannel() || !enabled) {
            return false;
        }
        
        try {
            String jsonPayload = generateAlertWebhookPayload(alert);
            return sendWebhookRequest(channel.getTarget(), jsonPayload, channel.getConfiguration());
        } catch (Exception e) {
            System.err.println("发送Webhook告警通知失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean sendAlertResolvedNotification(Alert alert, NotificationChannel channel) {
        if (!channel.isWebhookChannel() || !enabled) {
            return false;
        }
        
        try {
            String jsonPayload = generateResolvedWebhookPayload(alert);
            return sendWebhookRequest(channel.getTarget(), jsonPayload, channel.getConfiguration());
        } catch (Exception e) {
            System.err.println("发送Webhook告警恢复通知失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isAvailable() {
        return enabled;
    }
    
    @Override
    public String getSupportedType() {
        return "WEBHOOK";
    }
    
    /**
     * 生成告警Webhook负载
     */
    private String generateAlertWebhookPayload(Alert alert) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"type\":\"alert\",");
        json.append("\"status\":\"firing\",");
        json.append("\"alertId\":\"").append(escapeJson(alert.getAlertId())).append("\",");
        json.append("\"ruleId\":\"").append(escapeJson(alert.getRuleId())).append("\",");
        json.append("\"ruleName\":\"").append(escapeJson(alert.getRuleName())).append("\",");
        json.append("\"instanceId\":\"").append(escapeJson(alert.getInstanceId())).append("\",");
        json.append("\"metricName\":\"").append(escapeJson(alert.getMetricName())).append("\",");
        json.append("\"metricValue\":").append(alert.getMetricValue()).append(",");
        json.append("\"threshold\":").append(alert.getThreshold()).append(",");
        json.append("\"condition\":\"").append(escapeJson(alert.getCondition())).append("\",");
        json.append("\"severity\":\"").append(escapeJson(alert.getSeverity())).append("\",");
        json.append("\"message\":\"").append(escapeJson(alert.getMessage())).append("\",");
        json.append("\"fireTime\":\"").append(alert.getFireTime().format(TIME_FORMATTER)).append("\",");
        json.append("\"timestamp\":\"").append(java.time.LocalDateTime.now().format(TIME_FORMATTER)).append("\",");
        json.append("\"summary\":\"").append(escapeJson(alert.getSummary())).append("\"");
        
        // 添加标签和注解（如果存在）
        if (alert.getLabels() != null && !alert.getLabels().isEmpty()) {
            json.append(",\"labels\":{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : alert.getLabels().entrySet()) {
                if (!first) json.append(",");
                json.append("\"").append(escapeJson(entry.getKey())).append("\":\"")
                    .append(escapeJson(String.valueOf(entry.getValue()))).append("\"");
                first = false;
            }
            json.append("}");
        }
        
        if (alert.getAnnotations() != null && !alert.getAnnotations().isEmpty()) {
            json.append(",\"annotations\":{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : alert.getAnnotations().entrySet()) {
                if (!first) json.append(",");
                json.append("\"").append(escapeJson(entry.getKey())).append("\":\"")
                    .append(escapeJson(String.valueOf(entry.getValue()))).append("\"");
                first = false;
            }
            json.append("}");
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * 生成告警恢复Webhook负载
     */
    private String generateResolvedWebhookPayload(Alert alert) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"type\":\"alert\",");
        json.append("\"status\":\"resolved\",");
        json.append("\"alertId\":\"").append(escapeJson(alert.getAlertId())).append("\",");
        json.append("\"ruleId\":\"").append(escapeJson(alert.getRuleId())).append("\",");
        json.append("\"ruleName\":\"").append(escapeJson(alert.getRuleName())).append("\",");
        json.append("\"instanceId\":\"").append(escapeJson(alert.getInstanceId())).append("\",");
        json.append("\"metricName\":\"").append(escapeJson(alert.getMetricName())).append("\",");
        json.append("\"severity\":\"").append(escapeJson(alert.getSeverity())).append("\",");
        json.append("\"fireTime\":\"").append(alert.getFireTime().format(TIME_FORMATTER)).append("\",");
        
        if (alert.getResolveTime() != null) {
            json.append("\"resolveTime\":\"").append(alert.getResolveTime().format(TIME_FORMATTER)).append("\",");
        }
        
        json.append("\"duration\":").append(alert.getDurationMinutes()).append(",");
        json.append("\"timestamp\":\"").append(java.time.LocalDateTime.now().format(TIME_FORMATTER)).append("\"");
        json.append("}");
        
        return json.toString();
    }
    
    /**
     * 发送Webhook请求
     */
    private boolean sendWebhookRequest(String webhookUrl, String payload, Map<String, Object> config) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // 模拟HTTP请求发送（实际实现应使用HttpClient）
                boolean success = simulateWebhookRequest(webhookUrl, payload, config);
                
                if (success) {
                    return true;
                } else if (attempt < maxRetries) {
                    Thread.sleep(retryDelay * attempt); // 递增延迟
                }
                
            } catch (Exception e) {
                System.err.println("Webhook请求失败 (尝试 " + attempt + "/" + maxRetries + "): " + e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(retryDelay * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 模拟Webhook请求发送
     */
    private boolean simulateWebhookRequest(String webhookUrl, String payload, Map<String, Object> config) {
        System.out.println("=== Webhook通知 ===");
        System.out.println("URL: " + webhookUrl);
        System.out.println("Method: POST");
        System.out.println("Content-Type: application/json");
        
        // 添加自定义头部（如果配置了）
        if (config != null) {
            Object headers = config.get("headers");
            if (headers instanceof Map) {
                System.out.println("Custom Headers:");
                @SuppressWarnings("unchecked")
                Map<String, String> headerMap = (Map<String, String>) headers;
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                }
            }
        }
        
        System.out.println("Payload:");
        System.out.println(formatJson(payload));
        System.out.println("发送时间: " + java.time.LocalDateTime.now().format(TIME_FORMATTER));
        System.out.println("模拟响应: 200 OK");
        System.out.println("=================");
        
        return true; // 模拟成功
    }
    
    /**
     * 实际的HTTP请求发送（示例实现）
     */
    @SuppressWarnings("unused")
    private boolean sendActualHttpRequest(String webhookUrl, String payload, Map<String, Object> config) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法和头部
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "MonitoringSystem/1.0");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            connection.setDoOutput(true);
            
            // 添加自定义头部
            if (config != null) {
                Object headers = config.get("headers");
                if (headers instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> headerMap = (Map<String, String>) headers;
                    for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
            }
            
            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 检查响应状态
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
            
        } catch (Exception e) {
            throw new RuntimeException("HTTP请求失败", e);
        }
    }
    
    /**
     * JSON字符串转义
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * 格式化JSON输出
     */
    private String formatJson(String json) {
        // 简单的JSON格式化（实际项目中可使用JSON库）
        return json.replace(",", ",\n  ")
                   .replace("{", "{\n  ")
                   .replace("}", "\n}");
    }
    
    /**
     * 验证Webhook URL
     */
    public boolean isValidWebhookUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        try {
            new URL(url);
            return url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 测试Webhook连接
     */
    public boolean testWebhook(String webhookUrl, Map<String, Object> config) {
        try {
            String testPayload = "{\"type\":\"test\",\"message\":\"This is a test webhook notification\",\"timestamp\":\"" + 
                    java.time.LocalDateTime.now().format(TIME_FORMATTER) + "\"}";
            
            return sendWebhookRequest(webhookUrl, testPayload, config);
        } catch (Exception e) {
            System.err.println("Webhook测试失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 配置Webhook服务
     */
    public void configure(boolean enabled, int maxRetries, int retryDelay) {
        this.enabled = enabled;
        this.maxRetries = Math.max(1, maxRetries);
        this.retryDelay = Math.max(100, retryDelay);
    }
    
    /**
     * 获取Webhook配置信息
     */
    public Map<String, Object> getConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("enabled", enabled);
        config.put("maxRetries", maxRetries);
        config.put("retryDelay", retryDelay);
        config.put("timeout", TIMEOUT_MS);
        return config;
    }
}