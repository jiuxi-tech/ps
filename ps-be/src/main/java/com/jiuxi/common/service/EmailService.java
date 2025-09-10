package com.jiuxi.common.service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: EmailService
 * @Description: 邮件服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface EmailService {
    
    /**
     * 发送简单文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return 发送结果
     */
    boolean sendSimpleEmail(String to, String subject, String content);
    
    /**
     * 发送简单文本邮件（多个收件人）
     * @param toList 收件人列表
     * @param subject 主题
     * @param content 内容
     * @return 发送结果
     */
    boolean sendSimpleEmail(List<String> toList, String subject, String content);
    
    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param htmlContent HTML内容
     * @return 发送结果
     */
    boolean sendHtmlEmail(String to, String subject, String htmlContent);
    
    /**
     * 发送HTML邮件（多个收件人）
     * @param toList 收件人列表
     * @param subject 主题
     * @param htmlContent HTML内容
     * @return 发送结果
     */
    boolean sendHtmlEmail(List<String> toList, String subject, String htmlContent);
    
    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param attachments 附件列表
     * @return 发送结果
     */
    boolean sendEmailWithAttachments(String to, String subject, String content, List<File> attachments);
    
    /**
     * 发送带附件的邮件（多个收件人）
     * @param toList 收件人列表
     * @param subject 主题
     * @param content 内容
     * @param attachments 附件列表
     * @return 发送结果
     */
    boolean sendEmailWithAttachments(List<String> toList, String subject, String content, List<File> attachments);
    
    /**
     * 发送模板邮件
     * @param to 收件人
     * @param subject 主题
     * @param templateName 模板名称
     * @param variables 模板变量
     * @return 发送结果
     */
    boolean sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);
    
    /**
     * 发送模板邮件（多个收件人）
     * @param toList 收件人列表
     * @param subject 主题
     * @param templateName 模板名称
     * @param variables 模板变量
     * @return 发送结果
     */
    boolean sendTemplateEmail(List<String> toList, String subject, String templateName, Map<String, Object> variables);
    
    /**
     * 发送验证码邮件
     * @param to 收件人
     * @param code 验证码
     * @param expireMinutes 过期时间（分钟）
     * @return 发送结果
     */
    boolean sendVerificationCode(String to, String code, int expireMinutes);
    
    /**
     * 发送密码重置邮件
     * @param to 收件人
     * @param resetLink 重置链接
     * @param expireMinutes 过期时间（分钟）
     * @return 发送结果
     */
    boolean sendPasswordResetEmail(String to, String resetLink, int expireMinutes);
    
    /**
     * 发送账户激活邮件
     * @param to 收件人
     * @param activationLink 激活链接
     * @param expireMinutes 过期时间（分钟）
     * @return 发送结果
     */
    boolean sendAccountActivationEmail(String to, String activationLink, int expireMinutes);
    
    /**
     * 发送系统通知邮件
     * @param to 收件人
     * @param title 通知标题
     * @param message 通知消息
     * @param level 通知级别（info, warning, error）
     * @return 发送结果
     */
    boolean sendSystemNotification(String to, String title, String message, String level);
    
    /**
     * 批量发送邮件
     * @param emailList 邮件列表（包含收件人、主题、内容等信息）
     * @return 发送结果统计
     */
    Map<String, Object> sendBatchEmails(List<Map<String, Object>> emailList);
    
    /**
     * 异步发送邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否HTML格式
     */
    void sendEmailAsync(String to, String subject, String content, boolean isHtml);
    
    /**
     * 异步发送邮件（多个收件人）
     * @param toList 收件人列表
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否HTML格式
     */
    void sendEmailAsync(List<String> toList, String subject, String content, boolean isHtml);
    
    /**
     * 验证邮箱地址格式
     * @param email 邮箱地址
     * @return 验证结果
     */
    boolean validateEmailFormat(String email);
    
    /**
     * 检查邮件服务状态
     * @return 服务状态
     */
    boolean checkEmailServiceStatus();
    
    /**
     * 获取邮件发送统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    Map<String, Object> getEmailStatistics(String startDate, String endDate);
    
    /**
     * 获取邮件发送历史
     * @param email 邮箱地址（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 发送历史
     */
    Map<String, Object> getEmailHistory(String email, int page, int size);
}