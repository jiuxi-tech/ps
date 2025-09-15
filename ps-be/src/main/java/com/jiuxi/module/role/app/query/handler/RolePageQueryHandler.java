package com.jiuxi.module.role.app.query.handler;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.module.role.app.query.dto.RolePageQuery;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.repo.RoleSpecification;
import com.jiuxi.module.role.domain.service.RoleAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色分页查询处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModulePageQueryHandler")
public class RolePageQueryHandler {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RoleAuthorizationService roleAuthorizationService;
    
    /**
     * 处理角色分页查询
     * 
     * @param query 分页查询条件
     * @return 分页结果
     */
    public IPage<TpRoleVO> handle(@Valid RolePageQuery query) {
        Objects.requireNonNull(query, "查询条件不能为空");
        
        // 获取可访问的角色
        List<String> operatorRoleIds = parseRoleIds(query.getOperatorRoleIds());
        List<Role> accessibleRoles = roleAuthorizationService.getAccessibleRoles(
            query.getOperatorId(),
            null, // 这里可以从查询中获取deptId
            operatorRoleIds
        );
        
        // 构建查询规格
        RoleSpecification spec = buildSpecification(query, accessibleRoles);
        
        // 创建分页对象
        Page<Role> page = new Page<>(query.getCurrent(), query.getSize());
        
        // 执行分页查询
        IPage<Role> rolePage = roleRepository.findPage(page, spec);
        
        // 转换为VO
        IPage<TpRoleVO> result = new Page<>(
            rolePage.getCurrent(),
            rolePage.getSize(),
            rolePage.getTotal()
        );
        
        List<TpRoleVO> voList = rolePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        result.setRecords(voList);
        
        return result;
    }
    
    /**
     * 构建查询规格
     */
    private RoleSpecification buildSpecification(RolePageQuery query, List<Role> accessibleRoles) {
        RoleSpecification spec = new RoleSpecification();
        
        spec.setRoleName(query.getRoleName());
        spec.setRoleDesc(query.getRoleDesc());
        spec.setCategory(query.getCategory());
        spec.setCreator(query.getCreator());
        spec.setApplicationId(query.getApplicationId());
        
        // 限制只能查询有权限访问的角色
        if (!accessibleRoles.isEmpty()) {
            List<String> accessibleRoleIds = accessibleRoles.stream()
                    .map(role -> role.getRoleId().getValue())
                    .collect(Collectors.toList());
            spec.setRoleIds(accessibleRoleIds);
        } else {
            // 如果没有可访问的角色，返回空结果
            spec.setRoleIds(Arrays.asList("NONE"));
        }
        
        return spec;
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