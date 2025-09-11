package com.jiuxi.module.auth.infra.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuxi.module.auth.domain.entity.Permission;
import com.jiuxi.module.auth.domain.entity.PermissionType;
import com.jiuxi.module.auth.domain.repo.PermissionRepository;
import com.jiuxi.module.auth.infra.persistence.entity.PermissionPO;
import com.jiuxi.module.auth.infra.persistence.mapper.PermissionMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限仓储实现
 * 实现权限实体的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Repository
public class PermissionRepositoryImpl extends ServiceImpl<PermissionMapper, PermissionPO> implements PermissionRepository {
    
    private final PermissionMapper permissionMapper;
    
    public PermissionRepositoryImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }
    
    /**
     * 保存权限
     * @param permission 权限实体
     * @return 保存后的权限
     */
    @Override
    public Permission save(Permission permission) {
        PermissionPO permissionPO = toPO(permission);
        if (permissionPO.getId() == null) {
            permissionMapper.insert(permissionPO);
        } else {
            permissionMapper.updateById(permissionPO);
        }
        return toEntity(permissionPO);
    }
    
    /**
     * 根据ID查找权限
     * @param permissionId 权限ID
     * @return 权限Optional
     */
    @Override
    public Optional<Permission> findById(String permissionId) {
        PermissionPO permissionPO = permissionMapper.selectById(permissionId);
        return Optional.ofNullable(permissionPO).map(this::toEntity);
    }
    
    /**
     * 根据ID删除权限
     * @param permissionId 权限ID
     */
    @Override
    public void deleteById(String permissionId) {
        permissionMapper.deleteById(permissionId);
    }
    
    /**
     * 根据权限编码查找权限
     * @param permissionCode 权限编码
     * @param tenantId 租户ID
     * @return 权限Optional
     */
    @Override
    public Optional<Permission> findByPermissionCode(String permissionCode, String tenantId) {
        QueryWrapper<PermissionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_code", permissionCode);
        queryWrapper.eq("tenant_id", tenantId);
        PermissionPO permissionPO = permissionMapper.selectOne(queryWrapper);
        return Optional.ofNullable(permissionPO).map(this::toEntity);
    }
    
    /**
     * 根据权限类型查找权限列表
     */
    @Override
    public List<Permission> findByPermissionType(PermissionType permissionType, String tenantId) {
        QueryWrapper<PermissionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_type", permissionType.getCode());
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.eq("status", "ACTIVE");
        queryWrapper.orderByAsc("order_index");
        
        List<PermissionPO> permissionPOs = permissionMapper.selectList(queryWrapper);
        return permissionPOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
    /**
     * 根据资源URI查找API权限
     */
    @Override
    public List<Permission> findApiPermissionsByResource(String resourceUri, String httpMethod, String tenantId) {
        QueryWrapper<PermissionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_type", PermissionType.API.getCode());
        queryWrapper.eq("permission_uri", resourceUri);
        if (StringUtils.hasText(httpMethod)) {
            queryWrapper.eq("permission_method", httpMethod.toUpperCase());
        }
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.eq("status", "ACTIVE");
        
        List<PermissionPO> permissionPOs = permissionMapper.selectList(queryWrapper);
        return permissionPOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
    /**
     * 根据租户ID查找所有激活的权限
     */
    @Override
    public List<Permission> findActivePermissionsByTenant(String tenantId) {
        QueryWrapper<PermissionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "ACTIVE");
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByAsc("permission_type", "order_index");
        
        List<PermissionPO> permissionPOs = permissionMapper.selectList(queryWrapper);
        return permissionPOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
    /**
     * 批量查找权限
     */
    @Override
    public List<Permission> findByIds(List<String> permissionIds, String tenantId) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        QueryWrapper<PermissionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", permissionIds);
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByAsc("permission_type", "order_index");
        
        List<PermissionPO> permissionPOs = permissionMapper.selectList(queryWrapper);
        return permissionPOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
    /**
     * 检查权限编码是否存在
     */
    @Override
    public boolean existsByPermissionCode(String permissionCode, String tenantId, String excludePermissionId) {
        QueryWrapper<PermissionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_code", permissionCode);
        queryWrapper.eq("tenant_id", tenantId);
        
        if (StringUtils.hasText(excludePermissionId)) {
            queryWrapper.ne("id", excludePermissionId);
        }
        
        return permissionMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 将Permission实体转换为PermissionPO
     * @param permission 权限实体
     * @return 权限持久化对象
     */
    private PermissionPO toPO(Permission permission) {
        if (permission == null) {
            return null;
        }
        
        PermissionPO permissionPO = new PermissionPO();
        permissionPO.setId(permission.getPermissionId());
        permissionPO.setPermissionCode(permission.getPermissionCode());
        permissionPO.setPermissionName(permission.getPermissionName());
        permissionPO.setPermissionDesc(permission.getPermissionDesc());
        permissionPO.setPermissionType(permission.getPermissionType() != null ? permission.getPermissionType().getCode() : null);
        permissionPO.setStatus(permission.getStatus() != null ? permission.getStatus().getCode() : null);
        permissionPO.setPermissionUri(permission.getResourcePath());
        permissionPO.setPermissionMethod(permission.getHttpMethod());
        permissionPO.setOrderIndex(permission.getOrderIndex());
        permissionPO.setCreator(permission.getCreator());
        permissionPO.setCreateTime(permission.getCreateTime());
        permissionPO.setUpdator(permission.getUpdator());
        permissionPO.setUpdateTime(permission.getUpdateTime());
        permissionPO.setTenantId(permission.getTenantId());
        
        return permissionPO;
    }
    
    /**
     * 将PermissionPO转换为Permission实体
     * @param permissionPO 权限持久化对象
     * @return 权限实体
     */
    private Permission toEntity(PermissionPO permissionPO) {
        if (permissionPO == null) {
            return null;
        }
        
        Permission permission = new Permission();
        permission.setPermissionId(permissionPO.getId());
        permission.setPermissionCode(permissionPO.getPermissionCode());
        permission.setPermissionName(permissionPO.getPermissionName());
        permission.setPermissionDesc(permissionPO.getPermissionDesc());
        permission.setPermissionType(permissionPO.getPermissionType() != null ? 
            com.jiuxi.module.auth.domain.entity.PermissionType.fromCode(permissionPO.getPermissionType()) : null);
        permission.setStatus(permissionPO.getStatus() != null ? 
            com.jiuxi.module.auth.domain.entity.PermissionStatus.fromCode(permissionPO.getStatus()) : null);
        permission.setResourcePath(permissionPO.getPermissionUri());
        permission.setHttpMethod(permissionPO.getPermissionMethod());
        permission.setOrderIndex(permissionPO.getOrderIndex());
        permission.setCreator(permissionPO.getCreator());
        permission.setCreateTime(permissionPO.getCreateTime());
        permission.setUpdator(permissionPO.getUpdator());
        permission.setUpdateTime(permissionPO.getUpdateTime());
        permission.setTenantId(permissionPO.getTenantId());
        
        return permission;
    }
}