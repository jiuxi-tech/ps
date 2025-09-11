package com.jiuxi.shared.common.validation.validators;

import com.jiuxi.shared.common.validation.annotations.BusinessCode;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 业务编码验证器
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class BusinessCodeValidator implements ConstraintValidator<BusinessCode, String> {

    private static final Pattern DEFAULT_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_-]*$");
    
    private boolean allowEmpty;
    private int minLength;
    private int maxLength;
    private Pattern customPattern;

    @Override
    public void initialize(BusinessCode constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        
        String patternStr = constraintAnnotation.pattern();
        if (StringUtils.hasText(patternStr)) {
            this.customPattern = Pattern.compile(patternStr);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值处理
        if (!StringUtils.hasText(value)) {
            return allowEmpty;
        }

        // 长度检查
        if (value.length() < minLength || value.length() > maxLength) {
            return false;
        }

        // 格式检查
        Pattern pattern = customPattern != null ? customPattern : DEFAULT_PATTERN;
        return pattern.matcher(value).matches();
    }
}