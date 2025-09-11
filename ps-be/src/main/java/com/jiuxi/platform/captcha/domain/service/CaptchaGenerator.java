package com.jiuxi.platform.captcha.domain.service;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;

/**
 * 验证码生成器接口
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public interface CaptchaGenerator {
    
    /**
     * 生成验证码挑战
     * 
     * @param captchaType 验证码类型
     * @return 验证码挑战
     */
    CaptchaChallenge generateChallenge(CaptchaType captchaType);
    
    /**
     * 检查是否支持指定类型的验证码
     * 
     * @param captchaType 验证码类型
     * @return 是否支持
     */
    boolean supports(CaptchaType captchaType);
    
    /**
     * 获取生成器名称
     * 
     * @return 生成器名称
     */
    String getGeneratorName();
}