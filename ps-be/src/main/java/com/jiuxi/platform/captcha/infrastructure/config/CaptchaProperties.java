package com.jiuxi.platform.captcha.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 验证码配置属性
 * 
 * @author System
 * @date 2025-12-16
 */
@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {
    
    /**
     * 验证码最大尝试次数，默认3次
     */
    private int maxAttempts = 3;
    
    /**
     * 图片相关配置
     */
    private ImageConfig image = new ImageConfig();
    
    public int getMaxAttempts() {
        return maxAttempts;
    }
    
    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
    
    public ImageConfig getImage() {
        return image;
    }
    
    public void setImage(ImageConfig image) {
        this.image = image;
    }
    
    /**
     * 图片配置
     */
    public static class ImageConfig {
        /**
         * 外部验证码图片根目录
         */
        private String externalDir;
        
        public String getExternalDir() {
            return externalDir;
        }
        
        public void setExternalDir(String externalDir) {
            this.externalDir = externalDir;
        }
    }
}
