package com.jiuxi.shared.infrastructure.messaging.notification.impl;

import com.jiuxi.shared.infrastructure.messaging.notification.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 邮件通知服务实现类（适配器模式）
 * @author DDD重构
 * @date 2025-09-12
 */
@Service("notificationEmailService")
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    @Qualifier("adminEmailService")
    private com.jiuxi.admin.core.service.EmailService originalEmailService;

    @Override
    public boolean sendVerificationCode(String to, String subject, String code) {
        if (originalEmailService != null) {
            return originalEmailService.sendVerificationCode(to, subject, code);
        }
        // 返回false表示发送失败
        return false;
    }

    @Override
    public boolean sendTextMail(String to, String subject, String content) {
        if (originalEmailService != null) {
            return originalEmailService.sendTextMail(to, subject, content);
        }
        return false;
    }

    @Override
    public boolean sendHtmlMail(String to, String subject, String content) {
        if (originalEmailService != null) {
            return originalEmailService.sendHtmlMail(to, subject, content);
        }
        return false;
    }

    @Override
    public boolean sendAttachmentMail(String to, String subject, String content, String attachmentPath) {
        // 原有服务可能不支持附件，这里提供默认实现
        return sendHtmlMail(to, subject, content);
    }
}