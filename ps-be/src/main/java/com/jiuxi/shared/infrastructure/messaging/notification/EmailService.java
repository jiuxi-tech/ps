package com.jiuxi.shared.infrastructure.messaging.notification;

/**
 * 邮件通知服务接口
 * @author DDD重构
 * @date 2025-09-12
 */
public interface EmailService {

    /**
     * 发送验证码邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param code    验证码
     * @return boolean 发送是否成功
     */
    boolean sendVerificationCode(String to, String subject, String code);

    /**
     * 发送普通文本邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return boolean 发送是否成功
     */
    boolean sendTextMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to       收件人邮箱
     * @param subject  邮件主题
     * @param content HTML内容
     * @return boolean 发送是否成功
     */
    boolean sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     *
     * @param to           收件人邮箱
     * @param subject      邮件主题
     * @param content      邮件内容
     * @param attachmentPath 附件路径
     * @return boolean 发送是否成功
     */
    boolean sendAttachmentMail(String to, String subject, String content, String attachmentPath);
}