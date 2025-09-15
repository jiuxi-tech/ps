package com.jiuxi.platform.captcha.bean.vo;

/**
 * 图片验证码校验VO
 * 
 * @author DDD Refactor
 * @date 2025-09-14
 */
public class ImageCaptchaCheckVO {
    
    private String clientUuid;
    private Integer x;
    private Integer y;
    private String ticket;
    
    // Getters and Setters
    
    public String getClientUuid() {
        return clientUuid;
    }
    
    public void setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
    }
    
    public Integer getX() {
        return x;
    }
    
    public void setX(Integer x) {
        this.x = x;
    }
    
    public Integer getY() {
        return y;
    }
    
    public void setY(Integer y) {
        this.y = y;
    }
    
    public String getTicket() {
        return ticket;
    }
    
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}