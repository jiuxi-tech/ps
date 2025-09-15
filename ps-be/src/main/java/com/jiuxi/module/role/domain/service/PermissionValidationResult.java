package com.jiuxi.module.role.domain.service;

import com.jiuxi.module.role.domain.model.entity.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 权限验证结果
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class PermissionValidationResult {
    
    private final boolean valid;
    private final List<String> errorMessages;
    private final List<String> warningMessages;
    private final List<Permission> invalidPermissions;
    
    private PermissionValidationResult(boolean valid, List<String> errorMessages, 
                                     List<String> warningMessages, List<Permission> invalidPermissions) {
        this.valid = valid;
        this.errorMessages = Objects.requireNonNull(errorMessages, "错误消息列表不能为空");
        this.warningMessages = Objects.requireNonNull(warningMessages, "警告消息列表不能为空");
        this.invalidPermissions = Objects.requireNonNull(invalidPermissions, "无效权限列表不能为空");
    }
    
    public static PermissionValidationResult success() {
        return new PermissionValidationResult(true, Collections.emptyList(), 
                                            Collections.emptyList(), Collections.emptyList());
    }
    
    public static PermissionValidationResult failure(String errorMessage) {
        List<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        return new PermissionValidationResult(false, errors, Collections.emptyList(), Collections.emptyList());
    }
    
    public static PermissionValidationResult failure(String errorMessage, List<Permission> invalidPermissions) {
        List<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        return new PermissionValidationResult(false, errors, Collections.emptyList(), 
                                            new ArrayList<>(invalidPermissions));
    }
    
    public static PermissionValidationResult failure(List<String> errorMessages) {
        return new PermissionValidationResult(false, new ArrayList<>(errorMessages), 
                                            Collections.emptyList(), Collections.emptyList());
    }
    
    public static PermissionValidationResult withWarnings(List<String> warningMessages) {
        return new PermissionValidationResult(true, Collections.emptyList(), 
                                            new ArrayList<>(warningMessages), Collections.emptyList());
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
    
    public boolean hasInvalidPermissions() {
        return !invalidPermissions.isEmpty();
    }
    
    public List<String> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }
    
    public List<String> getWarningMessages() {
        return Collections.unmodifiableList(warningMessages);
    }
    
    public List<Permission> getInvalidPermissions() {
        return Collections.unmodifiableList(invalidPermissions);
    }
    
    public String getFirstErrorMessage() {
        return errorMessages.isEmpty() ? null : errorMessages.get(0);
    }
    
    /**
     * 合并两个权限验证结果
     */
    public PermissionValidationResult merge(PermissionValidationResult other) {
        Objects.requireNonNull(other, "其他验证结果不能为空");
        
        List<String> mergedErrors = new ArrayList<>(this.errorMessages);
        mergedErrors.addAll(other.errorMessages);
        
        List<String> mergedWarnings = new ArrayList<>(this.warningMessages);
        mergedWarnings.addAll(other.warningMessages);
        
        List<Permission> mergedInvalidPermissions = new ArrayList<>(this.invalidPermissions);
        mergedInvalidPermissions.addAll(other.invalidPermissions);
        
        boolean mergedValid = this.valid && other.valid;
        
        return new PermissionValidationResult(mergedValid, mergedErrors, mergedWarnings, mergedInvalidPermissions);
    }
    
    @Override
    public String toString() {
        return "PermissionValidationResult{" +
                "valid=" + valid +
                ", errorCount=" + errorMessages.size() +
                ", warningCount=" + warningMessages.size() +
                ", invalidPermissionCount=" + invalidPermissions.size() +
                '}';
    }
}