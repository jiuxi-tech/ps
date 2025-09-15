package com.jiuxi.module.role.domain.service;

import com.jiuxi.module.role.domain.model.entity.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 权限冲突检查结果
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class PermissionConflictResult {
    
    private final boolean hasConflicts;
    private final List<ConflictPair> conflicts;
    private final List<String> conflictMessages;
    
    private PermissionConflictResult(boolean hasConflicts, List<ConflictPair> conflicts, List<String> conflictMessages) {
        this.hasConflicts = hasConflicts;
        this.conflicts = Objects.requireNonNull(conflicts, "冲突列表不能为空");
        this.conflictMessages = Objects.requireNonNull(conflictMessages, "冲突消息列表不能为空");
    }
    
    public static PermissionConflictResult noConflict() {
        return new PermissionConflictResult(false, Collections.emptyList(), Collections.emptyList());
    }
    
    public static PermissionConflictResult withConflicts(List<ConflictPair> conflicts, List<String> messages) {
        return new PermissionConflictResult(true, new ArrayList<>(conflicts), new ArrayList<>(messages));
    }
    
    public boolean hasConflicts() {
        return hasConflicts;
    }
    
    public List<ConflictPair> getConflicts() {
        return Collections.unmodifiableList(conflicts);
    }
    
    public List<String> getConflictMessages() {
        return Collections.unmodifiableList(conflictMessages);
    }
    
    public int getConflictCount() {
        return conflicts.size();
    }
    
    /**
     * 权限冲突对
     */
    public static class ConflictPair {
        private final Permission permission1;
        private final Permission permission2;
        private final ConflictType conflictType;
        private final String description;
        
        public ConflictPair(Permission permission1, Permission permission2, ConflictType conflictType, String description) {
            this.permission1 = Objects.requireNonNull(permission1, "权限1不能为空");
            this.permission2 = Objects.requireNonNull(permission2, "权限2不能为空");
            this.conflictType = Objects.requireNonNull(conflictType, "冲突类型不能为空");
            this.description = description;
        }
        
        public Permission getPermission1() {
            return permission1;
        }
        
        public Permission getPermission2() {
            return permission2;
        }
        
        public ConflictType getConflictType() {
            return conflictType;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return "ConflictPair{" +
                    "permission1=" + permission1.getMenuName() +
                    ", permission2=" + permission2.getMenuName() +
                    ", conflictType=" + conflictType +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
    
    /**
     * 冲突类型枚举
     */
    public enum ConflictType {
        DUPLICATE("重复权限"),
        HIERARCHY_CONFLICT("层级冲突"),
        MUTUAL_EXCLUSIVE("互斥权限"),
        CIRCULAR_DEPENDENCY("循环依赖");
        
        private final String description;
        
        ConflictType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    @Override
    public String toString() {
        return "PermissionConflictResult{" +
                "hasConflicts=" + hasConflicts +
                ", conflictCount=" + conflicts.size() +
                '}';
    }
}