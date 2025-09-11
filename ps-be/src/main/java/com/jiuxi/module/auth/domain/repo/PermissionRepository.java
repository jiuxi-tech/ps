package com.jiuxi.module.auth.domain.repo;

import com.jiuxi.module.auth.domain.entity.Permission;
import com.jiuxi.module.auth.domain.entity.PermissionType;
import java.util.List;
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
    
    /**
     * 根据权限类型查找权限列表
     * @param permissionType 权限类型
     * @param tenantId 租户ID
     * @return 权限列表
     */
    List<Permission> findByPermissionType(PermissionType permissionType, String tenantId);
    
    /**
     * 根据资源URI查找API权限
     * @param resourceUri 资源URI
     * @param httpMethod 请求方法
     * @param tenantId 租户ID
     * @return 权限列表
     */
    List<Permission> findApiPermissionsByResource(String resourceUri, String httpMethod, String tenantId);
    
    /**
     * 根据租户ID查找所有激活的权限
     * @param tenantId 租户ID
     * @return 权限列表
     */
    List<Permission> findActivePermissionsByTenant(String tenantId);
    
    /**
     * 批量查找权限
     * @param permissionIds 权限ID列表
     * @param tenantId 租户ID
     * @return 权限列表
     */
    List<Permission> findByIds(List<String> permissionIds, String tenantId);
    
    /**
     * 检查权限编码是否存在
     * @param permissionCode 权限编码
     * @param tenantId 租户ID
     * @param excludePermissionId 排除的权限ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByPermissionCode(String permissionCode, String tenantId, String excludePermissionId);
}