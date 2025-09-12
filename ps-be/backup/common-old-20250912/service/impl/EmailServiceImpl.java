package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @ClassName: EmailServiceImpl
 * @Description: 邮件服务实现类
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Service("commonEmailService")
@ConditionalOnClass(JavaMailSender.class)
public class EmailServiceImpl implements EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    @Value("${app.name:九溪系统}")
    private String appName;
    
    @Value("${app.url:http://localhost:10801}")
    private String appUrl;
    
    // 邮箱格式验证正则
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    public EmailServiceImpl(@Autowired(required = false) JavaMailSender mailSender, 
                           @Autowired(required = false) TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    @Override
    public boolean sendSimpleEmail(String to, String subject, String content) {
        return sendSimpleEmail(Collections.singletonList(to), subject, content);
    }
    
    @Override
    public boolean sendSimpleEmail(List<String> toList, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toList.toArray(new String[0]));
            message.setSubject(subject);
            message.setText(content);
            message.setSentDate(new Date());
            
            mailSender.send(message);
            logger.info("简单邮件发送成功，收件人: {}, 主题: {}", toList, subject);
            return true;
        } catch (Exception e) {
            logger.error("简单邮件发送失败，收件人: {}, 主题: {}", toList, subject, e);
            return false;
        }
    }
    
    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        return sendHtmlEmail(Collections.singletonList(to), subject, htmlContent);
    }
    
    @Override
    public boolean sendHtmlEmail(List<String> toList, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toList.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setSentDate(new Date());
            
            mailSender.send(message);
            logger.info("HTML邮件发送成功，收件人: {}, 主题: {}", toList, subject);
            return true;
        } catch (Exception e) {
            logger.error("HTML邮件发送失败，收件人: {}, 主题: {}", toList, subject, e);
            return false;
        }
    }
    
    @Override
    public boolean sendEmailWithAttachments(String to, String subject, String content, List<File> attachments) {
        return sendEmailWithAttachments(Collections.singletonList(to), subject, content, attachments);
    }
    
    @Override
    public boolean sendEmailWithAttachments(List<String> toList, String subject, String content, List<File> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toList.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setSentDate(new Date());
            
            // 添加附件
            if (attachments != null && !attachments.isEmpty()) {
                for (File attachment : attachments) {
                    if (attachment.exists() && attachment.isFile()) {
                        helper.addAttachment(attachment.getName(), attachment);
                    }
                }
            }
            
            mailSender.send(message);
            logger.info("带附件邮件发送成功，收件人: {}, 主题: {}, 附件数: {}", toList, subject, attachments.size());
            return true;
        } catch (Exception e) {
            logger.error("带附件邮件发送失败，收件人: {}, 主题: {}", toList, subject, e);
            return false;
        }
    }
    
    @Override
    public boolean sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        return sendTemplateEmail(Collections.singletonList(to), subject, templateName, variables);
    }
    
    @Override
    public boolean sendTemplateEmail(List<String> toList, String subject, String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            
            // 添加公共变量
            context.setVariable("appName", appName);
            context.setVariable("appUrl", appUrl);
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("currentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            String htmlContent = templateEngine.process(templateName, context);
            return sendHtmlEmail(toList, subject, htmlContent);
        } catch (Exception e) {
            logger.error("模板邮件发送失败，收件人: {}, 主题: {}, 模板: {}", toList, subject, templateName, e);
            return false;
        }
    }
    
    @Override
    public boolean sendVerificationCode(String to, String code, int expireMinutes) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("code", code);
            variables.put("expireMinutes", expireMinutes);
            variables.put("email", to);
            
            return sendTemplateEmail(to, "【" + appName + "】邮箱验证码", "verification-code", variables);
        } catch (Exception e) {
            logger.error("验证码邮件发送失败，收件人: {}", to, e);
            // 如果模板不存在，发送简单文本邮件
            String content = String.format(
                "您的验证码是：%s\n\n" +
                "验证码有效期为 %d 分钟，请及时使用。\n\n" +
                "如果这不是您的操作，请忽略此邮件。\n\n" +
                "此邮件由系统自动发送，请勿回复。",
                code, expireMinutes
            );
            return sendSimpleEmail(to, "【" + appName + "】邮箱验证码", content);
        }
    }
    
    @Override
    public boolean sendPasswordResetEmail(String to, String resetLink, int expireMinutes) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("resetLink", resetLink);
            variables.put("expireMinutes", expireMinutes);
            variables.put("email", to);
            
            return sendTemplateEmail(to, "【" + appName + "】密码重置", "password-reset", variables);
        } catch (Exception e) {
            logger.error("密码重置邮件发送失败，收件人: {}", to, e);
            // 如果模板不存在，发送简单文本邮件
            String content = String.format(
                "您请求重置密码，请点击以下链接进行重置：\n\n" +
                "%s\n\n" +
                "重置链接有效期为 %d 分钟，请及时使用。\n\n" +
                "如果这不是您的操作，请忽略此邮件。\n\n" +
                "此邮件由系统自动发送，请勿回复。",
                resetLink, expireMinutes
            );
            return sendSimpleEmail(to, "【" + appName + "】密码重置", content);
        }
    }
    
    @Override
    public boolean sendAccountActivationEmail(String to, String activationLink, int expireMinutes) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("activationLink", activationLink);
            variables.put("expireMinutes", expireMinutes);
            variables.put("email", to);
            
            return sendTemplateEmail(to, "【" + appName + "】账户激活", "account-activation", variables);
        } catch (Exception e) {
            logger.error("账户激活邮件发送失败，收件人: {}", to, e);
            // 如果模板不存在，发送简单文本邮件
            String content = String.format(
                "欢迎注册 %s！\n\n" +
                "请点击以下链接激活您的账户：\n\n" +
                "%s\n\n" +
                "激活链接有效期为 %d 分钟，请及时激活。\n\n" +
                "此邮件由系统自动发送，请勿回复。",
                appName, activationLink, expireMinutes
            );
            return sendSimpleEmail(to, "【" + appName + "】账户激活", content);
        }
    }
    
    @Override
    public boolean sendSystemNotification(String to, String title, String message, String level) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("title", title);
            variables.put("message", message);
            variables.put("level", level);
            variables.put("email", to);
            
            String levelText = "";
            switch (level.toLowerCase()) {
                case "warning":
                    levelText = "警告";
                    break;
                case "error":
                    levelText = "错误";
                    break;
                default:
                    levelText = "通知";
                    break;
            }
            
            return sendTemplateEmail(to, "【" + appName + "】系统" + levelText, "system-notification", variables);
        } catch (Exception e) {
            logger.error("系统通知邮件发送失败，收件人: {}", to, e);
            // 如果模板不存在，发送简单文本邮件
            String content = String.format(
                "系统通知\n\n" +
                "标题：%s\n\n" +
                "内容：%s\n\n" +
                "级别：%s\n\n" +
                "时间：%s\n\n" +
                "此邮件由系统自动发送，请勿回复。",
                title, message, level,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            return sendSimpleEmail(to, "【" + appName + "】系统通知", content);
        }
    }
    
    @Override
    public Map<String, Object> sendBatchEmails(List<Map<String, Object>> emailList) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failureCount = 0;
        List<String> failureEmails = new ArrayList<>();
        
        for (Map<String, Object> emailInfo : emailList) {
            try {
                String to = (String) emailInfo.get("to");
                String subject = (String) emailInfo.get("subject");
                String content = (String) emailInfo.get("content");
                Boolean isHtml = (Boolean) emailInfo.getOrDefault("isHtml", false);
                
                boolean success;
                if (isHtml) {
                    success = sendHtmlEmail(to, subject, content);
                } else {
                    success = sendSimpleEmail(to, subject, content);
                }
                
                if (success) {
                    successCount++;
                } else {
                    failureCount++;
                    failureEmails.add(to);
                }
            } catch (Exception e) {
                failureCount++;
                String to = (String) emailInfo.get("to");
                failureEmails.add(to);
                logger.error("批量邮件发送失败，收件人: {}", to, e);
            }
        }
        
        result.put("total", emailList.size());
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("failureEmails", failureEmails);
        
        logger.info("批量邮件发送完成，总数: {}, 成功: {}, 失败: {}", emailList.size(), successCount, failureCount);
        return result;
    }
    
    @Async
    @Override
    public void sendEmailAsync(String to, String subject, String content, boolean isHtml) {
        sendEmailAsync(Collections.singletonList(to), subject, content, isHtml);
    }
    
    @Async
    @Override
    public void sendEmailAsync(List<String> toList, String subject, String content, boolean isHtml) {
        try {
            if (isHtml) {
                sendHtmlEmail(toList, subject, content);
            } else {
                sendSimpleEmail(toList, subject, content);
            }
        } catch (Exception e) {
            logger.error("异步邮件发送失败，收件人: {}, 主题: {}", toList, subject, e);
        }
    }
    
    @Override
    public boolean validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return pattern.matcher(email.trim()).matches();
    }
    
    @Override
    public boolean checkEmailServiceStatus() {
        try {
            // 尝试创建一个测试邮件消息来检查服务状态
            MimeMessage testMessage = mailSender.createMimeMessage();
            return testMessage != null;
        } catch (Exception e) {
            logger.error("邮件服务状态检查失败", e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getEmailStatistics(String startDate, String endDate) {
        // 这里应该从数据库或日志中获取统计信息
        // 暂时返回模拟数据
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSent", 0);
        statistics.put("successCount", 0);
        statistics.put("failureCount", 0);
        statistics.put("startDate", startDate);
        statistics.put("endDate", endDate);
        
        logger.info("获取邮件统计信息，开始日期: {}, 结束日期: {}", startDate, endDate);
        return statistics;
    }
    
    @Override
    public Map<String, Object> getEmailHistory(String email, int page, int size) {
        // 这里应该从数据库中获取邮件发送历史
        // 暂时返回模拟数据
        Map<String, Object> history = new HashMap<>();
        history.put("records", Collections.emptyList());
        history.put("total", 0);
        history.put("current", page);
        history.put("size", size);
        history.put("pages", 0);
        
        logger.info("获取邮件发送历史，邮箱: {}, 页码: {}, 大小: {}", email, page, size);
        return history;
    }
}