package com.jiuxi.module.org.domain.model.vo;

import java.util.Objects;

/**
 * 组织代码值对象
 * 包含统一社会信用代码、组织机构代码等
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class OrganizationCode {
    
    /**
     * 统一社会信用代码
     */
    private final String unifiedSocialCreditCode;
    
    /**
     * 组织机构代码
     */
    private final String organizationCode;
    
    /**
     * 代码类型
     */
    private final CodeType codeType;
    
    public OrganizationCode(String code, CodeType codeType) {
        this.codeType = codeType;
        if (codeType == CodeType.UNIFIED_SOCIAL_CREDIT_CODE) {
            this.unifiedSocialCreditCode = code;
            this.organizationCode = null;
        } else {
            this.unifiedSocialCreditCode = null;
            this.organizationCode = code;
        }
    }
    
    /**
     * 验证代码是否有效
     */
    public boolean isValid() {
        if (codeType == CodeType.UNIFIED_SOCIAL_CREDIT_CODE) {
            return isValidUnifiedSocialCreditCode(unifiedSocialCreditCode);
        } else {
            return isValidOrganizationCode(organizationCode);
        }
    }
    
    /**
     * 验证统一社会信用代码
     */
    private boolean isValidUnifiedSocialCreditCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        // 统一社会信用代码应该是18位字符
        return code.matches("^[0-9A-HJ-NPQRTUWXY]{2}[0-9]{6}[0-9A-HJ-NPQRTUWXY]{10}$");
    }
    
    /**
     * 验证组织机构代码
     */
    private boolean isValidOrganizationCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        // 组织机构代码应该是9位字符（8位数字或字母 + 1位校验码）
        return code.matches("^[0-9A-Z]{8}-?[0-9A-Z]$");
    }
    
    /**
     * 获取主代码
     */
    public String getMainCode() {
        return codeType == CodeType.UNIFIED_SOCIAL_CREDIT_CODE ? unifiedSocialCreditCode : organizationCode;
    }
    
    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }
    
    public String getOrganizationCode() {
        return organizationCode;
    }
    
    public CodeType getCodeType() {
        return codeType;
    }
    
    /**
     * 代码类型枚举
     */
    public enum CodeType {
        /**
         * 统一社会信用代码
         */
        UNIFIED_SOCIAL_CREDIT_CODE("统一社会信用代码"),
        
        /**
         * 组织机构代码
         */
        ORGANIZATION_CODE("组织机构代码");
        
        private final String description;
        
        CodeType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationCode that = (OrganizationCode) o;
        return Objects.equals(unifiedSocialCreditCode, that.unifiedSocialCreditCode) &&
               Objects.equals(organizationCode, that.organizationCode) &&
               codeType == that.codeType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(unifiedSocialCreditCode, organizationCode, codeType);
    }
    
    @Override
    public String toString() {
        return "OrganizationCode{" +
                "code='" + getMainCode() + '\'' +
                ", codeType=" + codeType +
                '}';
    }
}