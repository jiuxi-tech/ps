package com.jiuxi.module.role.app.query.handler;

import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.module.role.app.query.dto.PermissionTreeQuery;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.repo.PermissionRepository;
import com.jiuxi.module.role.domain.service.RoleAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限树查询处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModulePermissionTreeQueryHandler")
public class PermissionTreeQueryHandler {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private RoleAuthorizationService roleAuthorizationService;
    
    /**
     * 处理权限树查询
     * 
     * @param query 查询条件
     * @return 权限树
     */
    public List<TreeNode> handle(@Valid PermissionTreeQuery query) {
        Objects.requireNonNull(query, "查询条件不能为空");
        
        // 验证角色存在性和访问权限
        RoleId roleId = RoleId.of(query.getRoleId());
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        
        if (!roleOptional.isPresent()) {
            throw new IllegalArgumentException("角色不存在: " + query.getRoleId());
        }
        
        Role role = roleOptional.get();
        validateAccessPermission(query, role);
        
        // 获取所有权限
        List<Permission> allPermissions = permissionRepository.findAll();
        
        // 获取角色已有权限
        List<Permission> rolePermissions = permissionRepository.findByRoleId(roleId);
        Set<String> rolePermissionIds = rolePermissions.stream()
                .map(p -> p.getPermissionId().getValue())
                .collect(Collectors.toSet());
        
        // 构建权限树
        List<TreeNode> treeNodes = buildPermissionTree(allPermissions, rolePermissionIds);
        
        return treeNodes;
    }
    
    /**
     * 验证访问权限
     */
    private void validateAccessPermission(PermissionTreeQuery query, Role role) {
        if (query.getOperatorId() != null) {
            List<String> operatorRoleIds = parseRoleIds(query.getOperatorRoleIds());
            
            boolean hasAccess = roleAuthorizationService.hasAccessToRole(
                query.getOperatorId(),
                null,
                operatorRoleIds,
                role.getRoleId()
            );
            
            if (!hasAccess) {
                throw new IllegalArgumentException("没有权限访问该角色的权限信息");
            }
        }
    }
    
    /**
     * 构建权限树
     */
    private List<TreeNode> buildPermissionTree(List<Permission> allPermissions, Set<String> rolePermissionIds) {
        Map<String, TreeNode> nodeMap = new HashMap<>();
        List<TreeNode> rootNodes = new ArrayList<>();
        
        // 创建所有节点
        for (Permission permission : allPermissions) {
            TreeNode node = new TreeNode();
            node.setId(permission.getPermissionId().getValue());
            // TreeNode使用setPid而不是setParentId
            node.setPid(permission.getParentId());
            node.setLabel(permission.getMenuName());
            node.setValue(permission.getMenuId());
            
            // 设置是否已选中（角色已有此权限）
            if (rolePermissionIds.contains(permission.getPermissionId().getValue())) {
                node.setChecked(true);
            }
            
            // 添加其他属性到扩展字段
            node.setExtend(permission.getMenuType());
            node.setExtend01(permission.getIcon());
            node.setExtend02(permission.getComponent());
            node.setExtend03(permission.getPath());
            
            nodeMap.put(permission.getPermissionId().getValue(), node);
        }
        
        // 构建父子关系
        for (Permission permission : allPermissions) {
            String permissionId = permission.getPermissionId().getValue();
            String parentId = permission.getParentId();
            
            TreeNode currentNode = nodeMap.get(permissionId);
            
            if (parentId == null || parentId.trim().isEmpty()) {
                // 根节点
                rootNodes.add(currentNode);
            } else {
                // 子节点
                TreeNode parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.addChildren(currentNode);
                }
            }
        }
        
        // 排序
        sortTreeNodes(rootNodes);
        
        return rootNodes;
    }
    
    /**
     * 递归排序树节点
     */
    private void sortTreeNodes(List<TreeNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        
        // 按排序号排序 - 由于TreeNode没有getAttributes方法，使用默认排序或orderNo字段
        nodes.sort((n1, n2) -> {
            // 如果TreeNode有orderNo字段，使用它排序
            if (n1.getOrderNo() != null && n2.getOrderNo() != null) {
                return n1.getOrderNo().compareTo(n2.getOrderNo());
            }
            // 否则按ID排序保证稳定性
            return n1.getId().compareTo(n2.getId());
        });
        
        // 递归排序子节点
        for (TreeNode node : nodes) {
            if (node.getChildren() != null) {
                sortTreeNodes(node.getChildren());
            }
        }
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