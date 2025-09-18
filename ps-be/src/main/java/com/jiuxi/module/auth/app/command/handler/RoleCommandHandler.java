package com.jiuxi.module.auth.app.command.handler;

import com.jiuxi.module.auth.app.command.dto.CreateRoleCommand;
import com.jiuxi.module.auth.app.command.dto.AssignPermissionsCommand;
import com.jiuxi.module.auth.domain.model.entity.Role;
import com.jiuxi.module.auth.domain.model.entity.RoleType;
import com.jiuxi.module.auth.domain.model.entity.DataScope;
import com.jiuxi.module.auth.domain.service.RoleDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 角色命令处理器
 * 处理所有角色相关的变更操作（Create, Update, Delete）
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
@Transactional
public class RoleCommandHandler {
    
    @Autowired
    private RoleDomainService roleDomainService;
    
    /**
     * 处理创建角色命令
     * @param command 创建角色命令
     * @return 角色ID
     */
    public String handle(CreateRoleCommand command) {
        // 验证参数
        validateCreateCommand(command);
        
        // 确定角色类型
        RoleType roleType = StringUtils.hasText(command.getRoleType()) 
            ? RoleType.fromCode(command.getRoleType()) 
            : roleDomainService.getDefaultRoleType();
        
        // 创建角色实体
        Role role = new Role(command.getRoleCode(), command.getRoleName(), roleType);
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleDesc(command.getRoleDesc());
        role.setParentRoleId(command.getParentRoleId());
        role.setInheritParentPermissions(command.getInheritParentPermissions());
        role.setDeptId(command.getDeptId());
        role.setOrderIndex(command.getOrderIndex());
        role.setTenantId(command.getTenantId());
        
        // 设置数据权限范围
        if (StringUtils.hasText(command.getDataScope())) {
            role.setDataScope(DataScope.valueOf(command.getDataScope()));
        }
        
        // 使用领域服务创建角色（包含完整的业务规则验证）
        Role savedRole = roleDomainService.createRole(role, command.getOperator());
        
        // 如果指定了权限和菜单，进行分配
        if ((command.getPermissionIds() != null && !command.getPermissionIds().isEmpty()) ||
            (command.getMenuIds() != null && !command.getMenuIds().isEmpty())) {
            
            roleDomainService.assignPermissions(
                savedRole.getRoleId(), 
                command.getPermissionIds(), 
                command.getMenuIds(), 
                command.getOperator()
            );
        }
        
        return savedRole.getRoleId();
    }
    
    /**
     * 处理分配权限命令
     * @param command 分配权限命令
     */
    public void handle(AssignPermissionsCommand command) {
        // 验证参数
        validateAssignPermissionsCommand(command);
        
        // 使用领域服务分配权限（包含业务规则验证和缓存更新）
        roleDomainService.assignPermissions(
            command.getRoleId(), 
            command.getPermissionIds(), 
            command.getMenuIds(), 
            command.getOperator()
        );
    }
    
    // 验证方法
    private void validateCreateCommand(CreateRoleCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("创建角色命令不能为空");
        }
        
        // 验证角色编码格式
        if (StringUtils.hasText(command.getRoleCode())) {
            String roleCode = command.getRoleCode().trim().toUpperCase();
            if (!roleCode.matches("^[A-Z0-9_]{2,50}$")) {
                throw new IllegalArgumentException("角色编码格式不正确，只能包含大写字母、数字和下划线，长度2-50位");
            }
        }
        
        // 验证角色类型
        if (StringUtils.hasText(command.getRoleType())) {
            try {
                RoleType.fromCode(command.getRoleType());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("无效的角色类型: " + command.getRoleType());
            }
        }
        
        // 验证数据权限范围
        if (StringUtils.hasText(command.getDataScope())) {
            try {
                DataScope.valueOf(command.getDataScope());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("无效的数据权限范围: " + command.getDataScope());
            }
        }
    }
    
    private void validateAssignPermissionsCommand(AssignPermissionsCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("分配权限命令不能为空");
        }
        
        // 至少需要分配权限或菜单中的一种
        if ((command.getPermissionIds() == null || command.getPermissionIds().isEmpty()) &&
            (command.getMenuIds() == null || command.getMenuIds().isEmpty())) {
            throw new IllegalArgumentException("至少需要指定权限或菜单");
        }
    }
}