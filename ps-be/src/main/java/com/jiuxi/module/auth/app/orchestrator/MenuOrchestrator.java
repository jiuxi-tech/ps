package com.jiuxi.module.auth.app.orchestrator;

import com.jiuxi.module.auth.app.command.dto.CreateMenuCommand;
import com.jiuxi.module.auth.app.command.dto.DeleteMenuCommand;
import com.jiuxi.module.auth.app.service.MenuCommandService;
import com.jiuxi.module.auth.app.service.MenuQueryService;
import com.jiuxi.module.auth.app.query.dto.MenuQueryDTO;
import com.jiuxi.module.auth.domain.event.MenuCreatedEvent;
import com.jiuxi.module.auth.domain.service.MenuDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单业务编排器
 * 协调复杂的菜单业务流程，处理跨多个聚合的业务逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
@Transactional
public class MenuOrchestrator {
    
    @Autowired
    private MenuCommandService menuCommandService;
    
    @Autowired
    private MenuQueryService menuQueryService;
    
    @Autowired
    private MenuDomainService menuDomainService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 创建菜单并初始化默认权限
     * 复杂业务流程：创建菜单 -> 生成默认权限 -> 关联角色
     * 
     * @param command 创建菜单命令
     * @param autoCreatePermissions 是否自动创建权限
     * @param defaultRoleIds 默认关联的角色ID列表
     * @return 菜单ID
     */
    public String createMenuWithPermissions(CreateMenuCommand command, 
                                          boolean autoCreatePermissions, 
                                          List<String> defaultRoleIds) {
        // 1. 创建菜单
        String menuId = menuCommandService.createMenu(command);
        
        // 2. 如果需要自动创建权限
        if (autoCreatePermissions) {
            createDefaultPermissionsForMenu(menuId, command.getMenuCode(), command.getOperator());
        }
        
        // 3. 关联到默认角色
        if (defaultRoleIds != null && !defaultRoleIds.isEmpty()) {
            associateMenuToRoles(menuId, defaultRoleIds, command.getOperator());
        }
        
        // 4. 发布菜单创建完成事件
        publishMenuCreatedEvent(menuId, command);
        
        return menuId;
    }
    
    /**
     * 批量删除菜单及其子菜单
     * 复杂业务流程：递归删除子菜单 -> 清理权限关联 -> 清理角色关联 -> 删除菜单
     * 
     * @param menuId 菜单ID
     * @param operator 操作者
     */
    public void deleteMenuHierarchy(String menuId, String operator) {
        // 1. 获取菜单详情
        MenuQueryDTO menu = menuQueryService.getMenuById(menuId);
        
        // 2. 递归删除子菜单
        List<MenuQueryDTO> childMenus = menuQueryService.getChildMenus(menuId, menu.getTenantId());
        for (MenuQueryDTO childMenu : childMenus) {
            deleteMenuHierarchy(childMenu.getMenuId(), operator);
        }
        
        // 3. 清理权限关联
        cleanupPermissionAssociations(menuId, operator);
        
        // 4. 清理角色关联
        cleanupRoleAssociations(menuId, operator);
        
        // 5. 删除菜单本身
        DeleteMenuCommand deleteCommand = new DeleteMenuCommand(menuId, operator);
        menuCommandService.deleteMenu(deleteCommand);
    }
    
    /**
     * 同步菜单权限状态
     * 复杂业务流程：菜单状态变更时同步其权限状态
     * 
     * @param menuId 菜单ID
     * @param enabled 是否启用
     * @param operator 操作者
     */
    public void syncMenuPermissionStatus(String menuId, boolean enabled, String operator) {
        // 1. 更新菜单状态
        if (enabled) {
            menuCommandService.enableMenu(menuId, operator);
        } else {
            menuCommandService.disableMenu(menuId, operator);
        }
        
        // 2. 同步相关权限状态
        syncRelatedPermissionStatus(menuId, enabled, operator);
        
        // 3. 清理权限缓存
        clearPermissionCache(menuId);
    }
    
    /**
     * 为菜单创建默认权限
     */
    private void createDefaultPermissionsForMenu(String menuId, String menuCode, String operator) {
        // 根据菜单编码生成默认的查看、编辑、删除权限
        // 这里是示例逻辑，实际实现需要根据业务需求定制
        
        String[] permissionSuffixes = {"VIEW", "EDIT", "DELETE"};
        for (String suffix : permissionSuffixes) {
            String permissionCode = menuCode + "_" + suffix;
            // 调用权限创建服务（这里简化处理）
            // permissionCommandService.createPermission(...)
        }
    }
    
    /**
     * 关联菜单到角色
     */
    private void associateMenuToRoles(String menuId, List<String> roleIds, String operator) {
        // 为指定角色添加菜单权限
        for (String roleId : roleIds) {
            // 调用角色权限分配服务（这里简化处理）
            // roleCommandService.assignMenuToRole(roleId, menuId, operator);
        }
    }
    
    /**
     * 清理权限关联
     */
    private void cleanupPermissionAssociations(String menuId, String operator) {
        // 清理与此菜单相关的权限
        // 这里需要调用权限服务来清理关联关系
    }
    
    /**
     * 清理角色关联
     */
    private void cleanupRoleAssociations(String menuId, String operator) {
        // 清理角色与此菜单的关联关系
        // 这里需要调用角色服务来清理关联关系
    }
    
    /**
     * 同步相关权限状态
     */
    private void syncRelatedPermissionStatus(String menuId, boolean enabled, String operator) {
        // 同步与菜单相关的权限状态
        // 这里需要调用权限服务来同步状态
    }
    
    /**
     * 清理权限缓存
     */
    private void clearPermissionCache(String menuId) {
        // 清理与菜单相关的权限缓存
        // 这里需要调用缓存服务来清理缓存
    }
    
    /**
     * 发布菜单创建事件
     */
    private void publishMenuCreatedEvent(String menuId, CreateMenuCommand command) {
        // 构建并发布领域事件
        // 这里简化处理，实际应该从聚合根获取事件
    }
}