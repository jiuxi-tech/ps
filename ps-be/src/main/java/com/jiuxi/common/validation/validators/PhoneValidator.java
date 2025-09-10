package com.jiuxi.common.validation.validators;

import com.jiuxi.common.validation.annotations.Phone;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @ClassName: PhoneValidator
 * @Description: 手机号验证器
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    private boolean allowEmpty;
    
    @Override
    public void initialize(Phone constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空
        if (!StringUtils.hasText(value)) {
            return allowEmpty;
        }
        
        // 验证手机号格式
        return PHONE_PATTERN.matcher(value).matches();
    }
}