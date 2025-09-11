package com.jiuxi.platform.monitoring.domain.service;

import com.jiuxi.platform.monitoring.domain.entity.Alert;
import com.jiuxi.platform.monitoring.domain.valueobject.NotificationChannel;

/**
 * 通知服务接口
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public interface NotificationService {
    
    /**
     * 发送告警通知
     * 
     * @param alert 告警信息
     * @param channel 通知渠道
     * @return 是否发送成功
     */
    boolean sendAlertNotification(Alert alert, NotificationChannel channel);
    
    /**
     * 发送告警恢复通知
     * 
     * @param alert 告警信息
     * @param channel 通知渠道
     * @return 是否发送成功
     */
    boolean sendAlertResolvedNotification(Alert alert, NotificationChannel channel);
    
    /**
     * 检查通知服务是否可用
     * 
     * @return 是否可用
     */
    boolean isAvailable();
    
    /**
     * 获取支持的通知类型
     * 
     * @return 支持的类型
     */
    String getSupportedType();
}