package com.jiuxi.security.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.bean.PasswordPolicyProperties;
import com.jiuxi.security.core.entity.vo.ValidationResult;
import com.jiuxi.security.core.service.PasswordValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * 密码验证服务实现类
 * 基于配置提供密码策略验证功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Service
public class PasswordValidationServiceImpl implements PasswordValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordValidationServiceImpl.class);

    @Autowired
    private SecurityConfigurationProperties properties;

    /**
     * 验证密码长度
     */
    @Override
    public ValidationResult validatePasswordLength(String password) {
        if (StrUtil.isBlank(password)) {
            return ValidationResult.failure("PASSWORD_EMPTY", "密码不能为空");
        }

        PasswordPolicyProperties.LengthConfig lengthConfig = 
            properties.getAuthentication().getPasswordPolicy().getLength();

        int length = password.length();
        int minLength = lengthConfig.getMin();
        int maxLength = lengthConfig.getMax();

        if (length < minLength) {
            String message = String.format("密码长度不能少于%d位", minLength);
            return ValidationResult.failure("PASSWORD_TOO_SHORT", message)
                    .addDetail("minLength", minLength)
                    .addDetail("actualLength", length);
        }

        if (length > maxLength) {
            String message = String.format("密码长度不能超过%d位", maxLength);
            return ValidationResult.failure("PASSWORD_TOO_LONG", message)
                    .addDetail("maxLength", maxLength)
                    .addDetail("actualLength", length);
        }

        return ValidationResult.success();
    }

    /**
     * 验证密码复杂度
     */
    @Override
    public ValidationResult validatePasswordComplexity(String password) {
        if (StrUtil.isBlank(password)) {
            return ValidationResult.failure("PASSWORD_EMPTY", "密码不能为空");
        }

        PasswordPolicyProperties.ComplexityConfig complexityConfig = 
            properties.getAuthentication().getPasswordPolicy().getComplexity();

        // 如果未启用复杂度验证，直接通过
        if (!complexityConfig.isEnabled()) {
            return ValidationResult.success();
        }

        // 优先使用自定义正则表达式
        String customRegex = complexityConfig.getCustomRegex();
        if (StrUtil.isNotBlank(customRegex)) {
            try {
                if (!Pattern.matches(customRegex, password)) {
                    return ValidationResult.failure("PASSWORD_COMPLEXITY_INVALID", 
                            "密码不符合复杂度要求");
                }
                return ValidationResult.success();
            } catch (Exception e) {
                LOGGER.error("自定义密码正则表达式验证失败: {}", customRegex, e);
                return ValidationResult.failure("PASSWORD_REGEX_ERROR", "密码验证规则配置错误");
            }
        }

        // 使用复杂度规则进行验证
        StringBuilder message = new StringBuilder("密码必须包含");
        boolean hasError = false;

        if (complexityConfig.isRequireUppercase() && !password.matches(".*[A-Z].*")) {
            message.append("大写字母、");
            hasError = true;
        }

        if (complexityConfig.isRequireLowercase() && !password.matches(".*[a-z].*")) {
            message.append("小写字母、");
            hasError = true;
        }

        if (complexityConfig.isRequireDigit() && !password.matches(".*\\d.*")) {
            message.append("数字、");
            hasError = true;
        }

        if (complexityConfig.isRequireSpecial()) {
            String specialChars = complexityConfig.getAllowedSpecialChars();
            String specialRegex = ".*[" + Pattern.quote(specialChars) + "].*";
            if (!password.matches(specialRegex)) {
                message.append("特殊字符(").append(specialChars).append(")、");
                hasError = true;
            }
        }

        if (hasError) {
            // 移除最后的"、"
            String errorMessage = message.substring(0, message.length() - 1);
            return ValidationResult.failure("PASSWORD_COMPLEXITY_INSUFFICIENT", errorMessage);
        }

        return ValidationResult.success();
    }

    /**
     * 检查弱密码
     */
    @Override
    public ValidationResult checkWeakPassword(String password, String username) {
        if (StrUtil.isBlank(password)) {
            return ValidationResult.failure("PASSWORD_EMPTY", "密码不能为空");
        }

        PasswordPolicyProperties.WeakPasswordConfig weakPasswordConfig = 
            properties.getAuthentication().getPasswordPolicy().getWeakPassword();

        // 如果未启用弱密码检测，直接通过
        if (!weakPasswordConfig.isEnabled()) {
            return ValidationResult.success();
        }

        // 检查黑名单
        String lowerPassword = password.toLowerCase();
        for (String weakPwd : weakPasswordConfig.getBlacklist()) {
            if (lowerPassword.equals(weakPwd.toLowerCase())) {
                return ValidationResult.failure("PASSWORD_IN_BLACKLIST", 
                        "密码过于简单，请使用更复杂的密码");
            }
        }

        // 检查与用户名相似度
        if (weakPasswordConfig.isCheckUsernameSimilarity() && StrUtil.isNotBlank(username)) {
            String lowerUsername = username.toLowerCase();
            // 密码包含用户名
            if (lowerPassword.contains(lowerUsername)) {
                return ValidationResult.failure("PASSWORD_SIMILAR_USERNAME", 
                        "密码不能与用户名相似");
            }
            // 用户名包含密码
            if (lowerUsername.contains(lowerPassword)) {
                return ValidationResult.failure("PASSWORD_SIMILAR_USERNAME", 
                        "密码不能与用户名相似");
            }
        }

        // 检查密码强度等级
        int strengthLevel = getPasswordStrengthLevel(password);
        int minStrengthLevel = weakPasswordConfig.getMinStrengthLevel();
        if (strengthLevel < minStrengthLevel) {
            String levelName = strengthLevel == 1 ? "弱" : "中";
            String minLevelName = minStrengthLevel == 2 ? "中" : "强";
            String message = String.format("密码强度为%s，不符合最低要求（%s）", levelName, minLevelName);
            return ValidationResult.failure("PASSWORD_STRENGTH_INSUFFICIENT", message)
                    .addDetail("strengthLevel", strengthLevel)
                    .addDetail("minStrengthLevel", minStrengthLevel);
        }

        return ValidationResult.success();
    }

    /**
     * 综合验证密码
     */
    @Override
    public ValidationResult validatePassword(String password, String username) {
        // 1. 验证长度
        ValidationResult lengthResult = validatePasswordLength(password);
        if (!lengthResult.isValid()) {
            return lengthResult;
        }

        // 2. 验证复杂度
        ValidationResult complexityResult = validatePasswordComplexity(password);
        if (!complexityResult.isValid()) {
            return complexityResult;
        }

        // 3. 检查弱密码
        ValidationResult weakPasswordResult = checkWeakPassword(password, username);
        if (!weakPasswordResult.isValid()) {
            return weakPasswordResult;
        }

        return ValidationResult.success();
    }

    /**
     * 获取密码强度等级
     */
    @Override
    public int getPasswordStrengthLevel(String password) {
        if (StrUtil.isBlank(password)) {
            return 1; // 弱
        }

        int score = 0;

        // 长度评分
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;

        // 字符类型评分
        if (password.matches(".*[A-Z].*")) score++; // 包含大写字母
        if (password.matches(".*[a-z].*")) score++; // 包含小写字母
        if (password.matches(".*\\d.*")) score++;   // 包含数字
        if (password.matches(".*[^a-zA-Z0-9].*")) score++; // 包含特殊字符

        // 根据分数判断强度
        if (score <= 2) return 1; // 弱
        if (score <= 4) return 2; // 中
        return 3; // 强
    }

    /**
     * 构建密码复杂度正则表达式
     */
    @Override
    public String buildComplexityRegex() {
        PasswordPolicyProperties.ComplexityConfig complexityConfig = 
            properties.getAuthentication().getPasswordPolicy().getComplexity();
        PasswordPolicyProperties.LengthConfig lengthConfig = 
            properties.getAuthentication().getPasswordPolicy().getLength();

        // 如果有自定义正则，直接返回
        if (StrUtil.isNotBlank(complexityConfig.getCustomRegex())) {
            return complexityConfig.getCustomRegex();
        }

        // 构建正则表达式
        StringBuilder regex = new StringBuilder("^");

        // 添加前瞻断言
        if (complexityConfig.isRequireUppercase()) {
            regex.append("(?=.*[A-Z])");
        }
        if (complexityConfig.isRequireLowercase()) {
            regex.append("(?=.*[a-z])");
        }
        if (complexityConfig.isRequireDigit()) {
            regex.append("(?=.*\\d)");
        }
        if (complexityConfig.isRequireSpecial()) {
            String specialChars = Pattern.quote(complexityConfig.getAllowedSpecialChars());
            regex.append("(?=.*[").append(specialChars).append("])");
        }

        // 添加字符集和长度限制
        regex.append("[a-zA-Z\\d");
        if (complexityConfig.isRequireSpecial()) {
            regex.append(Pattern.quote(complexityConfig.getAllowedSpecialChars()));
        }
        regex.append("]{").append(lengthConfig.getMin()).append(",")
              .append(lengthConfig.getMax()).append("}$");

        return regex.toString();
    }
}
