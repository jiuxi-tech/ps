package com.jiuxi.module.auth.domain.repo;

import com.jiuxi.module.auth.domain.entity.Permission;
import java.util.Optional;

/**
 * 权限仓储接口
 * 定义权限实体的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public interface PermissionRepository {
    
    /**
     * 保存权限
     * @param permission 权限实体
     * @return 保存后的权限
     */
    Permission save(Permission permission);
    
    /**
     * 根据ID查找权限
     * @param permissionId 权限ID
     * @return 权限Optional
     */
    Optional<Permission> findById(String permissionId);
    
    /**
     * 根据ID删除权限
     * @param permissionId 权限ID
     */
    void deleteById(String permissionId);
    
    /**
     * 根据权限编码查找权限
     * @param permissionCode 权限编码
     * @param tenantId 租户ID
     * @return 权限Optional
     */
    Optional<Permission> findByPermissionCode(String permissionCode, String tenantId);
}