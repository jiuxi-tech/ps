package com.jiuxi.module.role.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 角色验证结果
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleValidationResult {
    
    private final boolean valid;
    private final List<String> errorMessages;
    private final List<String> warningMessages;
    
    private RoleValidationResult(boolean valid, List<String> errorMessages, List<String> warningMessages) {
        this.valid = valid;
        this.errorMessages = Objects.requireNonNull(errorMessages, "错误消息列表不能为空");
        this.warningMessages = Objects.requireNonNull(warningMessages, "警告消息列表不能为空");
    }
    
    public static RoleValidationResult success() {
        return new RoleValidationResult(true, Collections.emptyList(), Collections.emptyList());
    }
    
    public static RoleValidationResult failure(String errorMessage) {
        List<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        return new RoleValidationResult(false, errors, Collections.emptyList());
    }
    
    public static RoleValidationResult failure(List<String> errorMessages) {
        return new RoleValidationResult(false, new ArrayList<>(errorMessages), Collections.emptyList());
    }
    
    public static RoleValidationResult withWarnings(List<String> warningMessages) {
        return new RoleValidationResult(true, Collections.emptyList(), new ArrayList<>(warningMessages));
    }
    
    public static RoleValidationResult withErrorsAndWarnings(List<String> errorMessages, List<String> warningMessages) {
        return new RoleValidationResult(false, new ArrayList<>(errorMessages), new ArrayList<>(warningMessages));
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public boolean hasErrors() {
        return !errorMessages.isEmpty();
    }
    
    public boolean hasWarnings() {
        return !warningMessages.isEmpty();
    }
    
    public List<String> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }
    
    public List<String> getWarningMessages() {
        return Collections.unmodifiableList(warningMessages);
    }
    
    public String getFirstErrorMessage() {
        return errorMessages.isEmpty() ? null : errorMessages.get(0);
    }
    
    public String getFirstWarningMessage() {
        return warningMessages.isEmpty() ? null : warningMessages.get(0);
    }
    
    /**
     * 合并两个验证结果
     */
    public RoleValidationResult merge(RoleValidationResult other) {
        Objects.requireNonNull(other, "其他验证结果不能为空");
        
        List<String> mergedErrors = new ArrayList<>(this.errorMessages);
        mergedErrors.addAll(other.errorMessages);
        
        List<String> mergedWarnings = new ArrayList<>(this.warningMessages);
        mergedWarnings.addAll(other.warningMessages);
        
        boolean mergedValid = this.valid && other.valid;
        
        return new RoleValidationResult(mergedValid, mergedErrors, mergedWarnings);
    }
    
    @Override
    public String toString() {
        return "RoleValidationResult{" +
                "valid=" + valid +
                ", errorCount=" + errorMessages.size() +
                ", warningCount=" + warningMessages.size() +
                '}';
    }
}