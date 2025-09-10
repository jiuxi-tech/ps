package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.domain.entity.Permission;
import com.jiuxi.module.auth.domain.repo.PermissionRepository;
import com.jiuxi.module.auth.domain.service.PermissionDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    
    public PermissionApplicationService(PermissionRepository permissionRepository, 
                                     PermissionDomainService permissionDomainService) {
        this.permissionRepository = permissionRepository;
        this.permissionDomainService = permissionDomainService;
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
}