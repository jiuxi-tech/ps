package com.jiuxi.module.auth.infra.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuxi.module.auth.domain.entity.Role;
import com.jiuxi.module.auth.domain.repo.RoleRepository;
import com.jiuxi.module.auth.infra.persistence.entity.RolePO;
import com.jiuxi.module.auth.infra.persistence.mapper.RoleMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色仓储实现
 * 实现角色聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Repository
public class RoleRepositoryImpl extends ServiceImpl<RoleMapper, RolePO> implements RoleRepository {
    
    private final RoleMapper roleMapper;
    
    public RoleRepositoryImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
    
    /**
     * 保存角色
     * @param role 角色聚合根
     * @return 保存后的角色
     */
    @Override
    public Role save(Role role) {
        RolePO rolePO = toPO(role);
        if (rolePO.getId() == null) {
            roleMapper.insert(rolePO);
        } else {
            roleMapper.updateById(rolePO);
        }
        return toEntity(rolePO);
    }
    
    /**
     * 根据ID查找角色
     * @param roleId 角色ID
     * @return 角色Optional
     */
    @Override
    public Optional<Role> findById(String roleId) {
        RolePO rolePO = roleMapper.selectById(roleId);
        return Optional.ofNullable(rolePO).map(this::toEntity);
    }
    
    /**
     * 根据ID删除角色
     * @param roleId 角色ID
     */
    @Override
    public void deleteById(String roleId) {
        roleMapper.deleteById(roleId);
    }
    
    /**
     * 根据角色编码查找角色
     * @param roleCode 角色编码
     * @param tenantId 租户ID
     * @return 角色Optional
     */
    @Override
    public Optional<Role> findByRoleCode(String roleCode, String tenantId) {
        QueryWrapper<RolePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", roleCode);
        queryWrapper.eq("tenant_id", tenantId);
        RolePO rolePO = roleMapper.selectOne(queryWrapper);
        return Optional.ofNullable(rolePO).map(this::toEntity);
    }
    
    /**
     * 将Role实体转换为RolePO
     * @param role 角色实体
     * @return 角色持久化对象
     */
    private RolePO toPO(Role role) {
        if (role == null) {
            return null;
        }
        
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
        
        return rolePO;
    }
    
    /**
     * 将RolePO转换为Role实体
     * @param rolePO 角色持久化对象
     * @return 角色实体
     */
    private Role toEntity(RolePO rolePO) {
        if (rolePO == null) {
            return null;
        }
        
        Role role = new Role();
        role.setRoleId(rolePO.getId());
        role.setRoleCode(rolePO.getRoleCode());
        role.setRoleName(rolePO.getRoleName());
        role.setRoleDesc(rolePO.getRoleDesc());
        role.setRoleType(rolePO.getRoleType() != null ? 
            com.jiuxi.module.auth.domain.entity.RoleType.valueOf(rolePO.getRoleType()) : null);
        role.setStatus(rolePO.getStatus() != null ? 
            com.jiuxi.module.auth.domain.entity.RoleStatus.fromCode(rolePO.getStatus()) : null);
        role.setBuiltIn(rolePO.getBuiltIn());
        role.setDeptId(rolePO.getDeptId());
        role.setDataScope(rolePO.getDataScope() != null ? 
            com.jiuxi.module.auth.domain.entity.DataScope.valueOf(rolePO.getDataScope()) : null);
        role.setOrderIndex(rolePO.getOrderIndex());
        role.setCreator(rolePO.getCreator());
        role.setCreateTime(rolePO.getCreateTime());
        role.setUpdator(rolePO.getUpdator());
        role.setUpdateTime(rolePO.getUpdateTime());
        role.setTenantId(rolePO.getTenantId());
        
        return role;
    }
}