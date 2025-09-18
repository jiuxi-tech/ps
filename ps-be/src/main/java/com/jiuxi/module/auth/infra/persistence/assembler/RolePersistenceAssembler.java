package com.jiuxi.module.auth.infra.persistence.assembler;

import com.jiuxi.module.auth.domain.model.entity.Role;
import com.jiuxi.module.auth.domain.model.entity.RoleStatus;
import com.jiuxi.module.auth.domain.model.entity.RoleType;
import com.jiuxi.module.auth.domain.model.entity.DataScope;
import com.jiuxi.module.auth.infra.persistence.entity.RolePO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色持久化装配器
 * 负责领域对象与持久化对象之间的转换
 * 实现统一的转换规范和异常处理
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component("authRolePersistenceAssembler")
public class RolePersistenceAssembler {
    
    /**
     * 将Role领域对象转换为RolePO持久化对象
     * @param role 角色领域对象
     * @return 角色持久化对象
     */
    public RolePO toPO(Role role) {
        if (role == null) {
            return null;
        }
        
        try {
            RolePO rolePO = new RolePO();
            rolePO.setId(role.getRoleId());
            rolePO.setRoleCode(role.getRoleCode());
            rolePO.setRoleName(role.getRoleName());
            rolePO.setRoleDesc(role.getRoleDesc());
            rolePO.setRoleType(role.getRoleType() != null ? role.getRoleType().getCode() : null);
            rolePO.setStatus(role.getStatus() != null ? role.getStatus().getCode() : null);
            rolePO.setBuiltIn(role.getBuiltIn());
            rolePO.setDeptId(role.getDeptId());
            rolePO.setDataScope(role.getDataScope() != null ? role.getDataScope().name() : null);
            rolePO.setOrderIndex(role.getOrderIndex());
            rolePO.setCreator(role.getCreator());
            rolePO.setCreateTime(role.getCreateTime());
            rolePO.setUpdator(role.getUpdator());
            rolePO.setUpdateTime(role.getUpdateTime());
            rolePO.setTenantId(role.getTenantId());
            
            // 权限继承相关字段
            rolePO.setParentRoleId(role.getParentRoleId());
            rolePO.setRoleLevel(role.getRoleLevel());
            rolePO.setRolePath(role.getRolePath());
            rolePO.setInheritParentPermissions(role.getInheritParentPermissions());
            
            return rolePO;
        } catch (Exception e) {
            throw new RuntimeException("Role领域对象转换为持久化对象失败", e);
        }
    }
    
    /**
     * 将RolePO持久化对象转换为Role领域对象
     * @param rolePO 角色持久化对象
     * @return 角色领域对象
     */
    public Role toEntity(RolePO rolePO) {
        if (rolePO == null) {
            return null;
        }
        
        try {
            Role role = new Role();
            role.setRoleId(rolePO.getId());
            role.setRoleCode(rolePO.getRoleCode());
            role.setRoleName(rolePO.getRoleName());
            role.setRoleDesc(rolePO.getRoleDesc());
            
            // 安全转换角色类型枚举
            if (rolePO.getRoleType() != null) {
                try {
                    role.setRoleType(RoleType.fromCode(rolePO.getRoleType()));
                } catch (IllegalArgumentException e) {
                    // 记录警告日志，使用默认值
                    role.setRoleType(RoleType.BUSINESS);
                }
            }
            
            // 安全转换状态枚举
            if (rolePO.getStatus() != null) {
                try {
                    role.setStatus(RoleStatus.fromCode(rolePO.getStatus()));
                } catch (IllegalArgumentException e) {
                    // 记录警告日志，使用默认值
                    role.setStatus(RoleStatus.ACTIVE);
                }
            }
            
            role.setBuiltIn(rolePO.getBuiltIn());
            role.setDeptId(rolePO.getDeptId());
            
            // 安全转换数据权限范围枚举
            if (rolePO.getDataScope() != null) {
                try {
                    role.setDataScope(DataScope.valueOf(rolePO.getDataScope()));
                } catch (IllegalArgumentException e) {
                    // 记录警告日志，使用默认值
                    role.setDataScope(DataScope.DEPT);
                }
            }
            
            role.setOrderIndex(rolePO.getOrderIndex());
            role.setCreator(rolePO.getCreator());
            role.setCreateTime(rolePO.getCreateTime());
            role.setUpdator(rolePO.getUpdator());
            role.setUpdateTime(rolePO.getUpdateTime());
            role.setTenantId(rolePO.getTenantId());
            
            // 权限继承相关字段
            role.setParentRoleId(rolePO.getParentRoleId());
            role.setRoleLevel(rolePO.getRoleLevel());
            role.setRolePath(rolePO.getRolePath());
            role.setInheritParentPermissions(rolePO.getInheritParentPermissions());
            
            return role;
        } catch (Exception e) {
            throw new RuntimeException("RolePO持久化对象转换为领域对象失败: " + rolePO.getId(), e);
        }
    }
    
    /**
     * 批量转换PO列表为实体列表
     * @param rolePOs 持久化对象列表
     * @return 领域对象列表
     */
    public List<Role> toEntityList(List<RolePO> rolePOs) {
        if (rolePOs == null || rolePOs.isEmpty()) {
            return List.of();
        }
        
        return rolePOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量转换实体列表为PO列表
     * @param roles 领域对象列表
     * @return 持久化对象列表
     */
    public List<RolePO> toPOList(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        
        return roles.stream()
                .map(this::toPO)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新RolePO对象，保持ID不变
     * @param target 目标持久化对象
     * @param source 源领域对象
     */
    public void updatePO(RolePO target, Role source) {
        if (target == null || source == null) {
            throw new IllegalArgumentException("目标对象和源对象都不能为空");
        }
        
        try {
            // 保持主键不变
            String originalId = target.getId();
            
            // 复制属性
            RolePO newPO = toPO(source);
            if (newPO != null) {
                // 复制所有字段
                target.setRoleCode(newPO.getRoleCode());
                target.setRoleName(newPO.getRoleName());
                target.setRoleDesc(newPO.getRoleDesc());
                target.setRoleType(newPO.getRoleType());
                target.setStatus(newPO.getStatus());
                target.setBuiltIn(newPO.getBuiltIn());
                target.setDeptId(newPO.getDeptId());
                target.setDataScope(newPO.getDataScope());
                target.setOrderIndex(newPO.getOrderIndex());
                target.setCreator(newPO.getCreator());
                target.setCreateTime(newPO.getCreateTime());
                target.setUpdator(newPO.getUpdator());
                target.setUpdateTime(newPO.getUpdateTime());
                target.setTenantId(newPO.getTenantId());
                target.setParentRoleId(newPO.getParentRoleId());
                target.setRoleLevel(newPO.getRoleLevel());
                target.setRolePath(newPO.getRolePath());
                target.setInheritParentPermissions(newPO.getInheritParentPermissions());
                
                // 恢复原始ID
                target.setId(originalId);
            }
        } catch (Exception e) {
            throw new RuntimeException("更新RolePO对象失败", e);
        }
    }
}