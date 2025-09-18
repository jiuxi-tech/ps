package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.domain.model.entity.Permission;
import com.jiuxi.module.auth.domain.model.entity.PermissionType;
import com.jiuxi.module.auth.domain.repo.PermissionRepository;
import com.jiuxi.module.auth.domain.service.PermissionDomainService;
import com.jiuxi.module.auth.domain.service.PermissionCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 权限应用服务
 * 负责权限相关的应用逻辑和事务协调
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
@Transactional
public class PermissionApplicationService {
    
    private final PermissionRepository permissionRepository;
    private final PermissionDomainService permissionDomainService;
    private final PermissionCacheService permissionCacheService;
    
    public PermissionApplicationService(PermissionRepository permissionRepository, 
                                     PermissionDomainService permissionDomainService,
                                     PermissionCacheService permissionCacheService) {
        this.permissionRepository = permissionRepository;
        this.permissionDomainService = permissionDomainService;
        this.permissionCacheService = permissionCacheService;
    }
    
    /**
     * 创建权限
     * @param permissionCode 权限编码
     * @param permissionName 权限名称
     * @param permissionDesc 权限描述
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 权限ID
     */
    public String createPermission(String permissionCode, String permissionName, String permissionDesc, 
                                 String operator, String tenantId) {
        // 创建权限实体
        Permission permission = new Permission(permissionCode, permissionName, 
                                            permissionDomainService.getDefaultPermissionType());
        permission.setPermissionId(UUID.randomUUID().toString());
        permission.setPermissionDesc(permissionDesc);
        permission.setCreator(operator);
        permission.setCreateTime(LocalDateTime.now());
        permission.setTenantId(tenantId);
        
        // 业务规则验证
        permissionDomainService.validateForCreate(permission, tenantId);
        
        // 保存权限
        Permission savedPermission = permissionRepository.save(permission);
        
        return savedPermission.getPermissionId();
    }
    
    /**
     * 更新权限
     * @param permissionId 权限ID
     * @param permissionName 权限名称
     * @param permissionDesc 权限描述
     * @param operator 操作者
     */
    public void updatePermission(String permissionId, String permissionName, String permissionDesc, 
                               String operator) {
        // 查找现有权限
        Optional<Permission> existingPermissionOpt = permissionRepository.findById(permissionId);
        if (existingPermissionOpt.isEmpty()) {
            throw new IllegalArgumentException("权限不存在: " + permissionId);
        }
        
        Permission existingPermission = existingPermissionOpt.get();
        
        // 更新权限信息
        existingPermission.setPermissionName(permissionName);
        existingPermission.setPermissionDesc(permissionDesc);
        existingPermission.setUpdator(operator);
        existingPermission.setUpdateTime(LocalDateTime.now());
        
        // 业务规则验证
        permissionDomainService.validateForUpdate(existingPermission);
        
        // 保存权限
        permissionRepository.save(existingPermission);
    }
    
    /**
     * 删除权限
     * @param permissionId 权限ID
     */
    public void deletePermission(String permissionId) {
        // 查找现有权限
        Optional<Permission> existingPermissionOpt = permissionRepository.findById(permissionId);
        if (existingPermissionOpt.isEmpty()) {
            throw new IllegalArgumentException("权限不存在: " + permissionId);
        }
        
        Permission existingPermission = existingPermissionOpt.get();
        
        // 业务规则验证
        permissionDomainService.validateForDelete(permissionId);
        
        // 删除权限
        permissionRepository.deleteById(permissionId);
    }
    
    /**
     * 启用权限
     * @param permissionId 权限ID
     * @param operator 操作者
     */
    public void enablePermission(String permissionId, String operator) {
        Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);
        if (permissionOpt.isEmpty()) {
            throw new IllegalArgumentException("权限不存在: " + permissionId);
        }
        
        Permission permission = permissionOpt.get();
        permission.enable();
        permission.setUpdator(operator);
        permission.setUpdateTime(LocalDateTime.now());
        
        permissionRepository.save(permission);
    }
    
    /**
     * 停用权限
     * @param permissionId 权限ID
     * @param operator 操作者
     */
    public void disablePermission(String permissionId, String operator) {
        Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);
        if (permissionOpt.isEmpty()) {
            throw new IllegalArgumentException("权限不存在: " + permissionId);
        }
        
        Permission permission = permissionOpt.get();
        permission.disable();
        permission.setUpdator(operator);
        permission.setUpdateTime(LocalDateTime.now());
        
        permissionRepository.save(permission);
    }
    
    /**
     * 根据ID获取权限
     * @param permissionId 权限ID
     * @return 权限对象
     */
    @Transactional(readOnly = true)
    public Permission getPermissionById(String permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在: " + permissionId));
    }
    
    /**
     * 根据权限编码获取权限
     * @param permissionCode 权限编码
     * @param tenantId 租户ID
     * @return 权限对象
     */
    @Transactional(readOnly = true)
    public Permission getPermissionByCode(String permissionCode, String tenantId) {
        return permissionRepository.findByPermissionCode(permissionCode, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在: " + permissionCode));
    }
    
    /**
     * 创建API权限
     * @param permissionCode 权限编码
     * @param permissionName 权限名称
     * @param resourceUri 资源URI
     * @param httpMethod HTTP方法
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 权限ID
     */
    public String createApiPermission(String permissionCode, String permissionName, String resourceUri,
                                    String httpMethod, String operator, String tenantId) {
        // 创建API权限实体
        Permission permission = new Permission(permissionCode, permissionName, PermissionType.API);
        permission.setPermissionId(UUID.randomUUID().toString());
        permission.setResourcePath(resourceUri);
        permission.setHttpMethod(httpMethod);
        permission.setCreator(operator);
        permission.setCreateTime(LocalDateTime.now());
        permission.setTenantId(tenantId);
        
        // 业务规则验证
        permissionDomainService.validateForCreate(permission, tenantId);
        
        // 保存权限
        Permission savedPermission = permissionRepository.save(permission);
        
        return savedPermission.getPermissionId();
    }
    
    /**
     * 创建菜单权限
     * @param permissionCode 权限编码
     * @param permissionName 权限名称
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 权限ID
     */
    public String createMenuPermission(String permissionCode, String permissionName,
                                     String operator, String tenantId) {
        // 创建菜单权限实体
        Permission permission = new Permission(permissionCode, permissionName, PermissionType.MENU);
        permission.setPermissionId(UUID.randomUUID().toString());
        permission.setCreator(operator);
        permission.setCreateTime(LocalDateTime.now());
        permission.setTenantId(tenantId);
        
        // 业务规则验证
        permissionDomainService.validateForCreate(permission, tenantId);
        
        // 保存权限
        Permission savedPermission = permissionRepository.save(permission);
        
        return savedPermission.getPermissionId();
    }
    
    /**
     * 创建按钮权限
     * @param permissionCode 权限编码
     * @param permissionName 权限名称
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 权限ID
     */
    public String createButtonPermission(String permissionCode, String permissionName,
                                       String operator, String tenantId) {
        // 创建按钮权限实体
        Permission permission = new Permission(permissionCode, permissionName, PermissionType.BUTTON);
        permission.setPermissionId(UUID.randomUUID().toString());
        permission.setCreator(operator);
        permission.setCreateTime(LocalDateTime.now());
        permission.setTenantId(tenantId);
        
        // 业务规则验证
        permissionDomainService.validateForCreate(permission, tenantId);
        
        // 保存权限
        Permission savedPermission = permissionRepository.save(permission);
        
        return savedPermission.getPermissionId();
    }
    
    /**
     * 根据权限类型获取权限列表
     * @param permissionType 权限类型
     * @param tenantId 租户ID
     * @return 权限列表
     */
    @Transactional(readOnly = true)
    public List<Permission> getPermissionsByType(PermissionType permissionType, String tenantId) {
        return permissionRepository.findByPermissionType(permissionType, tenantId);
    }
    
    /**
     * 根据资源URI查找API权限
     * @param resourceUri 资源URI
     * @param httpMethod HTTP方法（可选）
     * @param tenantId 租户ID
     * @return 权限列表
     */
    @Transactional(readOnly = true)
    public List<Permission> getApiPermissionsByResource(String resourceUri, String httpMethod, String tenantId) {
        return permissionRepository.findApiPermissionsByResource(resourceUri, httpMethod, tenantId);
    }
    
    /**
     * 获取租户的所有激活权限
     * @param tenantId 租户ID
     * @return 权限列表
     */
    @Transactional(readOnly = true)
    public List<Permission> getActivePermissions(String tenantId) {
        return permissionRepository.findActivePermissionsByTenant(tenantId);
    }
    
    /**
     * 批量获取权限
     * @param permissionIds 权限ID列表
     * @param tenantId 租户ID
     * @return 权限列表
     */
    @Transactional(readOnly = true)
    public List<Permission> getPermissionsByIds(List<String> permissionIds, String tenantId) {
        return permissionRepository.findByIds(permissionIds, tenantId);
    }
    
    /**
     * 检查权限编码是否存在
     * @param permissionCode 权限编码
     * @param tenantId 租户ID
     * @param excludePermissionId 排除的权限ID（用于更新时检查）
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean isPermissionCodeExists(String permissionCode, String tenantId, String excludePermissionId) {
        return permissionRepository.existsByPermissionCode(permissionCode, tenantId, excludePermissionId);
    }
    
    /**
     * 更新权限缓存
     * @param permissionId 权限ID
     * @param tenantId 租户ID
     */
    public void updatePermissionCache(String permissionId, String tenantId) {
        Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);
        if (permissionOpt.isPresent()) {
            Permission permission = permissionOpt.get();
            permissionCacheService.cachePermission(permissionId, tenantId, permission);
        } else {
            // 权限不存在，清除缓存
            permissionCacheService.evictPermissionCache(permissionId, tenantId);
        }
    }
    
    /**
     * 清除权限相关缓存
     * @param tenantId 租户ID
     */
    public void clearPermissionCaches(String tenantId) {
        permissionCacheService.evictAllPermissionCaches();
    }
}