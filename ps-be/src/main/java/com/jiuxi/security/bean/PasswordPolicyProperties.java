package com.jiuxi.security.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 密码策略配置类
 * 用于配置密码长度、复杂度和弱密码检测等策略
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class PasswordPolicyProperties {

    /**
     * 密码长度配置
     */
    private LengthConfig length = new LengthConfig();

    /**
     * 密码复杂度配置
     */
    private ComplexityConfig complexity = new ComplexityConfig();

    /**
     * 弱密码检测配置
     */
    private WeakPasswordConfig weakPassword = new WeakPasswordConfig();

    /**
     * 密码长度配置
     */
    public static class LengthConfig {
        /**
         * 最小长度，默认8位
         */
        private int min = 8;

        /**
         * 最大长度，默认20位
         */
        private int max = 20;

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }

    /**
     * 密码复杂度配置
     */
    public static class ComplexityConfig {
        /**
         * 是否启用复杂度验证，默认启用
         */
        private boolean enabled = true;

        /**
         * 是否必须包含大写字母，默认true
         */
        private boolean requireUppercase = true;

        /**
         * 是否必须包含小写字母，默认true
         */
        private boolean requireLowercase = true;

        /**
         * 是否必须包含数字，默认true
         */
        private boolean requireDigit = true;

        /**
         * 是否必须包含特殊字符，默认false
         */
        private boolean requireSpecial = false;

        /**
         * 允许的特殊字符集，默认"@$!%*?&"
         */
        private String allowedSpecialChars = "@$!%*?&";

        /**
         * 自定义正则表达式（优先级最高），为空则使用复杂度规则生成正则
         */
        private String customRegex = "";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isRequireUppercase() {
            return requireUppercase;
        }

        public void setRequireUppercase(boolean requireUppercase) {
            this.requireUppercase = requireUppercase;
        }

        public boolean isRequireLowercase() {
            return requireLowercase;
        }

        public void setRequireLowercase(boolean requireLowercase) {
            this.requireLowercase = requireLowercase;
        }

        public boolean isRequireDigit() {
            return requireDigit;
        }

        public void setRequireDigit(boolean requireDigit) {
            this.requireDigit = requireDigit;
        }

        public boolean isRequireSpecial() {
            return requireSpecial;
        }

        public void setRequireSpecial(boolean requireSpecial) {
            this.requireSpecial = requireSpecial;
        }

        public String getAllowedSpecialChars() {
            return allowedSpecialChars;
        }

        public void setAllowedSpecialChars(String allowedSpecialChars) {
            this.allowedSpecialChars = allowedSpecialChars;
        }

        public String getCustomRegex() {
            return customRegex;
        }

        public void setCustomRegex(String customRegex) {
            this.customRegex = customRegex;
        }
    }

    /**
     * 弱密码检测配置
     */
    public static class WeakPasswordConfig {
        /**
         * 是否启用弱密码检测，默认启用
         */
        private boolean enabled = true;

        /**
         * 弱密码黑名单
         */
        private List<String> blacklist = new ArrayList<>();

        /**
         * 是否检查与用户名相似度，默认true
         */
        private boolean checkUsernameSimilarity = true;

        /**
         * 最低密码强度等级（1=弱，2=中，3=强），默认2
         */
        private int minStrengthLevel = 2;

        public WeakPasswordConfig() {
            // 初始化默认黑名单
            blacklist.add("123456");
            blacklist.add("password");
            blacklist.add("admin123");
            blacklist.add("12345678");
            blacklist.add("qwerty");
            blacklist.add("111111");
            blacklist.add("abc123");
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getBlacklist() {
            return blacklist;
        }

        public void setBlacklist(List<String> blacklist) {
            this.blacklist = blacklist;
        }

        public boolean isCheckUsernameSimilarity() {
            return checkUsernameSimilarity;
        }

        public void setCheckUsernameSimilarity(boolean checkUsernameSimilarity) {
            this.checkUsernameSimilarity = checkUsernameSimilarity;
        }

        public int getMinStrengthLevel() {
            return minStrengthLevel;
        }

        public void setMinStrengthLevel(int minStrengthLevel) {
            this.minStrengthLevel = minStrengthLevel;
        }
    }

    public LengthConfig getLength() {
        return length;
    }

    public void setLength(LengthConfig length) {
        this.length = length;
    }

    public ComplexityConfig getComplexity() {
        return complexity;
    }

    public void setComplexity(ComplexityConfig complexity) {
        this.complexity = complexity;
    }

    public WeakPasswordConfig getWeakPassword() {
        return weakPassword;
    }

    public void setWeakPassword(WeakPasswordConfig weakPassword) {
        this.weakPassword = weakPassword;
    }
}
