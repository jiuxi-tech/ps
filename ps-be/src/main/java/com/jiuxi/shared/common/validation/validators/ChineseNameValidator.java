package com.jiuxi.shared.common.validation.validators;

import com.jiuxi.shared.common.validation.annotations.ChineseName;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 中文姓名验证器
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class ChineseNameValidator implements ConstraintValidator<ChineseName, String> {

    private static final Pattern CHINESE_NAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5·]+$");
    
    private boolean allowEmpty;
    private int minLength;
    private int maxLength;

    @Override
    public void initialize(ChineseName constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
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

        // 中文字符检查（包含中文点号·）
        return CHINESE_NAME_PATTERN.matcher(value).matches();
    }
}