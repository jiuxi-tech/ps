package com.jiuxi.module.role.app.command.handler;

import com.jiuxi.module.role.app.command.dto.AssignPermissionsCommand;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.repo.PermissionRepository;
import com.jiuxi.module.role.domain.service.RoleAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分配权限命令处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModuleAssignPermissionsHandler")
public class AssignPermissionsCommandHandler {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private RoleAuthorizationService roleAuthorizationService;
    
    /**
     * 处理分配权限命令
     * 
     * @param command 分配权限命令
     * @return 是否分配成功
     */
    @Transactional
    public boolean handle(@Valid AssignPermissionsCommand command) {
        Objects.requireNonNull(command, "分配权限命令不能为空");
        
        // 查找角色
        RoleId roleId = RoleId.of(command.getRoleId());
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        
        if (!roleOptional.isPresent()) {
            throw new IllegalArgumentException("角色不存在: " + command.getRoleId());
        }
        
        Role role = roleOptional.get();
        
        // 验证操作权限
        validateAssignPermission(command, role);
        
        // 根据菜单ID查询权限
        List<Permission> permissions = command.getMenuIds().stream()
                .map(menuId -> permissionRepository.findByMenuId(menuId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        
        if (permissions.size() != command.getMenuIds().size()) {
            throw new IllegalArgumentException("部分权限不存在");
        }
        
        // 使用领域服务分配权限
        try {
            roleAuthorizationService.assignPermissions(role, permissions);
            
            // 保存角色（包含新的权限分配）
            roleRepository.save(role);
            
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("权限分配失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证分配权限的操作权限
     */
    private void validateAssignPermission(AssignPermissionsCommand command, Role role) {
        // 检查是否是角色创建者或有管理权限
        if (!command.getOperatorId().equals(role.getCreator())) {
            // 这里可以通过roleAuthorizationService检查管理权限
            // 简化处理
            throw new IllegalArgumentException("只有角色创建者可以分配权限");
        }
        
        // 检查角色状态
        if (!role.isEnabled()) {
            throw new IllegalArgumentException("已禁用的角色无法分配权限");
        }
    }
}