package com.jiuxi.module.role.app.command.handler;

import com.jiuxi.module.role.app.command.dto.UpdateRoleCommand;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.service.PermissionValidationService;
import com.jiuxi.module.role.domain.service.RoleValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * 更新角色命令处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModuleUpdateCommandHandler")
public class UpdateRoleCommandHandler {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionValidationService permissionValidationService;
    
    /**
     * 处理更新角色命令
     * 
     * @param command 更新角色命令
     * @return 是否更新成功
     */
    @Transactional
    public boolean handle(@Valid UpdateRoleCommand command) {
        Objects.requireNonNull(command, "更新角色命令不能为空");
        
        // 查找角色
        RoleId roleId = RoleId.of(command.getRoleId());
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        
        if (!roleOptional.isPresent()) {
            throw new IllegalArgumentException("角色不存在: " + command.getRoleId());
        }
        
        Role role = roleOptional.get();
        
        // 检查操作权限
        validateUpdatePermission(command, role);
        
        // 更新角色信息
        role.updateInfo(
            command.getRoleName(),
            command.getRoleDesc(),
            command.getRemark()
        );
        
        // 验证更新后的角色信息
        RoleValidationResult validationResult = permissionValidationService.validateRoleName(role);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("角色验证失败: " + validationResult.getFirstErrorMessage());
        }
        
        // 如果有警告信息，记录日志
        if (validationResult.hasWarnings()) {
            // 这里可以记录警告日志
            System.out.println("角色更新警告: " + String.join(", ", validationResult.getWarningMessages()));
        }
        
        // 政府角色特殊验证
        if (role.getCategory().isGovernment()) {
            RoleValidationResult govValidationResult = permissionValidationService.validateGovernmentRole(role);
            if (govValidationResult.hasWarnings()) {
                System.out.println("政府角色警告: " + String.join(", ", govValidationResult.getWarningMessages()));
            }
        }
        
        // 保存更新
        roleRepository.save(role);
        
        return true;
    }
    
    /**
     * 验证更新权限
     */
    private void validateUpdatePermission(UpdateRoleCommand command, Role role) {
        // 检查是否是角色创建者
        if (!command.getOperatorId().equals(role.getCreator())) {
            // 可以通过roleAuthorizationService检查是否有管理权限
            // 这里简化处理，只允许创建者更新
            throw new IllegalArgumentException("只有角色创建者可以更新角色");
        }
        
        // 检查角色状态是否允许更新
        if (!role.isEnabled()) {
            throw new IllegalArgumentException("已禁用的角色无法更新");
        }
    }
}