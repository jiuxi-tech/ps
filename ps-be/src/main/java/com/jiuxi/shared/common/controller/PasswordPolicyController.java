package com.jiuxi.shared.common.controller;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.bean.PasswordPolicyProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码策略查询控制器
 * 提供密码策略配置信息供前端动态验证使用
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("/api/sys")
public class PasswordPolicyController {

    @Autowired
    private SecurityConfigurationProperties securityConfigurationProperties;

    /**
     * 获取密码策略配置
     * 前端通过此接口获取密码策略配置，用于动态验证
     * 
     * @return 密码策略配置信息
     */
    @GetMapping("/password-policy")
    public JsonResponse getPasswordPolicy() {
        log.debug("获取密码策略配置");
        
        try {
            PasswordPolicyProperties passwordPolicy = 
                securityConfigurationProperties.getAuthentication().getPasswordPolicy();
            
            // 构建返回数据
            Map<String, Object> policyData = new HashMap<>();
            
            // 长度配置
            Map<String, Integer> length = new HashMap<>();
            length.put("min", passwordPolicy.getLength().getMin());
            length.put("max", passwordPolicy.getLength().getMax());
            policyData.put("length", length);
            
            // 复杂度配置
            Map<String, Object> complexity = new HashMap<>();
            complexity.put("enabled", passwordPolicy.getComplexity().isEnabled());
            complexity.put("requireUppercase", passwordPolicy.getComplexity().isRequireUppercase());
            complexity.put("requireLowercase", passwordPolicy.getComplexity().isRequireLowercase());
            complexity.put("requireDigit", passwordPolicy.getComplexity().isRequireDigit());
            complexity.put("requireSpecial", passwordPolicy.getComplexity().isRequireSpecial());
            complexity.put("allowedSpecialChars", passwordPolicy.getComplexity().getAllowedSpecialChars());
            complexity.put("customRegex", passwordPolicy.getComplexity().getCustomRegex());
            policyData.put("complexity", complexity);
            
            // 弱密码配置（出于安全考虑，不返回黑名单具体内容，只返回是否启用）
            Map<String, Object> weakPassword = new HashMap<>();
            weakPassword.put("enabled", passwordPolicy.getWeakPassword().isEnabled());
            weakPassword.put("checkUsernameSimilarity", passwordPolicy.getWeakPassword().isCheckUsernameSimilarity());
            weakPassword.put("minStrengthLevel", passwordPolicy.getWeakPassword().getMinStrengthLevel());
            policyData.put("weakPassword", weakPassword);
            
            log.debug("密码策略配置返回成功: {}", policyData);
            return JsonResponse.buildSuccess(policyData);
            
        } catch (Exception e) {
            log.error("获取密码策略配置失败", e);
            return JsonResponse.buildFailure("获取密码策略配置失败: " + e.getMessage());
        }
    }
}
