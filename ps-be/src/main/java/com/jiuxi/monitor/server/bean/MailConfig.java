package com.jiuxi.monitor.server.bean;

/**
 * @ClassName: EmailConfig
 * @Description: 邮件配置
 * @Author jiuxx
 * @Date 2024/11/20 16:00
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class MailConfig {

    /**
     * 邮箱用户名
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * smtp地址
     */
    private String smtp = "smtp.qq.com";

    /**
     * 端口
     */
    private String port = "587";

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}