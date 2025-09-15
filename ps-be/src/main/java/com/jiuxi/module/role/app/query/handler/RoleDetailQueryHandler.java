package com.jiuxi.module.role.app.query.handler;

import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.module.role.app.query.dto.RoleDetailQuery;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.service.RoleAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 角色详情查询处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModuleDetailQueryHandler")
public class RoleDetailQueryHandler {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RoleAuthorizationService roleAuthorizationService;
    
    /**
     * 处理角色详情查询
     * 
     * @param query 查询条件
     * @return 角色详情
     */
    public TpRoleVO handle(@Valid RoleDetailQuery query) {
        Objects.requireNonNull(query, "查询条件不能为空");
        
        // 查找角色
        RoleId roleId = RoleId.of(query.getRoleId());
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        
        if (!roleOptional.isPresent()) {
            throw new IllegalArgumentException("角色不存在: " + query.getRoleId());
        }
        
        Role role = roleOptional.get();
        
        // 验证访问权限
        validateAccessPermission(query, role);
        
        // 转换为VO
        return convertToVO(role);
    }
    
    /**
     * 验证访问权限
     */
    private void validateAccessPermission(RoleDetailQuery query, Role role) {
        if (query.getOperatorId() != null) {
            List<String> operatorRoleIds = parseRoleIds(query.getOperatorRoleIds());
            
            boolean hasAccess = roleAuthorizationService.hasAccessToRole(
                query.getOperatorId(),
                null, // deptId 可以从查询中获取
                operatorRoleIds,
                role.getRoleId()
            );
            
            if (!hasAccess) {
                throw new IllegalArgumentException("没有权限访问该角色信息");
            }
        }
    }
    
    /**
     * 转换为VO对象
     */
    private TpRoleVO convertToVO(Role role) {
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
     * 解析角色ID字符串
     */
    private List<String> parseRoleIds(String roleIds) {
        if (roleIds == null || roleIds.trim().isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(roleIds.split(","));
    }
}