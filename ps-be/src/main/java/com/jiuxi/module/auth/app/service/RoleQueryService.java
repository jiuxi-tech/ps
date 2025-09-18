package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.app.query.dto.RoleQueryDTO;
import com.jiuxi.module.auth.app.query.dto.PermissionQueryDTO;
import com.jiuxi.module.auth.app.query.dto.MenuQueryDTO;

import java.util.List;

/**
 * 角色查询服务接口
 * 定义角色相关的查询操作接口
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public interface RoleQueryService {
    
    /**
     * 根据ID查询角色
     * @param roleId 角色ID
     * @return 角色查询DTO
     */
    RoleQueryDTO getRoleById(String roleId);
    
    /**
     * 根据角色编码查询角色
     * @param roleCode 角色编码
     * @param tenantId 租户ID
     * @return 角色查询DTO
     */
    RoleQueryDTO getRoleByCode(String roleCode, String tenantId);
    
    /**
     * 获取角色的有效权限（包括继承权限）
     * @param roleId 角色ID
     * @return 权限查询DTO列表
     */
    List<PermissionQueryDTO> getRoleEffectivePermissions(String roleId);
    
    /**
     * 获取角色的有效菜单（包括继承菜单）
     * @param roleId 角色ID
     * @return 菜单查询DTO列表
     */
    List<MenuQueryDTO> getRoleEffectiveMenus(String roleId);
    
    /**
     * 获取子角色列表
     * @param roleId 角色ID
     * @return 角色查询DTO列表
     */
    List<RoleQueryDTO> getChildRoles(String roleId);
    
    /**
     * 获取租户的所有根角色
     * @param tenantId 租户ID
     * @return 角色查询DTO列表
     */
    List<RoleQueryDTO> getRootRoles(String tenantId);
}