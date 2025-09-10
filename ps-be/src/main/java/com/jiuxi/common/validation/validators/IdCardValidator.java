package com.jiuxi.common.validation.validators;

import com.jiuxi.common.validation.annotations.IdCard;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @ClassName: IdCardValidator
 * @Description: 身份证号验证器
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {
    
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    );
    
    private boolean allowEmpty;
    
    @Override
    public void initialize(IdCard constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空
        if (!StringUtils.hasText(value)) {
            return allowEmpty;
        }
        
        // 验证身份证号格式
        if (!ID_CARD_PATTERN.matcher(value).matches()) {
            return false;
        }
        
        // 验证校验码
        return validateCheckCode(value);
    }
    
    /**
     * 验证身份证号校验码
     */
    private boolean validateCheckCode(String idCard) {
        if (idCard.length() != 18) {
            return false;
        }
        
        // 加权因子
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 校验码对应值
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Character.getNumericValue(idCard.charAt(i)) * weights[i];
        }
        
        int mod = sum % 11;
        char expectedCheckCode = checkCodes[mod];
        char actualCheckCode = Character.toUpperCase(idCard.charAt(17));
        
        return expectedCheckCode == actualCheckCode;
    }
}