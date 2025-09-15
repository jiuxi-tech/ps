package com.jiuxi.module.role.infra.persistence.assembler;

import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.*;
import com.jiuxi.module.role.infra.persistence.entity.RolePO;
import com.jiuxi.module.role.infra.persistence.entity.PermissionPO;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色持久化数据转换器
 * 负责领域对象（DO）与持久化对象（PO）之间的转换
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Component
public class RolePersistenceAssembler {
    
    /**
     * 将角色领域对象转换为持久化对象
     */
    public RolePO toPO(Role role) {
        Objects.requireNonNull(role, "角色聚合根不能为空");
        
        RolePO po = new RolePO();
        po.setRoleId(role.getRoleId().getValue());
        po.setRoleName(role.getRoleName().getValue());
        po.setRoleDesc(role.getRoleDesc());
        po.setCategory(role.getCategory().getValue());
        po.setCreator(role.getCreator());
        po.setRemark(role.getRemark());
        po.setIsEnabled(role.isEnabled() ? 1 : 0);
        po.setCreateTime(role.getCreateTime());
        po.setUpdateTime(role.getUpdateTime());
        
        return po;
    }
    
    /**
     * 将持久化对象转换为角色领域对象
     */
    public Role toDO(RolePO po) {
        Objects.requireNonNull(po, "角色持久化对象不能为空");
        
        return Role.rebuild(
            RoleId.of(po.getRoleId()),
            RoleName.of(po.getRoleName()),
            po.getRoleDesc(),
            RoleStatus.of(po.getIsEnabled()),
            RoleCategory.of(po.getCategory()),
            po.getCreator(),
            po.getRemark(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    /**
     * 批量转换为领域对象列表
     */
    public List<Role> toDOList(List<RolePO> poList) {
        Objects.requireNonNull(poList, "PO列表不能为空");
        
        return poList.stream()
                .map(this::toDO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将权限持久化对象转换为权限领域实体
     */
    public Permission toPermissionDO(PermissionPO po) {
        Objects.requireNonNull(po, "权限持久化对象不能为空");
        
        return Permission.rebuild(
            PermissionId.of(po.getMenuId()),
            po.getParentId(),
            po.getMenuName(),
            po.getMenuId(), // 将menuId作为menuId字段
            po.getMenuType(),
            po.getOrderNum(),
            po.getPath(),
            po.getComponent(),
            po.getIcon(),
            po.getIsEnabled() == 1,
            po.getPerms(),
            po.getRemark(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    /**
     * 将权限领域实体转换为权限持久化对象
     */
    public PermissionPO toPermissionPO(Permission permission) {
        Objects.requireNonNull(permission, "权限领域实体不能为空");
        
        PermissionPO po = new PermissionPO();
        po.setMenuId(permission.getPermissionId().getValue());
        po.setParentId(permission.getParentId());
        po.setMenuName(permission.getMenuName());
        po.setMenuType(permission.getMenuType());
        po.setOrderNum(permission.getOrderNum());
        po.setPath(permission.getPath());
        po.setComponent(permission.getComponent());
        po.setIcon(permission.getIcon());
        po.setIsEnabled(permission.isEnabled() ? 1 : 0);
        po.setPerms(permission.getPermCode());
        po.setRemark(permission.getRemark());
        po.setCreateTime(permission.getCreateTime());
        po.setUpdateTime(permission.getUpdateTime());
        
        return po;
    }
    
    /**
     * 批量转换权限为领域实体列表
     */
    public List<Permission> toPermissionDOList(List<PermissionPO> poList) {
        Objects.requireNonNull(poList, "权限PO列表不能为空");
        
        return poList.stream()
                .map(this::toPermissionDO)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建角色查询规格对象的辅助方法
     */
    public static class RoleQuerySpec {
        private String roleNameLike;
        private String roleDescLike;
        private Integer category;
        private String creator;
        private String applicationId;
        private List<String> roleIds;
        private Integer isEnabled;
        
        public String getRoleNameLike() {
            return roleNameLike;
        }
        
        public void setRoleNameLike(String roleNameLike) {
            this.roleNameLike = roleNameLike;
        }
        
        public String getRoleDescLike() {
            return roleDescLike;
        }
        
        public void setRoleDescLike(String roleDescLike) {
            this.roleDescLike = roleDescLike;
        }
        
        public Integer getCategory() {
            return category;
        }
        
        public void setCategory(Integer category) {
            this.category = category;
        }
        
        public String getCreator() {
            return creator;
        }
        
        public void setCreator(String creator) {
            this.creator = creator;
        }
        
        public String getApplicationId() {
            return applicationId;
        }
        
        public void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }
        
        public List<String> getRoleIds() {
            return roleIds;
        }
        
        public void setRoleIds(List<String> roleIds) {
            this.roleIds = roleIds;
        }
        
        public Integer getIsEnabled() {
            return isEnabled;
        }
        
        public void setIsEnabled(Integer isEnabled) {
            this.isEnabled = isEnabled;
        }
    }
}