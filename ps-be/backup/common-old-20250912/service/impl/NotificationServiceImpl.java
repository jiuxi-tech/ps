package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.NotificationService;
import com.jiuxi.common.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName: NotificationServiceImpl
 * @Description: 消息通知服务实现类
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private EmailService emailService;
    
    // 内存存储通知数据（实际项目中应使用数据库）
    private final Map<String, List<Map<String, Object>>> userNotifications = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> notificationTemplates = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> userPreferences = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> userTopicSubscriptions = new ConcurrentHashMap<>();
    private final Map<String, Boolean> channelStatus = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> channelConfigs = new ConcurrentHashMap<>();
    
    public NotificationServiceImpl() {
        // 初始化默认渠道状态
        channelStatus.put("system", true);
        channelStatus.put("email", true);
        channelStatus.put("sms", false);
        channelStatus.put("wechat", false);
        channelStatus.put("dingtalk", false);
        channelStatus.put("websocket", true);
        channelStatus.put("push", false);
    }
    
    @Override
    public Map<String, Object> sendSystemNotification(String userId, String title, String content, String type) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> notification = createNotification(userId, title, content, type, "system");
            saveNotification(userId, notification);
            
            result.put("success", true);
            result.put("notificationId", notification.get("id"));
            result.put("message", "系统通知发送成功");
            
            log.info("系统通知发送成功: userId={}, title={}", userId, title);
            
        } catch (Exception e) {
            log.error("系统通知发送失败: userId={}, title={}", userId, title, e);
            result.put("success", false);
            result.put("message", "系统通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendBatchSystemNotification(List<String> userIds, String title, String content, String type) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> failedUsers = new ArrayList<>();
        
        for (String userId : userIds) {
            Map<String, Object> singleResult = sendSystemNotification(userId, title, content, type);
            if ((Boolean) singleResult.get("success")) {
                successCount++;
            } else {
                failCount++;
                failedUsers.add(userId);
            }
        }
        
        result.put("total", userIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedUsers", failedUsers);
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendBroadcastNotification(String title, String content, String type) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要从数据库获取所有用户ID
            List<String> allUserIds = getAllUserIds();
            result = sendBatchSystemNotification(allUserIds, title, content, type);
            
            log.info("广播通知发送完成: title={}, 目标用户数={}", title, allUserIds.size());
            
        } catch (Exception e) {
            log.error("广播通知发送失败: title={}", title, e);
            result.put("success", false);
            result.put("message", "广播通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendRoleNotification(String roleId, String title, String content, String type) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要从数据库根据角色ID获取用户列表
            List<String> roleUserIds = getUserIdsByRole(roleId);
            result = sendBatchSystemNotification(roleUserIds, title, content, type);
            
            log.info("角色通知发送完成: roleId={}, title={}, 目标用户数={}", roleId, title, roleUserIds.size());
            
        } catch (Exception e) {
            log.error("角色通知发送失败: roleId={}, title={}", roleId, title, e);
            result.put("success", false);
            result.put("message", "角色通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendDepartmentNotification(String deptId, String title, String content, String type) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要从数据库根据部门ID获取用户列表
            List<String> deptUserIds = getUserIdsByDepartment(deptId);
            result = sendBatchSystemNotification(deptUserIds, title, content, type);
            
            log.info("部门通知发送完成: deptId={}, title={}, 目标用户数={}", deptId, title, deptUserIds.size());
            
        } catch (Exception e) {
            log.error("部门通知发送失败: deptId={}, title={}", deptId, title, e);
            result.put("success", false);
            result.put("message", "部门通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendEmailNotification(String userId, String subject, String content, boolean isHtml) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!channelStatus.getOrDefault("email", false)) {
                result.put("success", false);
                result.put("message", "邮件通知渠道已禁用");
                return result;
            }
            
            // 获取用户邮箱地址（简化实现）
            String userEmail = getUserEmail(userId);
            if (userEmail == null) {
                result.put("success", false);
                result.put("message", "用户邮箱地址不存在");
                return result;
            }
            
            // 发送邮件
            boolean emailSuccess;
            if (isHtml) {
                emailSuccess = emailService.sendHtmlEmail(userEmail, subject, content);
            } else {
                emailSuccess = emailService.sendSimpleEmail(userEmail, subject, content);
            }
            
            // 记录通知
            Map<String, Object> notification = createNotification(userId, subject, content, "email", "email");
            notification.put("emailResult", emailSuccess);
            saveNotification(userId, notification);
            
            result.put("success", emailSuccess);
            result.put("notificationId", notification.get("id"));
            result.put("message", emailSuccess ? "邮件发送成功" : "邮件发送失败");
            
        } catch (Exception e) {
            log.error("邮件通知发送失败: userId={}, subject={}", userId, subject, e);
            result.put("success", false);
            result.put("message", "邮件通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendSmsNotification(String userId, String content, String templateCode, Map<String, Object> templateParams) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!channelStatus.getOrDefault("sms", false)) {
                result.put("success", false);
                result.put("message", "短信通知渠道已禁用");
                return result;
            }
            
            // 获取用户手机号（简化实现）
            String userPhone = getUserPhone(userId);
            if (userPhone == null) {
                result.put("success", false);
                result.put("message", "用户手机号不存在");
                return result;
            }
            
            // 简化实现，实际项目中需要集成短信服务商API
            Map<String, Object> notification = createNotification(userId, "短信通知", content, "sms", "sms");
            notification.put("phone", userPhone);
            notification.put("templateCode", templateCode);
            notification.put("templateParams", templateParams);
            saveNotification(userId, notification);
            
            result.put("success", true);
            result.put("notificationId", notification.get("id"));
            result.put("message", "短信通知发送成功");
            
            log.info("短信通知发送成功: userId={}, phone={}", userId, userPhone);
            
        } catch (Exception e) {
            log.error("短信通知发送失败: userId={}", userId, e);
            result.put("success", false);
            result.put("message", "短信通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendWechatNotification(String userId, String title, String content, String url) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!channelStatus.getOrDefault("wechat", false)) {
                result.put("success", false);
                result.put("message", "微信通知渠道已禁用");
                return result;
            }
            
            // 简化实现，实际项目中需要集成微信API
            Map<String, Object> notification = createNotification(userId, title, content, "wechat", "wechat");
            notification.put("url", url);
            saveNotification(userId, notification);
            
            result.put("success", true);
            result.put("notificationId", notification.get("id"));
            result.put("message", "微信通知发送成功");
            
            log.info("微信通知发送成功: userId={}, title={}", userId, title);
            
        } catch (Exception e) {
            log.error("微信通知发送失败: userId={}, title={}", userId, title, e);
            result.put("success", false);
            result.put("message", "微信通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendDingTalkNotification(String userId, String title, String content, String url) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!channelStatus.getOrDefault("dingtalk", false)) {
                result.put("success", false);
                result.put("message", "钉钉通知渠道已禁用");
                return result;
            }
            
            // 简化实现，实际项目中需要集成钉钉API
            Map<String, Object> notification = createNotification(userId, title, content, "dingtalk", "dingtalk");
            notification.put("url", url);
            saveNotification(userId, notification);
            
            result.put("success", true);
            result.put("notificationId", notification.get("id"));
            result.put("message", "钉钉通知发送成功");
            
            log.info("钉钉通知发送成功: userId={}, title={}", userId, title);
            
        } catch (Exception e) {
            log.error("钉钉通知发送失败: userId={}, title={}", userId, title, e);
            result.put("success", false);
            result.put("message", "钉钉通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendWebSocketNotification(String userId, String message, String type) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!channelStatus.getOrDefault("websocket", false)) {
                result.put("success", false);
                result.put("message", "WebSocket通知渠道已禁用");
                return result;
            }
            
            // 简化实现，实际项目中需要集成WebSocket
            Map<String, Object> notification = createNotification(userId, "实时通知", message, type, "websocket");
            saveNotification(userId, notification);
            
            result.put("success", true);
            result.put("notificationId", notification.get("id"));
            result.put("message", "WebSocket通知发送成功");
            
            log.info("WebSocket通知发送成功: userId={}, type={}", userId, type);
            
        } catch (Exception e) {
            log.error("WebSocket通知发送失败: userId={}, type={}", userId, type, e);
            result.put("success", false);
            result.put("message", "WebSocket通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendPushNotification(String userId, String title, String content, Map<String, Object> extras) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!channelStatus.getOrDefault("push", false)) {
                result.put("success", false);
                result.put("message", "推送通知渠道已禁用");
                return result;
            }
            
            // 简化实现，实际项目中需要集成推送服务
            Map<String, Object> notification = createNotification(userId, title, content, "push", "push");
            notification.put("extras", extras);
            saveNotification(userId, notification);
            
            result.put("success", true);
            result.put("notificationId", notification.get("id"));
            result.put("message", "推送通知发送成功");
            
            log.info("推送通知发送成功: userId={}, title={}", userId, title);
            
        } catch (Exception e) {
            log.error("推送通知发送失败: userId={}, title={}", userId, title, e);
            result.put("success", false);
            result.put("message", "推送通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendTemplateNotification(String userId, String templateId, Map<String, Object> templateParams, List<String> channels) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> template = notificationTemplates.get(templateId);
            if (template == null) {
                result.put("success", false);
                result.put("message", "通知模板不存在");
                return result;
            }
            
            String title = processTemplate((String) template.get("title"), templateParams);
            String content = processTemplate((String) template.get("content"), templateParams);
            String type = (String) template.get("type");
            
            Map<String, Object> channelResults = new HashMap<>();
            
            for (String channel : channels) {
                Map<String, Object> channelResult;
                switch (channel) {
                    case "system":
                        channelResult = sendSystemNotification(userId, title, content, type);
                        break;
                    case "email":
                        channelResult = sendEmailNotification(userId, title, content, true);
                        break;
                    case "sms":
                        channelResult = sendSmsNotification(userId, content, null, templateParams);
                        break;
                    case "wechat":
                        channelResult = sendWechatNotification(userId, title, content, null);
                        break;
                    case "dingtalk":
                        channelResult = sendDingTalkNotification(userId, title, content, null);
                        break;
                    case "websocket":
                        channelResult = sendWebSocketNotification(userId, content, type);
                        break;
                    case "push":
                        channelResult = sendPushNotification(userId, title, content, templateParams);
                        break;
                    default:
                        channelResult = new HashMap<>();
                        channelResult.put("success", false);
                        channelResult.put("message", "不支持的通知渠道: " + channel);
                }
                channelResults.put(channel, channelResult);
            }
            
            result.put("success", true);
            result.put("templateId", templateId);
            result.put("channelResults", channelResults);
            
        } catch (Exception e) {
            log.error("模板通知发送失败: userId={}, templateId={}", userId, templateId, e);
            result.put("success", false);
            result.put("message", "模板通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> sendScheduledNotification(String userId, String title, String content, String type, String scheduleTime) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要使用定时任务框架
            Map<String, Object> notification = createNotification(userId, title, content, type, "system");
            notification.put("scheduled", true);
            notification.put("scheduleTime", scheduleTime);
            notification.put("status", "scheduled");
            saveNotification(userId, notification);
            
            result.put("success", true);
            result.put("notificationId", notification.get("id"));
            result.put("message", "定时通知创建成功");
            
            log.info("定时通知创建成功: userId={}, title={}, scheduleTime={}", userId, title, scheduleTime);
            
        } catch (Exception e) {
            log.error("定时通知创建失败: userId={}, title={}", userId, title, e);
            result.put("success", false);
            result.put("message", "定时通知创建失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean cancelScheduledNotification(String notificationId) {
        try {
            // 简化实现，实际项目中需要从定时任务中移除
            for (List<Map<String, Object>> notifications : userNotifications.values()) {
                for (Map<String, Object> notification : notifications) {
                    if (notificationId.equals(notification.get("id"))) {
                        notification.put("status", "cancelled");
                        log.info("定时通知取消成功: notificationId={}", notificationId);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            log.error("定时通知取消失败: notificationId={}", notificationId, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getUserNotifications(String userId, int page, int size, String status) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> notifications = userNotifications.getOrDefault(userId, new ArrayList<>());
            
            // 过滤状态
            List<Map<String, Object>> filteredNotifications = notifications.stream()
                    .filter(n -> "all".equals(status) || status.equals(n.get("readStatus")))
                    .sorted((n1, n2) -> ((LocalDateTime) n2.get("createTime")).compareTo((LocalDateTime) n1.get("createTime")))
                    .collect(Collectors.toList());
            
            // 分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredNotifications.size());
            List<Map<String, Object>> pageNotifications = filteredNotifications.subList(start, end);
            
            result.put("records", pageNotifications);
            result.put("total", filteredNotifications.size());
            result.put("current", page);
            result.put("size", size);
            result.put("pages", (int) Math.ceil((double) filteredNotifications.size() / size));
            
        } catch (Exception e) {
            log.error("获取用户通知列表失败: userId={}", userId, e);
            result.put("records", new ArrayList<>());
            result.put("total", 0);
            result.put("current", page);
            result.put("size", size);
            result.put("pages", 0);
        }
        
        return result;
    }
    
    @Override
    public boolean markNotificationAsRead(String userId, String notificationId) {
        try {
            List<Map<String, Object>> notifications = userNotifications.get(userId);
            if (notifications != null) {
                for (Map<String, Object> notification : notifications) {
                    if (notificationId.equals(notification.get("id"))) {
                        notification.put("readStatus", "read");
                        notification.put("readTime", LocalDateTime.now());
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            log.error("标记通知已读失败: userId={}, notificationId={}", userId, notificationId, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> markNotificationsAsRead(String userId, List<String> notificationIds) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        
        for (String notificationId : notificationIds) {
            if (markNotificationAsRead(userId, notificationId)) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        result.put("total", notificationIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        
        return result;
    }
    
    @Override
    public boolean markAllNotificationsAsRead(String userId) {
        try {
            List<Map<String, Object>> notifications = userNotifications.get(userId);
            if (notifications != null) {
                for (Map<String, Object> notification : notifications) {
                    notification.put("readStatus", "read");
                    notification.put("readTime", LocalDateTime.now());
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("标记所有通知已读失败: userId={}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteNotification(String userId, String notificationId) {
        try {
            List<Map<String, Object>> notifications = userNotifications.get(userId);
            if (notifications != null) {
                return notifications.removeIf(n -> notificationId.equals(n.get("id")));
            }
            return false;
        } catch (Exception e) {
            log.error("删除通知失败: userId={}, notificationId={}", userId, notificationId, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> deleteNotifications(String userId, List<String> notificationIds) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        
        for (String notificationId : notificationIds) {
            if (deleteNotification(userId, notificationId)) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        result.put("total", notificationIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        
        return result;
    }
    
    @Override
    public boolean clearAllNotifications(String userId) {
        try {
            userNotifications.remove(userId);
            return true;
        } catch (Exception e) {
            log.error("清空用户通知失败: userId={}", userId, e);
            return false;
        }
    }
    
    @Override
    public int getUnreadNotificationCount(String userId) {
        try {
            List<Map<String, Object>> notifications = userNotifications.getOrDefault(userId, new ArrayList<>());
            return (int) notifications.stream()
                    .filter(n -> "unread".equals(n.get("readStatus")))
                    .count();
        } catch (Exception e) {
            log.error("获取未读通知数量失败: userId={}", userId, e);
            return 0;
        }
    }
    
    @Override
    public Map<String, Object> getUserNotificationStatistics(String userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Map<String, Object>> notifications = userNotifications.getOrDefault(userId, new ArrayList<>());
            
            long totalCount = notifications.size();
            long unreadCount = notifications.stream().filter(n -> "unread".equals(n.get("readStatus"))).count();
            long readCount = notifications.stream().filter(n -> "read".equals(n.get("readStatus"))).count();
            
            Map<String, Long> typeStats = notifications.stream()
                    .collect(Collectors.groupingBy(
                            n -> (String) n.get("type"),
                            Collectors.counting()
                    ));
            
            Map<String, Long> channelStats = notifications.stream()
                    .collect(Collectors.groupingBy(
                            n -> (String) n.get("channel"),
                            Collectors.counting()
                    ));
            
            stats.put("totalCount", totalCount);
            stats.put("unreadCount", unreadCount);
            stats.put("readCount", readCount);
            stats.put("typeStats", typeStats);
            stats.put("channelStats", channelStats);
            
        } catch (Exception e) {
            log.error("获取用户通知统计失败: userId={}", userId, e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getSystemNotificationStatistics(String startDate, String endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要根据日期范围统计
            long totalNotifications = userNotifications.values().stream()
                    .mapToLong(List::size)
                    .sum();
            
            long totalUsers = userNotifications.size();
            
            stats.put("totalNotifications", totalNotifications);
            stats.put("totalUsers", totalUsers);
            stats.put("averagePerUser", totalUsers > 0 ? (double) totalNotifications / totalUsers : 0);
            
        } catch (Exception e) {
            log.error("获取系统通知统计失败", e);
        }
        
        return stats;
    }
    
    @Override
    public boolean setUserNotificationPreferences(String userId, Map<String, Object> preferences) {
        try {
            userPreferences.put(userId, new HashMap<>(preferences));
            return true;
        } catch (Exception e) {
            log.error("设置用户通知偏好失败: userId={}", userId, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getUserNotificationPreferences(String userId) {
        return userPreferences.getOrDefault(userId, getDefaultPreferences());
    }
    
    @Override
    public Map<String, Object> createNotificationTemplate(String templateName, String templateContent, String templateType, List<String> channels) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String templateId = UUID.randomUUID().toString();
            Map<String, Object> template = new HashMap<>();
            template.put("id", templateId);
            template.put("name", templateName);
            template.put("content", templateContent);
            template.put("type", templateType);
            template.put("channels", channels);
            template.put("createTime", LocalDateTime.now());
            
            notificationTemplates.put(templateId, template);
            
            result.put("success", true);
            result.put("templateId", templateId);
            result.put("message", "通知模板创建成功");
            
        } catch (Exception e) {
            log.error("创建通知模板失败: templateName={}", templateName, e);
            result.put("success", false);
            result.put("message", "创建通知模板失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean updateNotificationTemplate(String templateId, String templateName, String templateContent, String templateType, List<String> channels) {
        try {
            Map<String, Object> template = notificationTemplates.get(templateId);
            if (template != null) {
                template.put("name", templateName);
                template.put("content", templateContent);
                template.put("type", templateType);
                template.put("channels", channels);
                template.put("updateTime", LocalDateTime.now());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新通知模板失败: templateId={}", templateId, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteNotificationTemplate(String templateId) {
        try {
            return notificationTemplates.remove(templateId) != null;
        } catch (Exception e) {
            log.error("删除通知模板失败: templateId={}", templateId, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getNotificationTemplates(int page, int size, String templateType) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> templates = notificationTemplates.values().stream()
                    .filter(t -> templateType == null || templateType.equals(t.get("type")))
                    .sorted((t1, t2) -> ((LocalDateTime) t2.get("createTime")).compareTo((LocalDateTime) t1.get("createTime")))
                    .collect(Collectors.toList());
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, templates.size());
            List<Map<String, Object>> pageTemplates = templates.subList(start, end);
            
            result.put("records", pageTemplates);
            result.put("total", templates.size());
            result.put("current", page);
            result.put("size", size);
            result.put("pages", (int) Math.ceil((double) templates.size() / size));
            
        } catch (Exception e) {
            log.error("获取通知模板列表失败", e);
            result.put("records", new ArrayList<>());
            result.put("total", 0);
            result.put("current", page);
            result.put("size", size);
            result.put("pages", 0);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationTemplate(String templateId) {
        return notificationTemplates.get(templateId);
    }
    
    @Override
    public Map<String, Object> testNotificationSend(String userId, String channel, String content) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            switch (channel) {
                case "system":
                    result = sendSystemNotification(userId, "测试通知", content, "info");
                    break;
                case "email":
                    result = sendEmailNotification(userId, "测试邮件", content, false);
                    break;
                case "sms":
                    result = sendSmsNotification(userId, content, null, null);
                    break;
                case "websocket":
                    result = sendWebSocketNotification(userId, content, "test");
                    break;
                default:
                    result.put("success", false);
                    result.put("message", "不支持的测试渠道: " + channel);
            }
        } catch (Exception e) {
            log.error("测试通知发送失败: userId={}, channel={}", userId, channel, e);
            result.put("success", false);
            result.put("message", "测试通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationHistory(String userId, int page, int size, String channel, String startDate, String endDate) {
        // 简化实现，返回用户通知列表
        return getUserNotifications(userId, page, size, "all");
    }
    
    @Override
    public Map<String, Object> resendFailedNotification(String notificationId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要重新发送失败的通知
            result.put("success", true);
            result.put("message", "通知重发成功");
            
        } catch (Exception e) {
            log.error("重发通知失败: notificationId={}", notificationId, e);
            result.put("success", false);
            result.put("message", "重发通知失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> resendFailedNotifications(List<String> notificationIds) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        
        for (String notificationId : notificationIds) {
            Map<String, Object> resendResult = resendFailedNotification(notificationId);
            if ((Boolean) resendResult.get("success")) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        result.put("total", notificationIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationStatus(String notificationId) {
        Map<String, Object> status = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要查询通知状态
            status.put("notificationId", notificationId);
            status.put("status", "sent");
            status.put("sendTime", LocalDateTime.now());
            
        } catch (Exception e) {
            log.error("获取通知状态失败: notificationId={}", notificationId, e);
            status.put("error", e.getMessage());
        }
        
        return status;
    }
    
    @Override
    public boolean subscribeNotificationTopic(String userId, String topic) {
        try {
            userTopicSubscriptions.computeIfAbsent(userId, k -> new HashSet<>()).add(topic);
            return true;
        } catch (Exception e) {
            log.error("订阅通知主题失败: userId={}, topic={}", userId, topic, e);
            return false;
        }
    }
    
    @Override
    public boolean unsubscribeNotificationTopic(String userId, String topic) {
        try {
            Set<String> topics = userTopicSubscriptions.get(userId);
            if (topics != null) {
                return topics.remove(topic);
            }
            return false;
        } catch (Exception e) {
            log.error("取消订阅通知主题失败: userId={}, topic={}", userId, topic, e);
            return false;
        }
    }
    
    @Override
    public List<String> getUserSubscribedTopics(String userId) {
        Set<String> topics = userTopicSubscriptions.getOrDefault(userId, new HashSet<>());
        return new ArrayList<>(topics);
    }
    
    @Override
    public Map<String, Object> sendTopicNotification(String topic, String title, String content, String type) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<String> subscribedUsers = userTopicSubscriptions.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(topic))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            
            result = sendBatchSystemNotification(subscribedUsers, title, content, type);
            result.put("topic", topic);
            
            log.info("主题通知发送完成: topic={}, title={}, 订阅用户数={}", topic, title, subscribedUsers.size());
            
        } catch (Exception e) {
            log.error("主题通知发送失败: topic={}, title={}", topic, title, e);
            result.put("success", false);
            result.put("message", "主题通知发送失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean toggleNotificationChannel(String channel, boolean enabled) {
        try {
            channelStatus.put(channel, enabled);
            log.info("通知渠道状态更新: channel={}, enabled={}", channel, enabled);
            return true;
        } catch (Exception e) {
            log.error("切换通知渠道状态失败: channel={}", channel, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getNotificationChannelStatus() {
        return new HashMap<>(channelStatus);
    }
    
    @Override
    public boolean configureNotificationChannel(String channel, Map<String, Object> config) {
        try {
            channelConfigs.put(channel, new HashMap<>(config));
            return true;
        } catch (Exception e) {
            log.error("配置通知渠道失败: channel={}", channel, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getNotificationChannelConfig(String channel) {
        return channelConfigs.getOrDefault(channel, new HashMap<>());
    }
    
    // 私有辅助方法
    
    private Map<String, Object> createNotification(String userId, String title, String content, String type, String channel) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("id", UUID.randomUUID().toString());
        notification.put("userId", userId);
        notification.put("title", title);
        notification.put("content", content);
        notification.put("type", type);
        notification.put("channel", channel);
        notification.put("readStatus", "unread");
        notification.put("createTime", LocalDateTime.now());
        notification.put("status", "sent");
        return notification;
    }
    
    private void saveNotification(String userId, Map<String, Object> notification) {
        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(notification);
    }
    
    private String processTemplate(String template, Map<String, Object> params) {
        if (template == null || params == null) {
            return template;
        }
        
        String result = template;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            result = result.replace(placeholder, String.valueOf(entry.getValue()));
        }
        
        return result;
    }
    
    private List<String> getAllUserIds() {
        // 简化实现，实际项目中需要从数据库获取
        return Arrays.asList("user1", "user2", "user3");
    }
    
    private List<String> getUserIdsByRole(String roleId) {
        // 简化实现，实际项目中需要从数据库根据角色获取用户
        return Arrays.asList("user1", "user2");
    }
    
    private List<String> getUserIdsByDepartment(String deptId) {
        // 简化实现，实际项目中需要从数据库根据部门获取用户
        return Arrays.asList("user2", "user3");
    }
    
    private String getUserEmail(String userId) {
        // 简化实现，实际项目中需要从数据库获取用户邮箱
        return userId + "@example.com";
    }
    
    private String getUserPhone(String userId) {
        // 简化实现，实际项目中需要从数据库获取用户手机号
        return "138****" + userId.hashCode() % 10000;
    }
    
    private Map<String, Object> getDefaultPreferences() {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("systemNotification", true);
        preferences.put("emailNotification", true);
        preferences.put("smsNotification", false);
        preferences.put("wechatNotification", false);
        preferences.put("dingtalkNotification", false);
        preferences.put("websocketNotification", true);
        preferences.put("pushNotification", false);
        return preferences;
    }
}