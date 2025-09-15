package com.jiuxi.module.role.infra.persistence.repo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.PermissionId;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.PermissionRepository;
import com.jiuxi.module.role.domain.repo.PermissionTreeNode;
import com.jiuxi.module.role.infra.persistence.assembler.RolePersistenceAssembler;
import com.jiuxi.module.role.infra.persistence.entity.PermissionPO;
import com.jiuxi.module.role.infra.persistence.mapper.PermissionBaseMapper;
import com.jiuxi.module.role.infra.persistence.mapper.RoleMenuMapper;
import com.jiuxi.module.role.infra.persistence.entity.RoleMenuPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限仓储实现
 * 基于MyBatis Plus的数据访问实现
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Repository("rolePermissionRepositoryImpl")
public class PermissionRepositoryImpl implements PermissionRepository {
    
    @Autowired
    private PermissionBaseMapper permissionBaseMapper;
    
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    
    @Autowired
    private RolePersistenceAssembler rolePersistenceAssembler;
    
    @Override
    public Optional<Permission> findById(PermissionId permissionId) {
        PermissionPO po = permissionBaseMapper.selectById(permissionId.getValue());
        if (po == null) {
            return Optional.empty();
        }
        return Optional.of(rolePersistenceAssembler.toPermissionDO(po));
    }
    
    @Override
    public Optional<Permission> findByMenuId(String menuId) {
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionPO::getMenuId, menuId)
                   .eq(PermissionPO::getIsDeleted, 0);
        
        PermissionPO po = permissionBaseMapper.selectOne(queryWrapper);
        if (po == null) {
            return Optional.empty();
        }
        return Optional.of(rolePersistenceAssembler.toPermissionDO(po));
    }
    
    @Override
    public List<Permission> findByRoleId(RoleId roleId) {
        // 通过角色菜单关联表查询
        List<PermissionPO> poList = roleMenuMapper.selectPermissionsByRoleId(roleId.getValue());
        return rolePersistenceAssembler.toPermissionDOList(poList);
    }
    
    @Override
    public List<Permission> findAll() {
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionPO::getIsDeleted, 0)
                   .eq(PermissionPO::getIsEnabled, 1)
                   .orderByAsc(PermissionPO::getOrderNum);
        
        List<PermissionPO> poList = permissionBaseMapper.selectList(queryWrapper);
        return rolePersistenceAssembler.toPermissionDOList(poList);
    }
    
    @Override
    public boolean existsById(PermissionId permissionId) {
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionPO::getMenuId, permissionId.getValue())
                   .eq(PermissionPO::getIsDeleted, 0);
        
        Long count = permissionBaseMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }
    
    @Override
    public boolean existsByMenuId(String menuId) {
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionPO::getMenuId, menuId)
                   .eq(PermissionPO::getIsDeleted, 0);
        
        Long count = permissionBaseMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }
    
    @Override
    public List<Permission> findByParentId(String parentId) {
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionPO::getParentId, parentId)
                   .eq(PermissionPO::getIsDeleted, 0)
                   .eq(PermissionPO::getIsEnabled, 1)
                   .orderByAsc(PermissionPO::getOrderNum);
        
        List<PermissionPO> poList = permissionBaseMapper.selectList(queryWrapper);
        return rolePersistenceAssembler.toPermissionDOList(poList);
    }
    
    @Override
    public List<Permission> findRootPermissions() {
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.isNull(PermissionPO::getParentId)
                                           .or()
                                           .eq(PermissionPO::getParentId, ""))
                   .eq(PermissionPO::getIsDeleted, 0)
                   .eq(PermissionPO::getIsEnabled, 1)
                   .orderByAsc(PermissionPO::getOrderNum);
        
        List<PermissionPO> poList = permissionBaseMapper.selectList(queryWrapper);
        return rolePersistenceAssembler.toPermissionDOList(poList);
    }
    
    @Override
    public List<Permission> findByIds(List<PermissionId> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return List.of();
        }
        
        List<String> ids = permissionIds.stream()
                .map(PermissionId::getValue)
                .collect(Collectors.toList());
        
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermissionPO::getMenuId, ids)
                   .eq(PermissionPO::getIsDeleted, 0);
        
        List<PermissionPO> poList = permissionBaseMapper.selectList(queryWrapper);
        return rolePersistenceAssembler.toPermissionDOList(poList);
    }
    
    @Override
    public List<Permission> findByMenuIds(List<String> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return List.of();
        }
        
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermissionPO::getMenuId, menuIds)
                   .eq(PermissionPO::getIsDeleted, 0);
        
        List<PermissionPO> poList = permissionBaseMapper.selectList(queryWrapper);
        return rolePersistenceAssembler.toPermissionDOList(poList);
    }
    
    @Override
    public List<PermissionTreeNode> buildPermissionTree() {
        // 获取所有权限
        List<Permission> allPermissions = findAll();
        
        // 构建权限树 - 这里返回简化实现，实际应该构建树形结构
        return allPermissions.stream()
                .map(this::toPermissionTreeNode)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PermissionTreeNode> buildRolePermissionTree(RoleId roleId) {
        // 获取所有权限
        List<Permission> allPermissions = findAll();
        // 获取角色已有权限
        List<Permission> rolePermissions = findByRoleId(roleId);
        Set<String> rolePermissionIds = rolePermissions.stream()
                .map(p -> p.getPermissionId().getValue())
                .collect(Collectors.toSet());
        
        // 构建权限树并标记已选中的权限
        return allPermissions.stream()
                .map(permission -> {
                    PermissionTreeNode node = toPermissionTreeNode(permission);
                    if (rolePermissionIds.contains(permission.getPermissionId().getValue())) {
                        node.markAsAssigned();
                    }
                    return node;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为权限树节点
     */
    private PermissionTreeNode toPermissionTreeNode(Permission permission) {
        return new PermissionTreeNode(
            permission.getPermissionId(),
            permission.getMenuId(),
            permission.getMenuName(),
            permission.getParentId()
        );
    }

    /**
     * 保存角色权限关联关系（基础设施层方法，不在领域接口中）
     */
    public void saveRolePermissions(RoleId roleId, List<Permission> permissions) {
        String roleIdValue = roleId.getValue();
        
        // 先删除原有关联关系
        roleMenuMapper.deleteByRoleId(roleIdValue);
        
        // 插入新的关联关系
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                roleMenuMapper.insertRoleMenu(roleIdValue, permission.getMenuId());
            }
        }
    }
    
    /**
     * 删除角色权限关联关系（基础设施层方法，不在领域接口中）
     */
    public void deleteRolePermissions(RoleId roleId) {
        roleMenuMapper.deleteByRoleId(roleId.getValue());
    }
    
}