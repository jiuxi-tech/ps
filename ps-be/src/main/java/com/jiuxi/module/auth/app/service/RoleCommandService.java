package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.app.command.dto.CreateRoleCommand;
import com.jiuxi.module.auth.app.command.dto.AssignPermissionsCommand;

/**
 * 角色命令服务接口
 * 定义角色相关的变更操作接口
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public interface RoleCommandService {
    
    /**
     * 创建角色
     * @param command 创建角色命令
     * @return 角色ID
     */
    String createRole(CreateRoleCommand command);
    
    /**
     * 分配权限
     * @param command 分配权限命令
     */
    void assignPermissions(AssignPermissionsCommand command);
    
    /**
     * 启用角色
     * @param roleId 角色ID
     * @param operator 操作者
     */
    void enableRole(String roleId, String operator);
    
    /**
     * 停用角色
     * @param roleId 角色ID
     * @param operator 操作者
     */
    void disableRole(String roleId, String operator);
    
    /**
     * 删除角色
     * @param roleId 角色ID
     * @param operator 操作者
     */
    void deleteRole(String roleId, String operator);
}