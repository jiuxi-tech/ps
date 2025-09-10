package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

/**
 * @Description: 邮件发送服务实现类
 * @ClassName: EmailServiceImpl
 * @Author: Trae AI
 * @Date: 2024/12/19
 * @Copyright: 2024 www.tuxun.net Inc. All rights reserved.
 */
@Service("adminEmailService")
@ConditionalOnClass(JavaMailSender.class)
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String from;

    @PostConstruct
    public void init() {
        LOGGER.info("EmailService初始化 - JavaMailSender注入状态: {}", mailSender != null ? "成功" : "失败");
        LOGGER.info("EmailService初始化 - 发件人邮箱: {}", from);
        if (mailSender != null) {
            LOGGER.info("EmailService初始化 - JavaMailSender类型: {}", mailSender.getClass().getName());
        }
    }

    /**
     * 发送验证码邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param code    验证码
     * @return boolean 发送是否成功
     */
    @Override
    public boolean sendVerificationCode(String to, String subject, String code) {
        String content = String.format(
            "您好！\n\n" +
            "您正在进行密码找回操作，验证码为：%s\n\n" +
            "验证码有效期为5分钟，请及时使用。\n\n" +
            "如果这不是您的操作，请忽略此邮件。\n\n" +
            "此邮件由系统自动发送，请勿回复。",
            code
        );
        return sendTextMail(to, subject, content);
    }

    /**
     * 发送普通文本邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return boolean 发送是否成功
     */
    @Override
    public boolean sendTextMail(String to, String subject, String content) {
        if (mailSender == null) {
            LOGGER.warn("邮件服务未配置，无法发送邮件到: {}", to);
            return false;
        }
        
        try {
            LOGGER.info("邮件服务配置 - 发件人: {}, SMTP主机: {}", from, "smtp.163.com");
            LOGGER.info("JavaMailSender实例: {}", mailSender != null ? "已注入" : "未注入");
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            LOGGER.info("准备发送邮件到: {}, 主题: {}", to, subject);
            mailSender.send(message);
            LOGGER.info("邮件发送成功到: {}", to);
            return true;
        } catch (Exception e) {
            LOGGER.error("邮件发送失败到: {}, 错误类型: {}, 错误信息: {}", to, e.getClass().getSimpleName(), e.getMessage());
            LOGGER.error("详细错误堆栈:", e);
            return false;
        }
    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content HTML内容
     * @return boolean 发送是否成功
     */
    @Override
    public boolean sendHtmlMail(String to, String subject, String content) {
        if (mailSender == null) {
            LOGGER.warn("邮件服务未配置，无法发送HTML邮件到: {}", to);
            return false;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true表示HTML格式
            
            LOGGER.info("准备发送HTML邮件到: {}, 主题: {}", to, subject);
            mailSender.send(message);
            LOGGER.info("HTML邮件发送成功到: {}", to);
            return true;
        } catch (Exception e) {
            LOGGER.error("HTML邮件发送失败到: {}, 错误: {}", to, e.getMessage(), e);
            return false;
        }
    }
}