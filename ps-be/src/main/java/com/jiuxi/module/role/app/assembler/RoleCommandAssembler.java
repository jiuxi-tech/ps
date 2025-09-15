package com.jiuxi.module.role.app.assembler;

import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.module.role.app.command.dto.CreateRoleCommand;
import com.jiuxi.module.role.app.command.dto.UpdateRoleCommand;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 角色命令转换器
 * 负责命令DTO与领域对象之间的转换
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Component("roleModuleCommandAssembler")
public class RoleCommandAssembler {
    
    /**
     * 从TpRoleVO转换为CreateRoleCommand
     */
    public CreateRoleCommand toCreateCommand(TpRoleVO vo, String operatorId, String applicationId) {
        Objects.requireNonNull(vo, "TpRoleVO不能为空");
        Objects.requireNonNull(operatorId, "操作者ID不能为空");
        
        CreateRoleCommand command = new CreateRoleCommand();
        command.setRoleName(vo.getRoleName());
        command.setRoleDesc(vo.getRoleDesc());
        command.setCategory(vo.getCategory());
        command.setCreatorId(operatorId);
        // TpRoleVO没有remark字段，使用extend03作为替代
        command.setRemark(vo.getExtend03());
        command.setApplicationId(applicationId);
        
        return command;
    }
    
    /**
     * 从TpRoleVO转换为UpdateRoleCommand
     */
    public UpdateRoleCommand toUpdateCommand(TpRoleVO vo, String operatorId) {
        Objects.requireNonNull(vo, "TpRoleVO不能为空");
        Objects.requireNonNull(operatorId, "操作者ID不能为空");
        
        UpdateRoleCommand command = new UpdateRoleCommand();
        command.setRoleId(vo.getRoleId());
        command.setRoleName(vo.getRoleName());
        command.setRoleDesc(vo.getRoleDesc());
        // TpRoleVO没有remark字段，使用extend03作为替代
        command.setRemark(vo.getExtend03());
        command.setOperatorId(operatorId);
        
        return command;
    }
    
    /**
     * 验证CreateRoleCommand的数据完整性
     */
    public void validateCreateCommand(CreateRoleCommand command) {
        Objects.requireNonNull(command, "创建角色命令不能为空");
        
        if (command.getRoleName() == null || command.getRoleName().trim().isEmpty()) {
            throw new IllegalArgumentException("角色名称不能为空");
        }
        
        if (command.getCategory() == null) {
            throw new IllegalArgumentException("角色类别不能为空");
        }
        
        if (command.getCreatorId() == null || command.getCreatorId().trim().isEmpty()) {
            throw new IllegalArgumentException("创建者ID不能为空");
        }
        
        // 验证角色名称长度
        if (command.getRoleName().length() < 2 || command.getRoleName().length() > 50) {
            throw new IllegalArgumentException("角色名称长度应为2-50个字符");
        }
        
        // 验证描述长度
        if (command.getRoleDesc() != null && command.getRoleDesc().length() > 200) {
            throw new IllegalArgumentException("角色描述长度不能超过200个字符");
        }
        
        // 验证备注长度
        if (command.getRemark() != null && command.getRemark().length() > 500) {
            throw new IllegalArgumentException("备注长度不能超过500个字符");
        }
    }
    
    /**
     * 验证UpdateRoleCommand的数据完整性
     */
    public void validateUpdateCommand(UpdateRoleCommand command) {
        Objects.requireNonNull(command, "更新角色命令不能为空");
        
        if (command.getRoleId() == null || command.getRoleId().trim().isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        if (command.getRoleName() == null || command.getRoleName().trim().isEmpty()) {
            throw new IllegalArgumentException("角色名称不能为空");
        }
        
        if (command.getOperatorId() == null || command.getOperatorId().trim().isEmpty()) {
            throw new IllegalArgumentException("操作者ID不能为空");
        }
        
        // 验证角色名称长度
        if (command.getRoleName().length() < 2 || command.getRoleName().length() > 50) {
            throw new IllegalArgumentException("角色名称长度应为2-50个字符");
        }
        
        // 验证描述长度
        if (command.getRoleDesc() != null && command.getRoleDesc().length() > 200) {
            throw new IllegalArgumentException("角色描述长度不能超过200个字符");
        }
        
        // 验证备注长度
        if (command.getRemark() != null && command.getRemark().length() > 500) {
            throw new IllegalArgumentException("备注长度不能超过500个字符");
        }
    }
    
    /**
     * 为兼容性保留的转换方法：从领域对象转换为TpRoleVO
     */
    public TpRoleVO toVO(Role role) {
        Objects.requireNonNull(role, "角色聚合根不能为空");
        
        TpRoleVO vo = new TpRoleVO();
        vo.setRoleId(role.getRoleId().getValue());
        vo.setRoleName(role.getRoleName().getValue());
        vo.setRoleDesc(role.getRoleDesc());
        vo.setCategory(role.getCategory().getValue());
        vo.setCreator(role.getCreator());
        // TpRoleVO没有remark字段，使用extend03作为替代
        vo.setExtend03(role.getRemark());
        // TpRoleVO中时间字段是String类型，需要转换
        if (role.getCreateTime() != null) {
            vo.setCreateTime(role.getCreateTime().toString());
        }
        if (role.getUpdateTime() != null) {
            vo.setUpdateTime(role.getUpdateTime().toString());
        }
        // TpRoleVO使用actived字段表示启用状态，不是isEnabled
        vo.setActived(role.isEnabled() ? 1 : 0);
        
        return vo;
    }
}