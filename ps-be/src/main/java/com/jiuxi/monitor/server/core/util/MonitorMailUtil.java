package com.jiuxi.monitor.server.core.util;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.monitor.server.bean.MailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: SendMailUtil
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/20 15:35
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class MonitorMailUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorMailUtil.class);

    /**
     * 邮箱格式
     */
    private static Pattern pattern = Pattern.compile("[a-zA-Z0-9]+@[A-Za-z0-9]+\\.[a-z0-9]");

    /**
     * 发送邮件
     *
     * @param mailConfig 邮件服务配置
     * @param email      收件人地址
     * @param subject    标题
     * @param content    内容
     * @return boolean
     * @author 杨占锐
     * @date 2024/11/20 15:45
     */
    public static boolean send(MailConfig mailConfig, String email, String subject, String content) {

        if (StrUtil.isBlank(mailConfig.getUser())) {
            LOGGER.error("发送邮件失败，user为空");
            return false;
        }
        if (StrUtil.isBlank(mailConfig.getPassword())) {
            LOGGER.error("发送邮件失败，password为空");
            return false;
        }
        if (StrUtil.isBlank(email)) {
            LOGGER.error("发送邮件失败，email地址为空");
            return false;
        }
        if (StrUtil.isBlank(subject)) {
            LOGGER.error("发送邮件失败，subject为空");
            return false;
        }
        if (StrUtil.isBlank(content)) {
            LOGGER.error("发送邮件失败，content为空");
            return false;
        }

        try {

            Properties props = new Properties();
            // 表示SMTP发送邮件，必须进行身份验证
            props.put("mail.smtp.auth", "true");
            // 启用TLS加密
            props.put("mail.smtp.starttls.enable", "true");
            //此处填写SMTP服务器
            props.put("mail.smtp.host", mailConfig.getSmtp());
            //端口号
            props.put("mail.smtp.port", mailConfig.getPort());
            // 此处填写你的账号
            props.put("mail.user", mailConfig.getUser());
            // 此处的密码就是前面说的16位STMP口令
            props.put("mail.password", mailConfig.getPassword());

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(
                    props.getProperty("mail.user"));
            message.setFrom(form);

            // 设置收件人的邮箱
            InternetAddress to = new InternetAddress(email);
            message.setRecipient(Message.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject(subject);

            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");

            // 最后当然就是发送邮件啦
            Transport.send(message);

        } catch (Exception e) {
            LOGGER.error("发送邮件失败，错误：{}, email：{}， subject：{}，content：{}", ExceptionUtils.getStackTrace(e), email, subject, content);
            return false;
        }

        return true;
    }


    /**
     * 校验邮箱格式
     *
     * @param email
     * @return boolean 校验成功返回true
     * @author 杨占锐
     * @date 2024/11/27 15:06
     */
    public static boolean validEmail(String email) {
        Matcher m = pattern.matcher(email);
        if(m.find()){
            return true;
        } else {
            return false;
        }
    }
}
