package com.jiuxi.shared.common.validation.validators;

import com.jiuxi.shared.common.validation.annotations.Email;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @ClassName: EmailValidator
 * @Description: 邮箱验证器
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class EmailValidator implements ConstraintValidator<Email, String> {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private boolean allowEmpty;
    
    @Override
    public void initialize(Email constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空
        if (!StringUtils.hasText(value)) {
            return allowEmpty;
        }
        
        // 验证邮箱格式
        return EMAIL_PATTERN.matcher(value).matches();
    }
}