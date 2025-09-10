package com.jiuxi.common.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: NotificationService
 * @Description: 消息通知服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface NotificationService {
    
    /**
     * 发送系统通知
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型（info, warning, error, success）
     * @return 发送结果
     */
    Map<String, Object> sendSystemNotification(String userId, String title, String content, String type);
    
    /**
     * 批量发送系统通知
     * @param userIds 用户ID列表
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @return 发送结果
     */
    Map<String, Object> sendBatchSystemNotification(List<String> userIds, String title, String content, String type);
    
    /**
     * 发送广播通知（所有用户）
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @return 发送结果
     */
    Map<String, Object> sendBroadcastNotification(String title, String content, String type);
    
    /**
     * 发送角色通知（指定角色的所有用户）
     * @param roleId 角色ID
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @return 发送结果
     */
    Map<String, Object> sendRoleNotification(String roleId, String title, String content, String type);
    
    /**
     * 发送部门通知（指定部门的所有用户）
     * @param deptId 部门ID
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @return 发送结果
     */
    Map<String, Object> sendDepartmentNotification(String deptId, String title, String content, String type);
    
    /**
     * 发送邮件通知
     * @param userId 用户ID
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否HTML格式
     * @return 发送结果
     */
    Map<String, Object> sendEmailNotification(String userId, String subject, String content, boolean isHtml);
    
    /**
     * 发送短信通知
     * @param userId 用户ID
     * @param content 短信内容
     * @param templateCode 短信模板代码
     * @param templateParams 模板参数
     * @return 发送结果
     */
    Map<String, Object> sendSmsNotification(String userId, String content, String templateCode, Map<String, Object> templateParams);
    
    /**
     * 发送微信通知
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param url 跳转链接
     * @return 发送结果
     */
    Map<String, Object> sendWechatNotification(String userId, String title, String content, String url);
    
    /**
     * 发送钉钉通知
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param url 跳转链接
     * @return 发送结果
     */
    Map<String, Object> sendDingTalkNotification(String userId, String title, String content, String url);
    
    /**
     * 发送WebSocket实时通知
     * @param userId 用户ID
     * @param message 消息内容
     * @param type 消息类型
     * @return 发送结果
     */
    Map<String, Object> sendWebSocketNotification(String userId, String message, String type);
    
    /**
     * 发送推送通知（移动端）
     * @param userId 用户ID
     * @param title 推送标题
     * @param content 推送内容
     * @param extras 额外参数
     * @return 发送结果
     */
    Map<String, Object> sendPushNotification(String userId, String title, String content, Map<String, Object> extras);
    
    /**
     * 发送模板通知
     * @param userId 用户ID
     * @param templateId 模板ID
     * @param templateParams 模板参数
     * @param channels 发送渠道（system, email, sms, wechat等）
     * @return 发送结果
     */
    Map<String, Object> sendTemplateNotification(String userId, String templateId, Map<String, Object> templateParams, List<String> channels);
    
    /**
     * 发送定时通知
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @param scheduleTime 定时发送时间
     * @return 发送结果
     */
    Map<String, Object> sendScheduledNotification(String userId, String title, String content, String type, String scheduleTime);
    
    /**
     * 取消定时通知
     * @param notificationId 通知ID
     * @return 取消结果
     */
    boolean cancelScheduledNotification(String notificationId);
    
    /**
     * 获取用户通知列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @param status 通知状态（unread, read, all）
     * @return 通知列表
     */
    Map<String, Object> getUserNotifications(String userId, int page, int size, String status);
    
    /**
     * 标记通知为已读
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 操作结果
     */
    boolean markNotificationAsRead(String userId, String notificationId);
    
    /**
     * 批量标记通知为已读
     * @param userId 用户ID
     * @param notificationIds 通知ID列表
     * @return 操作结果
     */
    Map<String, Object> markNotificationsAsRead(String userId, List<String> notificationIds);
    
    /**
     * 标记所有通知为已读
     * @param userId 用户ID
     * @return 操作结果
     */
    boolean markAllNotificationsAsRead(String userId);
    
    /**
     * 删除通知
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 删除结果
     */
    boolean deleteNotification(String userId, String notificationId);
    
    /**
     * 批量删除通知
     * @param userId 用户ID
     * @param notificationIds 通知ID列表
     * @return 删除结果
     */
    Map<String, Object> deleteNotifications(String userId, List<String> notificationIds);
    
    /**
     * 清空用户所有通知
     * @param userId 用户ID
     * @return 清空结果
     */
    boolean clearAllNotifications(String userId);
    
    /**
     * 获取用户未读通知数量
     * @param userId 用户ID
     * @return 未读数量
     */
    int getUnreadNotificationCount(String userId);
    
    /**
     * 获取用户通知统计
     * @param userId 用户ID
     * @return 统计信息
     */
    Map<String, Object> getUserNotificationStatistics(String userId);
    
    /**
     * 获取系统通知统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    Map<String, Object> getSystemNotificationStatistics(String startDate, String endDate);
    
    /**
     * 设置用户通知偏好
     * @param userId 用户ID
     * @param preferences 通知偏好设置
     * @return 设置结果
     */
    boolean setUserNotificationPreferences(String userId, Map<String, Object> preferences);
    
    /**
     * 获取用户通知偏好
     * @param userId 用户ID
     * @return 通知偏好
     */
    Map<String, Object> getUserNotificationPreferences(String userId);
    
    /**
     * 创建通知模板
     * @param templateName 模板名称
     * @param templateContent 模板内容
     * @param templateType 模板类型
     * @param channels 支持的渠道
     * @return 创建结果
     */
    Map<String, Object> createNotificationTemplate(String templateName, String templateContent, String templateType, List<String> channels);
    
    /**
     * 更新通知模板
     * @param templateId 模板ID
     * @param templateName 模板名称
     * @param templateContent 模板内容
     * @param templateType 模板类型
     * @param channels 支持的渠道
     * @return 更新结果
     */
    boolean updateNotificationTemplate(String templateId, String templateName, String templateContent, String templateType, List<String> channels);
    
    /**
     * 删除通知模板
     * @param templateId 模板ID
     * @return 删除结果
     */
    boolean deleteNotificationTemplate(String templateId);
    
    /**
     * 获取通知模板列表
     * @param page 页码
     * @param size 每页大小
     * @param templateType 模板类型
     * @return 模板列表
     */
    Map<String, Object> getNotificationTemplates(int page, int size, String templateType);
    
    /**
     * 获取通知模板详情
     * @param templateId 模板ID
     * @return 模板详情
     */
    Map<String, Object> getNotificationTemplate(String templateId);
    
    /**
     * 测试通知发送
     * @param userId 用户ID
     * @param channel 发送渠道
     * @param content 测试内容
     * @return 测试结果
     */
    Map<String, Object> testNotificationSend(String userId, String channel, String content);
    
    /**
     * 获取通知发送历史
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @param channel 发送渠道
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 发送历史
     */
    Map<String, Object> getNotificationHistory(String userId, int page, int size, String channel, String startDate, String endDate);
    
    /**
     * 重发失败的通知
     * @param notificationId 通知ID
     * @return 重发结果
     */
    Map<String, Object> resendFailedNotification(String notificationId);
    
    /**
     * 批量重发失败的通知
     * @param notificationIds 通知ID列表
     * @return 重发结果
     */
    Map<String, Object> resendFailedNotifications(List<String> notificationIds);
    
    /**
     * 获取通知发送状态
     * @param notificationId 通知ID
     * @return 发送状态
     */
    Map<String, Object> getNotificationStatus(String notificationId);
    
    /**
     * 订阅通知主题
     * @param userId 用户ID
     * @param topic 主题名称
     * @return 订阅结果
     */
    boolean subscribeNotificationTopic(String userId, String topic);
    
    /**
     * 取消订阅通知主题
     * @param userId 用户ID
     * @param topic 主题名称
     * @return 取消订阅结果
     */
    boolean unsubscribeNotificationTopic(String userId, String topic);
    
    /**
     * 获取用户订阅的主题列表
     * @param userId 用户ID
     * @return 主题列表
     */
    List<String> getUserSubscribedTopics(String userId);
    
    /**
     * 发送主题通知
     * @param topic 主题名称
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @return 发送结果
     */
    Map<String, Object> sendTopicNotification(String topic, String title, String content, String type);
    
    /**
     * 启用/禁用通知渠道
     * @param channel 渠道名称
     * @param enabled 是否启用
     * @return 操作结果
     */
    boolean toggleNotificationChannel(String channel, boolean enabled);
    
    /**
     * 获取通知渠道状态
     * @return 渠道状态列表
     */
    Map<String, Object> getNotificationChannelStatus();
    
    /**
     * 配置通知渠道参数
     * @param channel 渠道名称
     * @param config 配置参数
     * @return 配置结果
     */
    boolean configureNotificationChannel(String channel, Map<String, Object> config);
    
    /**
     * 获取通知渠道配置
     * @param channel 渠道名称
     * @return 配置信息
     */
    Map<String, Object> getNotificationChannelConfig(String channel);
}