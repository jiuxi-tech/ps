package com.jiuxi.module.role.app.command.handler;

import com.jiuxi.module.role.app.command.dto.CreateRoleCommand;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.service.RoleAuthorizationService;
import com.jiuxi.module.role.domain.service.PermissionValidationService;
import com.jiuxi.module.role.domain.service.RoleValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.UUID;
import java.util.Objects;

/**
 * 创建角色命令处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModuleCreateCommandHandler")
public class CreateRoleCommandHandler {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionValidationService permissionValidationService;
    
    @Autowired
    private RoleAuthorizationService roleAuthorizationService;
    
    /**
     * 处理创建角色命令
     * 
     * @param command 创建角色命令
     * @return 新创建的角色ID
     */
    @Transactional
    public String handle(@Valid CreateRoleCommand command) {
        Objects.requireNonNull(command, "创建角色命令不能为空");
        
        // 生成新的角色ID
        String roleId = UUID.randomUUID().toString().replace("-", "");
        
        // 创建角色聚合根
        Role role = Role.create(
            roleId,
            command.getRoleName(),
            command.getRoleDesc(),
            command.getCategory(),
            command.getCreatorId(),
            command.getRemark()
        );
        
        // 验证角色信息
        RoleValidationResult validationResult = permissionValidationService.validateRoleName(role);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("角色验证失败: " + validationResult.getFirstErrorMessage());
        }
        
        // 政府角色特殊验证
        if (role.getCategory().isGovernment()) {
            RoleValidationResult govValidationResult = permissionValidationService.validateGovernmentRole(role);
            if (!govValidationResult.isValid()) {
                throw new IllegalArgumentException("政府角色验证失败: " + govValidationResult.getFirstErrorMessage());
            }
        }
        
        // 保存角色
        roleRepository.save(role);
        
        return roleId;
    }
    
    /**
     * 验证创建者是否有权限创建指定类型的角色
     */
    private void validateCreatePermission(CreateRoleCommand command, Role role) {
        // 这里可以添加更复杂的权限验证逻辑
        // 比如检查创建者是否有权限创建指定类别的角色
        
        // 简化实现：政府角色创建需要特殊权限
        if (role.getCategory().isGovernment()) {
            // 可以通过roleAuthorizationService检查创建者权限
            // 这里简化处理
        }
    }
}