package com.jiuxi.module.role.app.assembler;

import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.module.role.app.query.dto.RolePageQuery;
import com.jiuxi.module.role.app.query.dto.RoleAuthQuery;
import com.jiuxi.module.role.app.query.dto.RoleDetailQuery;
import com.jiuxi.module.role.app.query.dto.PermissionTreeQuery;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色查询转换器
 * 负责查询DTO与领域对象、遗留DTO之间的转换
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Component("roleModuleQueryAssembler")
public class RoleQueryAssembler {
    
    /**
     * 从遗留的TpRoleQuery转换为新的RolePageQuery
     */
    public RolePageQuery toPageQuery(TpRoleQuery legacyQuery, String operatorId, String operatorRoleIds, String applicationId) {
        Objects.requireNonNull(legacyQuery, "TpRoleQuery不能为空");
        
        RolePageQuery query = new RolePageQuery();
        query.setRoleName(legacyQuery.getRoleName());
        // TpRoleQuery没有getRoleDesc和getCreator方法，使用默认值或从其他字段获取
        query.setRoleDesc(null);
        query.setCategory(legacyQuery.getCategory());
        query.setCreator(null);
        query.setApplicationId(applicationId);
        query.setCurrent(legacyQuery.getCurrent());
        query.setSize(legacyQuery.getSize());
        query.setOperatorId(operatorId);
        query.setOperatorRoleIds(operatorRoleIds);
        
        return query;
    }
    
    /**
     * 从遗留的TpRoleAuthQuery转换为新的RoleAuthQuery
     */
    public RoleAuthQuery toAuthQuery(TpRoleAuthQuery legacyQuery, String operatorId, String deptId, String operatorRoleIds, String applicationId) {
        Objects.requireNonNull(legacyQuery, "TpRoleAuthQuery不能为空");
        
        RoleAuthQuery query = new RoleAuthQuery();
        // TpRoleAuthQuery没有getRoleName, getRoleDesc, getCategory方法
        query.setRoleName(null);
        query.setRoleDesc(null);
        query.setCategory(null);
        query.setApplicationId(applicationId);
        query.setOperatorId(operatorId);
        query.setDeptId(deptId);
        query.setOperatorRoleIds(operatorRoleIds);
        
        return query;
    }
    
    /**
     * 创建角色详情查询
     */
    public RoleDetailQuery toDetailQuery(String roleId, String operatorId, String operatorRoleIds) {
        if (roleId == null || roleId.trim().isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        RoleDetailQuery query = new RoleDetailQuery();
        query.setRoleId(roleId);
        query.setOperatorId(operatorId);
        query.setOperatorRoleIds(operatorRoleIds);
        
        return query;
    }
    
    /**
     * 创建权限树查询
     */
    public PermissionTreeQuery toPermissionTreeQuery(String roleId, String operatorId, String operatorRoleIds) {
        if (roleId == null || roleId.trim().isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        PermissionTreeQuery query = new PermissionTreeQuery();
        query.setRoleId(roleId);
        query.setOperatorId(operatorId);
        query.setOperatorRoleIds(operatorRoleIds);
        
        return query;
    }
    
    /**
     * 从领域对象转换为TpRoleVO（用于查询结果）
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
    
    /**
     * 批量转换为TpRoleVO列表
     */
    public List<TpRoleVO> toVOList(List<Role> roles) {
        Objects.requireNonNull(roles, "角色列表不能为空");
        
        return roles.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }
    
    /**
     * 验证查询参数的合法性
     */
    public void validatePageQuery(RolePageQuery query) {
        Objects.requireNonNull(query, "分页查询条件不能为空");
        
        if (query.getCurrent() < 1) {
            query.setCurrent(1);
        }
        
        if (query.getSize() < 1 || query.getSize() > 100) {
            query.setSize(10);
        }
        
        // 清理空字符串参数
        if (query.getRoleName() != null && query.getRoleName().trim().isEmpty()) {
            query.setRoleName(null);
        }
        
        if (query.getRoleDesc() != null && query.getRoleDesc().trim().isEmpty()) {
            query.setRoleDesc(null);
        }
        
        if (query.getCreator() != null && query.getCreator().trim().isEmpty()) {
            query.setCreator(null);
        }
        
        if (query.getApplicationId() != null && query.getApplicationId().trim().isEmpty()) {
            query.setApplicationId(null);
        }
    }
    
    /**
     * 验证授权查询参数的合法性
     */
    public void validateAuthQuery(RoleAuthQuery query) {
        Objects.requireNonNull(query, "授权查询条件不能为空");
        
        // 清理空字符串参数
        if (query.getRoleName() != null && query.getRoleName().trim().isEmpty()) {
            query.setRoleName(null);
        }
        
        if (query.getRoleDesc() != null && query.getRoleDesc().trim().isEmpty()) {
            query.setRoleDesc(null);
        }
        
        if (query.getApplicationId() != null && query.getApplicationId().trim().isEmpty()) {
            query.setApplicationId(null);
        }
    }
    
    /**
     * 创建角色人员关系VO（保持兼容性）
     */
    public TpPersonRoleVO toPersonRoleVO(String personId, String roleId, String roleName, String deptId) {
        TpPersonRoleVO vo = new TpPersonRoleVO();
        vo.setPersonId(personId);
        // TpPersonRoleVO没有setPersonName方法，只有personId, roleId, roleName, deptId
        vo.setRoleId(roleId);
        vo.setRoleName(roleName);
        vo.setDeptId(deptId);
        
        return vo;
    }
}