package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.ValidationResult;

/**
 * 密码验证服务接口
 * 提供基于配置的密码策略验证功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public interface PasswordValidationService {

    /**
     * 验证密码长度是否符合要求
     * 
     * @param password 待验证的密码
     * @return 验证结果
     */
    ValidationResult validatePasswordLength(String password);

    /**
     * 验证密码复杂度是否符合要求
     * 
     * @param password 待验证的密码
     * @return 验证结果
     */
    ValidationResult validatePasswordComplexity(String password);

    /**
     * 检查是否为弱密码
     * 
     * @param password 待验证的密码
     * @param username 用户名（可选，用于相似度检查）
     * @return 验证结果
     */
    ValidationResult checkWeakPassword(String password, String username);

    /**
     * 综合验证密码（包含长度、复杂度、弱密码检测）
     * 
     * @param password 待验证的密码
     * @param username 用户名（可选，用于相似度检查）
     * @return 验证结果
     */
    ValidationResult validatePassword(String password, String username);

    /**
     * 获取密码强度等级
     * 
     * @param password 待验证的密码
     * @return 密码强度等级（1=弱，2=中，3=强）
     */
    int getPasswordStrengthLevel(String password);

    /**
     * 构建密码复杂度正则表达式
     * 根据配置动态生成正则表达式
     * 
     * @return 正则表达式
     */
    String buildComplexityRegex();
}
