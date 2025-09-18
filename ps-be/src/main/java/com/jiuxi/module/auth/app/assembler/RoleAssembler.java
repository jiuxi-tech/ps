package com.jiuxi.module.auth.app.assembler;

import com.jiuxi.module.auth.domain.model.entity.Role;
import com.jiuxi.module.auth.app.dto.RoleResponseDTO;
import org.springframework.stereotype.Component;

/**
 * 角色装配器
 * 负责Role实体和RoleResponseDTO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Component("roleAppAssembler")
public class RoleAssembler {
    
    /**
     * 将Role实体转换为RoleResponseDTO
     * @param role 角色实体
     * @return 角色响应DTO
     */
    public RoleResponseDTO toResponseDTO(Role role) {
        if (role == null) {
            return null;
        }
        
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setRoleId(role.getRoleId());
        dto.setRoleCode(role.getRoleCode());
        dto.setRoleName(role.getRoleName());
        dto.setRoleDesc(role.getRoleDesc());
        dto.setRoleType(role.getRoleType());
        dto.setStatus(role.getStatus());
        dto.setBuiltIn(role.getBuiltIn());
        dto.setDeptId(role.getDeptId());
        dto.setDataScope(role.getDataScope());
        dto.setOrderIndex(role.getOrderIndex());
        dto.setCreator(role.getCreator());
        dto.setCreateTime(role.getCreateTime());
        dto.setUpdator(role.getUpdator());
        dto.setUpdateTime(role.getUpdateTime());
        dto.setTenantId(role.getTenantId());
        
        return dto;
    }
}