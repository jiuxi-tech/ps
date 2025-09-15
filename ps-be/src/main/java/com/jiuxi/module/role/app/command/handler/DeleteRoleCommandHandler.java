package com.jiuxi.module.role.app.command.handler;

import com.jiuxi.module.role.app.command.dto.DeleteRoleCommand;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.service.PermissionValidationService;
import com.jiuxi.module.role.domain.service.RoleAuthorizationService;
import com.jiuxi.module.role.domain.service.RoleValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.Arrays;

/**
 * 删除角色命令处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModuleDeleteCommandHandler")
public class DeleteRoleCommandHandler {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionValidationService permissionValidationService;
    
    @Autowired
    private RoleAuthorizationService roleAuthorizationService;
    
    /**
     * 处理删除角色命令
     * 
     * @param command 删除角色命令
     * @return 是否删除成功
     */
    @Transactional
    public boolean handle(@Valid DeleteRoleCommand command) {
        Objects.requireNonNull(command, "删除角色命令不能为空");
        
        // 查找角色
        RoleId roleId = RoleId.of(command.getRoleId());
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        
        if (!roleOptional.isPresent()) {
            throw new IllegalArgumentException("角色不存在: " + command.getRoleId());
        }
        
        Role role = roleOptional.get();
        
        // 验证删除权限
        validateDeletePermission(command, role);
        
        // 验证是否可以删除
        RoleValidationResult validationResult = permissionValidationService.validateRoleDeletion(role);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("角色无法删除: " + validationResult.getFirstErrorMessage());
        }
        
        // 如果有警告信息，记录日志
        if (validationResult.hasWarnings()) {
            System.out.println("角色删除警告: " + String.join(", ", validationResult.getWarningMessages()));
        }
        
        // 执行删除
        roleRepository.deleteById(roleId);
        
        return true;
    }
    
    /**
     * 验证删除权限
     */
    private void validateDeletePermission(DeleteRoleCommand command, Role role) {
        // 获取操作者的角色列表
        String[] roleIds = command.getOperatorId() != null ? new String[]{} : new String[]{}; // 简化处理
        
        // 使用领域服务验证删除权限
        boolean canDelete = roleAuthorizationService.canDeleteRole(
            command.getOperatorId(),
            Arrays.asList(roleIds),
            role
        );
        
        if (!canDelete) {
            throw new IllegalArgumentException("没有权限删除该角色");
        }
    }
}