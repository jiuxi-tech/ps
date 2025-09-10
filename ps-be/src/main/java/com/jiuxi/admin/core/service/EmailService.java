package com.jiuxi.admin.core.service;

/**
 * @Description: 邮件发送服务接口
 * @ClassName: EmailService
 * @Author: Trae AI
 * @Date: 2024/12/19
 * @Copyright: 2024 www.tuxun.net Inc. All rights reserved.
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
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content HTML内容
     * @return boolean 发送是否成功
     */
    boolean sendHtmlMail(String to, String subject, String content);
}