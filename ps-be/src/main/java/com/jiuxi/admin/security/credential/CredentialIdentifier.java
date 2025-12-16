package com.jiuxi.admin.security.credential;

import com.jiuxi.shared.common.config.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 登录凭据识别服务
 * 通过正则表达式识别用户输入的登录凭据类型
 * 
 * @author Qoder AI
 * @since 2024-12-15
 */
@Service
public class CredentialIdentifier {
    
    private static final Logger logger = LoggerFactory.getLogger(CredentialIdentifier.class);
    
    @Autowired
    private SecurityConfig securityConfig;
    
    /**
     * 中国大陆手机号正则表达式
     * 规则: 11位数字,1开头,第二位3-9
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    /**
     * 中国身份证号正则表达式(18位)
     * 规则: 符合身份证号码规则,包含校验位
     */
    private static final Pattern IDCARD_18_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$"
    );
    
    /**
     * 中国身份证号正则表达式(15位,旧版)
     * 规则: 15位数字,符合旧版身份证号码规则
     */
    private static final Pattern IDCARD_15_PATTERN = Pattern.compile(
        "^[1-9]\\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([12]\\d)|(3[01]))\\d{3}$"
    );
    
    /**
     * 识别登录凭据类型
     * 识别优先级: 手机号 > 身份证号 > 用户名
     * 
     * @param credential 用户输入的登录凭据
     * @return 凭据类型
     */
    public CredentialType identify(String credential) {
        if (!StringUtils.hasText(credential)) {
            logger.warn("登录凭据为空,默认识别为用户名类型");
            return CredentialType.USERNAME;
        }
        
        // 去除首尾空格
        String trimmedCredential = credential.trim();
        
        // 1. 检查是否为手机号(优先级最高)
        if (isPhone(trimmedCredential)) {
            logger.debug("识别登录凭据为手机号: {}", maskCredential(trimmedCredential));
            return CredentialType.PHONE;
        }
        
        // 2. 检查是否为身份证号
        if (isIdCard(trimmedCredential)) {
            logger.debug("识别登录凭据为身份证号: {}", maskCredential(trimmedCredential));
            return CredentialType.IDCARD;
        }
        
        // 3. 默认为用户名
        logger.debug("识别登录凭据为用户名: {}", trimmedCredential);
        return CredentialType.USERNAME;
    }
    
    /**
     * 检查是否为有效的手机号
     * 
     * @param credential 待检查的凭据
     * @return true表示是手机号
     */
    public boolean isPhone(String credential) {
        if (!StringUtils.hasText(credential)) {
            return false;
        }
        return PHONE_PATTERN.matcher(credential.trim()).matches();
    }
    
    /**
     * 检查是否为有效的身份证号
     * 支持15位和18位身份证号
     * 
     * @param credential 待检查的凭据
     * @return true表示是身份证号
     */
    public boolean isIdCard(String credential) {
        if (!StringUtils.hasText(credential)) {
            logger.debug("身份证号检查: 凭据为空");
            return false;
        }
        
        String trimmed = credential.trim();
        logger.debug("身份证号检查: 凭据='{}', 长度={}", trimmed, trimmed.length());
        
        // 检查18位身份证
        if (trimmed.length() == 18) {
            boolean regexMatches = IDCARD_18_PATTERN.matcher(trimmed).matches();
            logger.debug("身份证号检查: 正则匹配结果={}", regexMatches);
            if (regexMatches) {
                // 检查配置是否启用校验位验证
                boolean strictValidation = securityConfig.getCredential().isIdcardCheckcodeValidation();
                logger.debug("身份证号检查: 校验位严格验证模式={}", strictValidation);
                
                if (strictValidation) {
                    // 严格模式: 必须校验位正确
                    boolean checkCodeValid = validateIdCard18CheckCode(trimmed);
                    logger.debug("身份证号检查: 校验位验证结果={}", checkCodeValid);
                    return checkCodeValid;
                } else {
                    // 宽松模式: 仅验证格式，校验位错误也允许
                    boolean checkCodeValid = validateIdCard18CheckCode(trimmed);
                    logger.debug("身份证号检查: 校验位验证结果={}", checkCodeValid);
                    if (!checkCodeValid) {
                        logger.warn("身份证号校验位不正确，但允许登录(宽松模式): {}", trimmed);
                    }
                    return true; // 只要格式正确就允许
                }
            }
        }
        
        // 检查15位身份证(旧版)
        if (trimmed.length() == 15) {
            boolean matches = IDCARD_15_PATTERN.matcher(trimmed).matches();
            logger.debug("身份证号检查: 15位身份证匹配结果={}", matches);
            return matches;
        }
        
        logger.debug("身份证号检查: 不符合任何规则");
        return false;
    }
    
    /**
     * 校验18位身份证号的校验位
     * 
     * @param idCard 18位身份证号
     * @return true表示校验位正确
     */
    private boolean validateIdCard18CheckCode(String idCard) {
        if (idCard.length() != 18) {
            logger.debug("校验位验证: 身份证号长度不是18位");
            return false;
        }
        
        try {
            // 加权因子
            int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            // 校验码对应值
            char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
            
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                int digit = Character.getNumericValue(idCard.charAt(i));
                sum += digit * weights[i];
            }
            
            int mod = sum % 11;
            char expectedCheckCode = checkCodes[mod];
            char actualCheckCode = Character.toUpperCase(idCard.charAt(17));
            
            logger.debug("校验位验证: 身份证号={}, 前17位={}, 加权和={}, 模11={}, 期望校验码={}, 实际校验码={}, 匹配={}",
                idCard, idCard.substring(0, 17), sum, mod, expectedCheckCode, actualCheckCode, (expectedCheckCode == actualCheckCode));
            
            return expectedCheckCode == actualCheckCode;
        } catch (Exception e) {
            logger.warn("身份证号校验位验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 对凭据进行脱敏处理(用于日志输出)
     * 手机号: 138****0000
     * 身份证号: 42010619****123456
     * 用户名: 不脱敏
     * 
     * @param credential 原始凭据
     * @return 脱敏后的凭据
     */
    private String maskCredential(String credential) {
        if (!StringUtils.hasText(credential)) {
            return "";
        }
        
        String trimmed = credential.trim();
        
        // 手机号脱敏: 138****0000
        if (isPhone(trimmed)) {
            return trimmed.substring(0, 3) + "****" + trimmed.substring(7);
        }
        
        // 身份证号脱敏: 42010619****123456
        if (isIdCard(trimmed)) {
            if (trimmed.length() == 18) {
                return trimmed.substring(0, 8) + "****" + trimmed.substring(14);
            } else if (trimmed.length() == 15) {
                return trimmed.substring(0, 6) + "****" + trimmed.substring(11);
            }
        }
        
        // 用户名不脱敏
        return trimmed;
    }
}
