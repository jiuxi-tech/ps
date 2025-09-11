package com.jiuxi.shared.common.validation.validators;

import com.jiuxi.shared.common.validation.annotations.SafeString;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 安全字符串验证器
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class SafeStringValidator implements ConstraintValidator<SafeString, String> {

    // SQL注入关键词
    private static final Set<String> SQL_KEYWORDS = new HashSet<>(Arrays.asList(
        "select", "insert", "update", "delete", "drop", "create", "alter", "exec", 
        "execute", "union", "script", "javascript", "vbscript", "onload", "onerror"
    ));

    // XSS攻击模式
    private static final Pattern XSS_PATTERN = Pattern.compile(
        ".*(<script[^>]*>.*?</script>|javascript:|vbscript:|onload=|onerror=|alert\\(|confirm\\(|prompt\\().*",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    // Script标签模式
    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
        ".*<script[^>]*>.*",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    private boolean allowEmpty;
    private boolean checkSqlInjection;
    private boolean checkXss;
    private boolean checkScript;
    private Set<String> dangerousKeywords;

    @Override
    public void initialize(SafeString constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
        this.checkSqlInjection = constraintAnnotation.checkSqlInjection();
        this.checkXss = constraintAnnotation.checkXss();
        this.checkScript = constraintAnnotation.checkScript();
        
        this.dangerousKeywords = new HashSet<>();
        if (constraintAnnotation.dangerousKeywords().length > 0) {
            this.dangerousKeywords.addAll(Arrays.asList(constraintAnnotation.dangerousKeywords()));
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值处理
        if (!StringUtils.hasText(value)) {
            return allowEmpty;
        }

        String lowerValue = value.toLowerCase();

        // 检查SQL注入
        if (checkSqlInjection && containsSqlKeywords(lowerValue)) {
            return false;
        }

        // 检查XSS攻击
        if (checkXss && XSS_PATTERN.matcher(value).matches()) {
            return false;
        }

        // 检查脚本标签
        if (checkScript && SCRIPT_PATTERN.matcher(value).matches()) {
            return false;
        }

        // 检查自定义危险关键词
        if (!dangerousKeywords.isEmpty()) {
            for (String keyword : dangerousKeywords) {
                if (lowerValue.contains(keyword.toLowerCase())) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean containsSqlKeywords(String value) {
        for (String keyword : SQL_KEYWORDS) {
            if (value.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}